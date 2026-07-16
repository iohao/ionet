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
import io.aeron.*;
import java.util.function.*;
import lombok.extern.slf4j.*;
import org.agrona.concurrent.*;

/**
 * Encodes and offers publisher messages to Aeron.
 *
 * @author 渔民小镇
 * @date 2026-05-01
 * @since 25.4
 */
@Slf4j
final class PublisherMessageKit {
    private PublisherMessageKit() {
    }

    static boolean publish(
            String publicationName,
            Object message,
            Publication publication,
            MessageHeaderEncoder headerEncoder,
            UnsafeBuffer buffer,
            BooleanSupplier running
    ) {
        MessageSbe<Object> encoder = SbeMessageManager.getMessageEncoder(message.getClass());
        if (encoder == null) {
            log.error("MessageSbe Error: {} not exist!", message.getClass().getSimpleName());
            return false;
        }

        try {
            encoder.encoder(message, headerEncoder, buffer);
        } catch (IndexOutOfBoundsException e) {
            log.error(
                    "Message [{}] encoding exceeded the publisher buffer capacity ({} bytes) and was dropped; "
                            + "increase CoreGlobalConfig.publisherBufferSize to allow a larger payload.",
                    message.getClass().getSimpleName(), buffer.capacity(), e);
            return false;
        } catch (IllegalStateException e) {
            log.error(
                    "Message [{}] encoding violated an SBE schema limit and was dropped.",
                    message.getClass().getSimpleName(), e);
            return false;
        }

        int limit = encoder.limit();
        int maxMessageLength = publication.maxMessageLength();
        if (limit > maxMessageLength) {
            log.error(
                    "Message [{}] encoded length ({} bytes) exceeded publication [{}] maximum message length "
                            + "({} bytes) and was dropped; reduce the payload or increase the Aeron term buffer length.",
                    message.getClass().getSimpleName(), limit, publicationName, maxMessageLength);
            return false;
        }

        long result = publication.offer(buffer, 0, limit);
        if (result <= 0) {
            return PublicationOfferKit.offerAfterFailedResult(
                    publicationName,
                    message,
                    result,
                    () -> publication.offer(buffer, 0, limit),
                    running
            );
        }

        return true;
    }
}
