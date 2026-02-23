/*
 * ionet
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.iohao.net.common;

import com.iohao.net.framework.CoreGlobalConfig;
import com.iohao.net.common.kit.CollKit;
import com.iohao.net.common.kit.concurrent.ExecutorKit;
import com.iohao.net.sbe.MessageHeaderEncoder;
import io.aeron.Publication;
import lombok.extern.slf4j.Slf4j;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Default single-threaded {@link Publisher} implementation backed by per-publication queues.
 *
 * <p>Messages are queued by publication name and encoded on the publisher thread before
 * being offered to Aeron.</p>
 *
 * @author 渔民小镇
 * @date 2025-09-27
 * @since 25.1
 */
@Slf4j
public final class DefaultPublisher implements Publisher {
    final Map<String, Publication> publicationMap = CollKit.ofConcurrentHashMap();
    final Map<String, Queue<Object>> messageQueueMap = CollKit.ofConcurrentHashMap();
    volatile boolean running = true;
    ExecutorService executorService;

    @Override
    public void addPublication(String name, Publication publication) {
        publicationMap.put(name, publication);
        messageQueueMap.putIfAbsent(name, new LinkedBlockingQueue<>());
    }

    @Override
    public void publishMessage(String name, Object message) {
        var queue = messageQueueMap.get(name);
        if (queue != null) {
            queue.offer(message);
        }
    }

    @Override
    public void startup() {
        executorService = ExecutorKit.newSingleScheduled("Publisher");
        executorService.submit(new DefaultPublisherRunnable());
    }

    @Override
    public void shutdown() {
        running = false;
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    private class DefaultPublisherRunnable implements PublisherRunnable {
        private final UnsafeBuffer buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(CoreGlobalConfig.publisherBufferSize));
        private final MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();

        @Override
        public void run() {
            try {
                while (running) {
                    extracted();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                publicationMap.values().forEach(Publication::close);
            }
        }

        private void extracted() throws Exception {
            boolean messagesPublished = false;
            for (String key : messageQueueMap.keySet()) {
                var queue = messageQueueMap.get(key);
                var publication = publicationMap.get(key);

                if (queue == null || publication == null) {
                    continue;
                }

                Object message;
                while ((message = queue.poll()) != null) {
                    messagesPublished = true;
                    MessageSbe<Object> encoder = SbeMessageManager.getMessageEncoder(message.getClass());

                    if (encoder == null) {
                        log.error("MessageSbe Error: {} not exist!", message.getClass().getSimpleName());
                        continue;
                    }

                    encoder.encoder(message, headerEncoder, buffer);
                    int limit = encoder.limit();
                    // The encoder writes into the shared direct buffer and reports the message boundary via limit().
                    publication.offer(buffer, 0, limit);
                }
            }

            if (!messagesPublished) {
                // Back off briefly when all queues are empty to avoid a busy-spin loop.
                TimeUnit.MILLISECONDS.sleep(1);
            }
        }
    }
}
