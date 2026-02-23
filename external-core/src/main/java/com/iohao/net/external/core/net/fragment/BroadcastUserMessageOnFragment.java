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
package com.iohao.net.external.core.net.fragment;

import com.iohao.net.framework.protocol.CommunicationMessage;
import com.iohao.net.common.kit.ByteKit;
import com.iohao.net.external.core.message.CommunicationMessageKit;
import com.iohao.net.external.core.message.ExternalServerSingle;
import com.iohao.net.external.core.message.ExternalWriteKit;
import com.iohao.net.common.OnFragment;
import com.iohao.net.sbe.BroadcastUserMessageDecoder;
import com.iohao.net.common.SbeKit;
import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;

/**
 * Aeron fragment consumer that routes a decoded message to one target user session.
 *
 * @author 渔民小镇
 * @date 2025-09-03
 * @since 25.1
 */
public class BroadcastUserMessageOnFragment implements OnFragment {
    final BroadcastUserMessageDecoder decoder = new BroadcastUserMessageDecoder();

    @Override
    public void process(DirectBuffer buffer, int offset, int actingBlockLength, int actingVersion, Header header) {
        decoder.wrap(buffer, offset, actingBlockLength, actingVersion);

        var message = CommunicationMessageKit.createCommunicationMessage();
        message.setCmdMerge(decoder.cmdMerge());
        message.setErrorCode(decoder.errorCode());
        message.setErrorMessage(decoder.errorMessage());
        message.setExternalServerId(decoder.externalServerId());
        SbeKit.decoderUserIdentity(message, decoder.userIdentity());

        var dataLength = decoder.dataLength();
        var dataBytes = ByteKit.ofBytes(dataLength);
        decoder.getData(dataBytes, 0, dataLength);
        message.setData(dataBytes);

        writeAndFlush(message);
    }

    /**
     * Resolve and write the decoded message to its target user session.
     *
     * @param message decoded outbound message
     */
    protected void writeAndFlush(CommunicationMessage message) {
        var userSessions = ExternalServerSingle.userSessions;
        ExternalWriteKit.writeAndFlush(message, userSessions);
    }

    @Override
    public int getTemplateId() {
        return BroadcastUserMessageDecoder.TEMPLATE_ID;
    }
}
