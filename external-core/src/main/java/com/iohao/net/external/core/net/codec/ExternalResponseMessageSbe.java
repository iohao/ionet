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

import com.iohao.net.framework.protocol.ExternalResponseMessage;
import com.iohao.net.common.MessageSbe;
import com.iohao.net.common.kit.ByteKit;
import com.iohao.net.sbe.ExternalResponseMessageEncoder;
import com.iohao.net.sbe.MessageHeaderEncoder;
import org.agrona.MutableDirectBuffer;

/**
 * SBE encoder for {@link ExternalResponseMessage} returned to internal request senders.
 *
 * @author 渔民小镇
 * @date 2025-09-11
 * @since 25.1
 */
@SuppressWarnings("all")
public final class ExternalResponseMessageSbe implements MessageSbe<ExternalResponseMessage> {
    final ExternalResponseMessageEncoder encoder = new ExternalResponseMessageEncoder();

    @Override
    public void encoder(ExternalResponseMessage message, MessageHeaderEncoder headerEncoder, MutableDirectBuffer buffer) {
        encoder.wrapAndApplyHeader(buffer, 0, headerEncoder);

        encoder.futureId(message.getFutureId())
                .errorCode((short) message.getErrorCode())
                .errorMessage(message.getErrorMessage())
                .externalServerId(message.getExternalServerId())
        ;

        var payload = ByteKit.getBytes(message.getPayload());
        encoder.putPayload(payload, 0, payload.length);
    }

    @Override
    public int limit() {
        return encoder.limit();
    }
}
