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

import com.iohao.net.common.*;
import com.iohao.net.common.kit.*;
import com.iohao.net.external.core.message.*;
import com.iohao.net.framework.protocol.*;
import com.iohao.net.sbe.*;
import io.aeron.logbuffer.*;
import org.agrona.*;

/**
 * Aeron fragment consumer that sends user response messages back to external clients.
 *
 * @author 渔民小镇
 * @date 2025-09-02
 * @since 25.1
 */
public class UserResponseMessageOnFragment implements OnFragment {
    final UserResponseMessageDecoder decoder = new UserResponseMessageDecoder();

    @Override
    public void process(DirectBuffer buffer, int offset, int actingBlockLength, int actingVersion, Header header) {
        decoder.wrap(buffer, offset, actingBlockLength, actingVersion);

        var message = CommunicationMessageKit.createCommunicationMessage();
        SbeKit.decoderMessageCommon(message, decoder.common());
        SbeKit.decoderUserIdentity(message, decoder.userIdentity());

        message.setMsgId(decoder.msgId());
        message.setCacheCondition(decoder.cacheCondition());

        int errorCode = decoder.errorCode();
        message.setErrorCode(errorCode);
        message.setErrorMessage(decoder.errorMessage());

        var dataLength = decoder.dataLength();
        var dataBytes = ByteKit.ofBytes(dataLength);
        decoder.getData(dataBytes, 0, dataLength);
        message.setData(dataBytes);

        writeAndFlush(message);
    }

    /**
     * Resolve the target user session and flush the decoded response.
     *
     * @param message decoded user response message
     */
    protected void writeAndFlush(CommunicationMessage message) {
        ExternalWriteKit.writeAndFlush(message, ExternalServerSingle.userSessions);
    }

    @Override
    public int getTemplateId() {
        return UserResponseMessageDecoder.TEMPLATE_ID;
    }
}
