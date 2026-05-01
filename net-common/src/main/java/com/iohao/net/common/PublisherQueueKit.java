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

import com.iohao.net.framework.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import lombok.extern.slf4j.*;

/**
 * Creates and writes publisher queues with overload protection.
 *
 * @author 渔民小镇
 * @date 2026-05-01
 * @since 25.4
 */
@Slf4j
final class PublisherQueueKit {
    private static final long DROP_LOG_INTERVAL = 1024;

    private PublisherQueueKit() {
    }

    static Queue<Object> newMessageQueue() {
        var capacity = CoreGlobalConfig.publisherQueueCapacity;
        if (capacity <= 0) {
            return new LinkedBlockingQueue<>();
        }

        return new ArrayBlockingQueue<>(capacity);
    }

    static boolean offer(
            String publicationName,
            Object message,
            Queue<Object> queue,
            Map<String, AtomicLong> droppedMessageCountMap
    ) {
        if (queue.offer(message)) {
            return true;
        }

        var droppedCount = droppedMessageCountMap
                .computeIfAbsent(publicationName, key -> new AtomicLong())
                .incrementAndGet();

        if (droppedCount == 1 || droppedCount % DROP_LOG_INTERVAL == 0) {
            log.warn(
                    "Publisher queue full, dropped newest message. publicationName: {}, messageType: {}, "
                            + "queueSize: {}, queueCapacity: {}, droppedCount: {}",
                    publicationName,
                    messageType(message),
                    queue.size(),
                    capacity(queue),
                    droppedCount);
        }

        return false;
    }

    private static int capacity(Queue<Object> queue) {
        if (queue instanceof BlockingQueue<?> blockingQueue) {
            return queue.size() + blockingQueue.remainingCapacity();
        }

        return -1;
    }

    private static String messageType(Object message) {
        return message == null ? "null" : message.getClass().getSimpleName();
    }
}
