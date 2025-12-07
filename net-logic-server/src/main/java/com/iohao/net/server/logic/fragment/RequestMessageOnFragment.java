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

import com.iohao.net.framework.communication.CommunicationType;
import com.iohao.net.framework.protocol.RequestMessage;
import com.iohao.net.common.kit.ByteKit;
import com.iohao.net.common.SbeKit;
import com.iohao.net.sbe.RequestMessageDecoder;
import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;

/**
 * Internal RequestMessageOnFragment
 *
 * @author 渔民小镇
 * @date 2025-08-28
 * @since 25.1
 */
@SuppressWarnings("all")
public class RequestMessageOnFragment extends AbstractRequestOnFragment {
    protected final RequestMessageDecoder decoder = new RequestMessageDecoder();

    @Override
    public void process(DirectBuffer buffer, int offset, int actingBlockLength, int actingVersion, Header header) {
        decoder.wrap(buffer, offset, actingBlockLength, actingVersion);

        var message = new RequestMessage();
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

        commonProcess(message, CommunicationType.INTERNAL_CALL);
    }

    protected void extracted(RequestMessage message) {
        decoder.bindingLogicServerIds();
    }

    @Override
    public int getTemplateId() {
        return RequestMessageDecoder.TEMPLATE_ID;
    }
}
