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

import com.iohao.net.common.kit.*;
import com.iohao.net.framework.*;
import com.iohao.net.sbe.*;
import io.aeron.*;
import java.nio.*;
import java.util.*;
import java.util.concurrent.*;
import lombok.extern.slf4j.*;
import org.agrona.concurrent.*;

/**
 * Parallel multi-threaded {@link Publisher} implementation with per-publication virtual threads.
 *
 * <p>Each publication runs on its own virtual thread, eliminating head-of-line blocking
 * between different publications and improving throughput under concurrent load. Messages
 * sent to the same publication maintain FIFO ordering, while different publications can
 * publish in parallel.</p>
 *
 * <p>Use this implementation when you have multiple publications with high message rates
 * and want to maximize throughput by parallelizing publication work.</p>
 *
 * @author 渔民小镇
 * @date 2026-03-01
 * @since 25.1
 */
@Slf4j
public final class ParallelPublisher implements Publisher {
    final Map<String, Publication> publicationMap = CollKit.ofConcurrentHashMap();
    final Map<String, Queue<Object>> messageQueueMap = CollKit.ofConcurrentHashMap();
    final Map<String, Thread> threadMap = CollKit.ofConcurrentHashMap();
    volatile boolean running = true;

    @Override
    public void addPublication(String name, Publication publication) {
        publicationMap.put(name, publication);
        messageQueueMap.putIfAbsent(name, new LinkedBlockingQueue<>());

        if (running) {
            startPublicationThread(name);
        }
    }

    private void startPublicationThread(String name) {
        threadMap.computeIfAbsent(name, key -> {
            var thread = Thread.ofVirtual()
                    .name("ParallelPublisher-" + key)
                    .start(new PublicationRunnable(key));
            return thread;
        });
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
        running = true;
        publicationMap.keySet().forEach(this::startPublicationThread);
    }

    @Override
    public void shutdown() {
        running = false;
        threadMap.values().forEach(thread -> {
            try {
                thread.interrupt();
                thread.join(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        publicationMap.values().forEach(Publication::close);
        threadMap.clear();
    }

    private class PublicationRunnable implements Runnable {
        private final String publicationName;
        private final UnsafeBuffer buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(CoreGlobalConfig.publisherBufferSize));
        private final MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();

        PublicationRunnable(String publicationName) {
            this.publicationName = publicationName;
        }

        @Override
        public void run() {
            try {
                var queue = messageQueueMap.get(publicationName);
                var publication = publicationMap.get(publicationName);

                if (queue == null || publication == null) {
                    return;
                }

                while (running) {
                    Object message = queue.poll();
                    if (message == null) {
                        TimeUnit.MILLISECONDS.sleep(1);
                        continue;
                    }

                    MessageSbe<Object> encoder = SbeMessageManager.getMessageEncoder(message.getClass());
                    if (encoder == null) {
                        log.error("MessageSbe Error: {} not exist!", message.getClass().getSimpleName());
                        continue;
                    }

                    encoder.encoder(message, headerEncoder, buffer);
                    int limit = encoder.limit();
                    publication.offer(buffer, 0, limit);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
