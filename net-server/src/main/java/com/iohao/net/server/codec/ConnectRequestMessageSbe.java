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
package com.iohao.net.server.codec;

import com.iohao.net.common.*;
import com.iohao.net.common.kit.*;
import com.iohao.net.framework.protocol.*;
import com.iohao.net.sbe.*;
import java.util.*;
import org.agrona.*;

/**
 * SBE encoder for {@link ServerRequestMessage} registration messages.
 *
 * @author 渔民小镇
 * @date 2025-09-06
 * @since 25.1
 */
@SuppressWarnings("all")
public final class ConnectRequestMessageSbe implements MessageSbe<ServerRequestMessage> {
    final ConnectRequestMessageEncoder encoder = new ConnectRequestMessageEncoder();

    @Override
    public void encoder(ServerRequestMessage message, MessageHeaderEncoder headerEncoder, MutableDirectBuffer buffer) {
        encoder.wrapAndApplyHeader(buffer, 0, headerEncoder);
        ServerMessageCodecKit.encoder(message, encoder.common());
        encoder.futureId(message.getFutureId());

        var cmdMerges = message.getCmdMerges();
        if (ArrayKit.isEmpty(cmdMerges)) {
            encoder.cmdMergesCount(0);
        } else {
            var _encoder = encoder.cmdMergesCount(cmdMerges.length);
            for (var value : cmdMerges) {
                _encoder.next().value(value);
            }
        }

        var payloadMap = message.getPayloadMap();
        var payloadEncoder = encoder.payloadCount(payloadMap.size());
        for (Map.Entry<String, byte[]> entry : payloadMap.entrySet()) {
            byte[] value = entry.getValue();
            payloadEncoder.next().key(entry.getKey()).putValue(value, 0, value.length);
        }
    }

    @Override
    public int limit() {
        return encoder.limit();
    }
}
