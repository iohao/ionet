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
package com.iohao.net.server.logic.codec;

import com.iohao.net.framework.protocol.ResponseMessage;
import com.iohao.net.common.SbeKit;
import com.iohao.net.common.MessageSbe;
import com.iohao.net.common.kit.ByteKit;
import com.iohao.net.sbe.MessageHeaderEncoder;
import com.iohao.net.sbe.ResponseMessageEncoder;
import lombok.extern.slf4j.Slf4j;
import org.agrona.MutableDirectBuffer;

/**
 * SBE encoder for internal response messages.
 *
 * @author 渔民小镇
 * @date 2025-09-06
 * @since 25.1
 */
@Slf4j
@SuppressWarnings("all")
public final class ResponseMessageSbe implements MessageSbe<ResponseMessage> {
    final ResponseMessageEncoder encoder = new ResponseMessageEncoder();

    @Override
    public void encoder(ResponseMessage message, MessageHeaderEncoder headerEncoder, MutableDirectBuffer buffer) {
        encoder.wrapAndApplyHeader(buffer, 0, headerEncoder);

        encoder.errorCode((short) message.getErrorCode())
                .errorMessage(message.getErrorMessage());

        SbeKit.encoderMessageCommon(message, encoder.common());

        var data = ByteKit.getBytes(message.getData());
        encoder.putData(data, 0, data.length);
    }

    @Override
    public int limit() {
        return encoder.limit();
    }
}
