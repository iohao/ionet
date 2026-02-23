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

import com.iohao.net.framework.protocol.CommunicationMessage;
import com.iohao.net.common.SbeKit;
import com.iohao.net.common.MessageSbe;
import com.iohao.net.common.kit.ByteKit;
import com.iohao.net.sbe.MessageHeaderEncoder;
import com.iohao.net.sbe.UserRequestMessageEncoder;
import org.agrona.MutableDirectBuffer;

/**
 * SBE encoder for outbound {@link CommunicationMessage} instances sent to the net server.
 *
 * @author 渔民小镇
 * @date 2025-09-06
 * @since 25.1
 */
@SuppressWarnings("all")
public class CommunicationMessageSbe implements MessageSbe<CommunicationMessage> {
    protected final UserRequestMessageEncoder encoder = new UserRequestMessageEncoder();

    @Override
    public void encoder(CommunicationMessage message, MessageHeaderEncoder headerEncoder, MutableDirectBuffer buffer) {
        encoder.wrapAndApplyHeader(buffer, 0, headerEncoder);

        SbeKit.encoderMessageCommon(message, encoder.common());
        SbeKit.encoderUserIdentity(message, encoder.userIdentity());

        encoder.msgId(message.getMsgId())
                .stick((byte) message.getStick())
                .cacheCondition(message.getCacheCondition())
        ;

        extracted(message);

        var data = ByteKit.getBytes(message.getData());
        encoder.putData(data, 0, data.length);

        var attachment = ByteKit.getBytes(message.getAttachment());
        encoder.putAttachment(attachment, 0, attachment.length);
    }

    /**
     * Hook for subclasses to encode transport-specific extensions before payload/attachment bytes.
     *
     * @param message communication message being encoded
     */
    protected void extracted(CommunicationMessage message) {
        encoder.bindingLogicServerIdsCount(0);
    }

    @Override
    public int limit() {
        return encoder.limit();
    }
}
