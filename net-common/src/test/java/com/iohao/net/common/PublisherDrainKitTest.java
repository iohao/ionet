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

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests publisher queue drain fairness.
 *
 * @author 渔民小镇
 * @date 2026-05-01
 * @since 25.4
 */
class PublisherDrainKitTest {

    @Test
    void positiveDrainLimitLeavesRemainingMessagesQueued() {
        Queue<Object> queue = new ArrayDeque<>(List.of("first", "second", "third"));
        var publishedMessages = new ArrayList<>();

        boolean drained = PublisherDrainKit.drain(queue, 2, publishedMessages::add);

        assertTrue(drained);
        assertEquals(List.of("first", "second"), publishedMessages);
        assertEquals(1, queue.size());
        assertEquals("third", queue.poll());
    }

    @Test
    void zeroDrainLimitDrainsAllMessages() {
        Queue<Object> queue = new ArrayDeque<>(List.of("first", "second", "third"));
        var publishedMessages = new ArrayList<>();

        boolean drained = PublisherDrainKit.drain(queue, 0, publishedMessages::add);

        assertTrue(drained);
        assertEquals(List.of("first", "second", "third"), publishedMessages);
        assertTrue(queue.isEmpty());
    }

    @Test
    void negativeDrainLimitDrainsAllMessages() {
        Queue<Object> queue = new ArrayDeque<>(List.of("first", "second"));
        var publishedMessages = new ArrayList<>();

        boolean drained = PublisherDrainKit.drain(queue, -1, publishedMessages::add);

        assertTrue(drained);
        assertEquals(List.of("first", "second"), publishedMessages);
        assertTrue(queue.isEmpty());
    }

    @Test
    void emptyQueueReturnsFalse() {
        Queue<Object> queue = new ArrayDeque<>();
        var publishedMessages = new ArrayList<>();

        boolean drained = PublisherDrainKit.drain(queue, 2, publishedMessages::add);

        assertFalse(drained);
        assertTrue(publishedMessages.isEmpty());
    }
}
