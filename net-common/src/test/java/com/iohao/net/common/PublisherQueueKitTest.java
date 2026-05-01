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
import org.junit.jupiter.api.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests publisher queue overload protection.
 *
 * @author 渔民小镇
 * @date 2026-05-01
 * @since 25.4
 */
class PublisherQueueKitTest {
    int originalPublisherQueueCapacity;

    @BeforeEach
    void setUp() {
        this.originalPublisherQueueCapacity = CoreGlobalConfig.publisherQueueCapacity;
    }

    @AfterEach
    void tearDown() {
        CoreGlobalConfig.publisherQueueCapacity = this.originalPublisherQueueCapacity;
    }

    @Test
    void positiveCapacityCreatesBoundedQueue() {
        CoreGlobalConfig.publisherQueueCapacity = 2;

        var queue = PublisherQueueKit.newMessageQueue();

        assertInstanceOf(ArrayBlockingQueue.class, queue);
        assertTrue(queue.offer("first"));
        assertTrue(queue.offer("second"));
        assertFalse(queue.offer("third"));
    }

    @Test
    void zeroOrNegativeCapacityCreatesUnboundedQueue() {
        CoreGlobalConfig.publisherQueueCapacity = 0;
        assertInstanceOf(LinkedBlockingQueue.class, PublisherQueueKit.newMessageQueue());

        CoreGlobalConfig.publisherQueueCapacity = -1;
        assertInstanceOf(LinkedBlockingQueue.class, PublisherQueueKit.newMessageQueue());
    }

    @Test
    void fullBoundedQueueDropsNewestAndKeepsFifoOrder() {
        CoreGlobalConfig.publisherQueueCapacity = 2;
        var queue = PublisherQueueKit.newMessageQueue();
        var droppedMessageCountMap = new ConcurrentHashMap<String, AtomicLong>();

        assertTrue(PublisherQueueKit.offer("test", "first", queue, droppedMessageCountMap));
        assertTrue(PublisherQueueKit.offer("test", "second", queue, droppedMessageCountMap));
        assertFalse(PublisherQueueKit.offer("test", "third", queue, droppedMessageCountMap));

        assertEquals(2, queue.size());
        assertEquals("first", queue.poll());
        assertEquals("second", queue.poll());
        assertEquals(1, droppedMessageCountMap.get("test").get());
    }

    @Test
    void droppedMessageCountIncrementsOnRejectedEnqueue() {
        CoreGlobalConfig.publisherQueueCapacity = 1;
        var queue = PublisherQueueKit.newMessageQueue();
        var droppedMessageCountMap = new ConcurrentHashMap<String, AtomicLong>();

        assertTrue(PublisherQueueKit.offer("test", "first", queue, droppedMessageCountMap));
        assertFalse(PublisherQueueKit.offer("test", "second", queue, droppedMessageCountMap));
        assertFalse(PublisherQueueKit.offer("test", "third", queue, droppedMessageCountMap));

        assertEquals(2, droppedMessageCountMap.get("test").get());
    }
}
