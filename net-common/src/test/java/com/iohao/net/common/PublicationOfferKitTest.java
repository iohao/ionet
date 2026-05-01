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

import io.aeron.*;
import org.junit.jupiter.api.*;

import java.util.concurrent.atomic.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests Aeron publication failed-offer result handling.
 *
 * @author 渔民小镇
 * @date 2026-04-28
 * @since 25.4
 */
class PublicationOfferKitTest {

    @Test
    void retryableResultsRetryUntilSuccess() {
        long[] retryResults = {Publication.ADMIN_ACTION, 1};
        AtomicInteger offerCount = new AtomicInteger();
        AtomicInteger idleCount = new AtomicInteger();

        boolean result = PublicationOfferKit.offerAfterFailedResult(
                "test",
                "message",
                Publication.BACK_PRESSURED,
                () -> retryResults[offerCount.getAndIncrement()],
                () -> true,
                idleCount::incrementAndGet
        );

        assertTrue(result);
        assertEquals(2, offerCount.get());
        assertEquals(2, idleCount.get());
    }

    @Test
    void defaultRetryIdleStrategyCanRetryUntilSuccess() {
        long[] retryResults = {Publication.ADMIN_ACTION, 1};
        AtomicInteger offerCount = new AtomicInteger();

        boolean result = PublicationOfferKit.offerAfterFailedResult(
                "test",
                "message",
                Publication.BACK_PRESSURED,
                () -> retryResults[offerCount.getAndIncrement()],
                () -> true
        );

        assertTrue(result);
        assertEquals(2, offerCount.get());
    }

    @Test
    void terminalResultsFailWithoutRetry() {
        assertTerminalFailure(Publication.NOT_CONNECTED);
        assertTerminalFailure(Publication.CLOSED);
        assertTerminalFailure(Publication.MAX_POSITION_EXCEEDED);
    }

    @Test
    void interruptedRetryRestoresInterruptFlag() {
        AtomicInteger offerCount = new AtomicInteger();

        boolean result = PublicationOfferKit.offerAfterFailedResult(
                "test",
                "message",
                Publication.BACK_PRESSURED,
                () -> {
                    offerCount.incrementAndGet();
                    return 1;
                },
                () -> true,
                () -> {
                    throw new InterruptedException("interrupted");
                }
        );

        assertFalse(result);
        assertEquals(0, offerCount.get());
        assertTrue(Thread.currentThread().isInterrupted());

        Thread.interrupted();
    }

    private static void assertTerminalFailure(long offerResult) {
        AtomicInteger offerCount = new AtomicInteger();
        AtomicInteger idleCount = new AtomicInteger();

        boolean result = PublicationOfferKit.offerAfterFailedResult(
                "test",
                "message",
                offerResult,
                () -> {
                    offerCount.incrementAndGet();
                    return 1;
                },
                () -> true,
                idleCount::incrementAndGet
        );

        assertFalse(result);
        assertEquals(0, offerCount.get());
        assertEquals(0, idleCount.get());
    }
}
