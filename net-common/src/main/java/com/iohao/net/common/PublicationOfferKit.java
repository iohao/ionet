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
        while (running.getAsBoolean() && isRetryable(result)) {
            if (!idle(publicationName, message, retryIdle)) {
                return false;
            }

            result = retryOffer.offer();
            if (result > 0) {
                return true;
            }
        }

        if (result < 0) {
            log.error("Aeron publication offer failed. publicationName: {}, messageType: {}, result: {}",
                    publicationName,
                    messageType(message),
                    Publication.errorString(result));
        }

        return false;
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
