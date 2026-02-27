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
package com.iohao.net.server.logic.fragment;

import com.iohao.net.common.*;
import com.iohao.net.common.kit.*;
import com.iohao.net.framework.communication.*;
import com.iohao.net.framework.protocol.*;
import com.iohao.net.sbe.*;
import io.aeron.logbuffer.*;
import org.agrona.*;

/**
 * Internal SendMessageOnFragment
 *
 * @author 渔民小镇
 * @date 2025-09-14
 * @since 25.1
 */
@SuppressWarnings("all")
public class SendMessageOnFragment extends AbstractRequestOnFragment {
    protected final SendMessageDecoder decoder = new SendMessageDecoder();

    @Override
    public void process(DirectBuffer buffer, int offset, int actingBlockLength, int actingVersion, Header header) {
        decoder.wrap(buffer, offset, actingBlockLength, actingVersion);

        var message = new SendMessage();
        SbeKit.decoderMessageCommon(message, decoder.common());
        SbeKit.decoderUserIdentity(message, decoder.userIdentity());
        message.setHopCount(decoder.hopCount());

        extracted(message);

        var dataLength = decoder.dataLength();
        var dataBytes = ByteKit.ofBytes(dataLength);
        decoder.getData(dataBytes, 0, dataLength);
        message.setData(dataBytes);

        var attachmentLength = decoder.attachmentLength();
        var attachmentBytes = ByteKit.ofBytes(attachmentLength);
        decoder.getAttachment(attachmentBytes, 0, attachmentLength);
        message.setAttachment(attachmentBytes);

        commonProcess(message, CommunicationType.INTERNAL_SEND);
    }

    protected void extracted(SendMessage message) {
        decoder.bindingLogicServerIds();
    }

    @Override
    public int getTemplateId() {
        return SendMessageDecoder.TEMPLATE_ID;
    }
}