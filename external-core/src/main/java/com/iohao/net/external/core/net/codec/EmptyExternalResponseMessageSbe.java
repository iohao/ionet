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
package com.iohao.net.external.core.net.codec;

import com.iohao.net.framework.protocol.EmptyExternalResponseMessage;
import com.iohao.net.common.MessageSbe;
import com.iohao.net.sbe.MessageHeaderEncoder;
import com.iohao.net.sbe.EmptyExternalResponseMessageEncoder;
import org.agrona.MutableDirectBuffer;

/**
 * SBE encoder for {@link EmptyExternalResponseMessage}.
 *
 * @author 渔民小镇
 * @date 2025-09-18
 * @since 25.1
 */
@SuppressWarnings("all")
public final class EmptyExternalResponseMessageSbe implements MessageSbe<EmptyExternalResponseMessage> {
    final EmptyExternalResponseMessageEncoder encoder = new EmptyExternalResponseMessageEncoder();

    @Override
    public void encoder(EmptyExternalResponseMessage message, MessageHeaderEncoder headerEncoder, MutableDirectBuffer buffer) {
        encoder.wrapAndApplyHeader(buffer, 0, headerEncoder);

        encoder.futureId(message.getFutureId());
        encoder.errorCode((short) message.getErrorCode());

        if (message.getErrorCode() != 0) {
            encoder.errorMessage(message.getErrorMessage());
        }
    }

    @Override
    public int limit() {
        return encoder.limit();
    }
}
