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

import com.iohao.net.sbe.*;
import java.util.concurrent.*;
import java.util.function.*;
import org.agrona.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test helpers for publisher integration tests.
 *
 * @author 渔民小镇
 * @date 2026-05-04
 * @since 25.4
 */
final class PublisherTestKit {
    private PublisherTestKit() {
    }

    static void registerTestMessageEncoder() {
        if (SbeMessageManager.getMessageEncoder(TestMessage.class) == null) {
            SbeMessageManager.register(TestMessage.class, new TestMessageSbe());
        }

        if (SbeMessageManager.getMessageEncoder(OversizedTestMessage.class) == null) {
            SbeMessageManager.register(OversizedTestMessage.class, new OversizedTestMessageSbe());
        }
    }

    static void awaitUntil(BooleanSupplier condition) throws InterruptedException {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        while (System.nanoTime() < deadline) {
            if (condition.getAsBoolean()) {
                return;
            }

            Thread.sleep(10);
        }

        fail("Condition was not met before timeout");
    }

    record TestMessage(int value) {
    }

    record OversizedTestMessage(int value) {
    }

    static final class TestMessageSbe implements MessageSbe<TestMessage> {
        private static final int ENCODED_LENGTH = 8;

        @Override
        public void encoder(TestMessage message, MessageHeaderEncoder headerEncoder, MutableDirectBuffer buffer) {
            buffer.putInt(0, message.value());
        }

        @Override
        public int limit() {
            return ENCODED_LENGTH;
        }
    }

    static final class OversizedTestMessageSbe implements MessageSbe<OversizedTestMessage> {
        private static final int ENCODED_LENGTH = 16;

        @Override
        public void encoder(
                OversizedTestMessage message,
                MessageHeaderEncoder headerEncoder,
                MutableDirectBuffer buffer
        ) {
            buffer.putInt(0, message.value());
        }

        @Override
        public int limit() {
            return ENCODED_LENGTH;
        }
    }
}
