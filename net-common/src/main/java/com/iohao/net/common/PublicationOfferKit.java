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
import io.aeron.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import lombok.extern.slf4j.*;

/**
 * Handles Aeron publication offer return codes.
 *
 * @author 渔民小镇
 * @date 2026-04-28
 * @since 25.4
 */
@Slf4j
final class PublicationOfferKit {
    private static final long DROP_LOG_INTERVAL = 1024;
    private static final AtomicLong droppedOfferCount = new AtomicLong();

    private PublicationOfferKit() {
    }

    static boolean offerAfterFailedResult(
            String publicationName,
            Object message,
            long result,
            RetryOffer retryOffer,
            BooleanSupplier running
    ) {
        return offerAfterFailedResult(
                publicationName,
                message,
                result,
                retryOffer,
                running,
                PublisherIdleStrategyKit.newIdleStrategy()::idle
        );
    }

    static boolean offerAfterFailedResult(
            String publicationName,
            Object message,
            long result,
            RetryOffer retryOffer,
            BooleanSupplier running,
            RetryIdle retryIdle
    ) {
        int retryCount = 0;
        int retryLimit = CoreGlobalConfig.publisherOfferRetryLimit;
        while (running.getAsBoolean() && isRetryable(result) && canRetry(retryCount, retryLimit)) {
            if (!idle(publicationName, message, retryIdle)) {
                return false;
            }

            result = retryOffer.offer();
            retryCount++;
            if (result > 0) {
                return true;
            }
        }

        if (result < 0) {
            logFailedOffer(publicationName, message, result, retryCount, retryLimit);
        }

        return false;
    }

    private static boolean canRetry(int retryCount, int retryLimit) {
        return retryLimit < 0 || retryCount < retryLimit;
    }

    private static void logFailedOffer(String publicationName, Object message, long result, int retryCount, int retryLimit) {
        if (isRetryable(result) && retryLimit >= 0 && retryCount >= retryLimit) {
            logRetryLimitReached(publicationName, message, result, retryCount);
            return;
        }

        log.error("Aeron publication offer failed. publicationName: {}, messageType: {}, result: {}",
                publicationName,
                messageType(message),
                Publication.errorString(result));
    }

    private static void logRetryLimitReached(String publicationName, Object message, long result, int retryCount) {
        var count = droppedOfferCount.incrementAndGet();

        if (count == 1 || count % DROP_LOG_INTERVAL == 0) {
            log.warn("Aeron publication offer retry limit reached, dropped message. publicationName: {}, "
                            + "messageType: {}, result: {}, retryCount: {}, droppedCount: {}",
                    publicationName,
                    messageType(message),
                    Publication.errorString(result),
                    retryCount,
                    count);
        }
    }

    private static boolean idle(String publicationName, Object message, RetryIdle retryIdle) {
        try {
            retryIdle.idle();
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Aeron publication offer retry interrupted. publicationName: {}, messageType: {}",
                    publicationName,
                    messageType(message));
            return false;
        }
    }

    private static boolean isRetryable(long result) {
        return result == Publication.BACK_PRESSURED || result == Publication.ADMIN_ACTION;
    }

    private static String messageType(Object message) {
        return message == null ? "null" : message.getClass().getSimpleName();
    }

    @FunctionalInterface
    interface RetryOffer {
        long offer();
    }

    @FunctionalInterface
    interface RetryIdle {
        void idle() throws InterruptedException;
    }
}
