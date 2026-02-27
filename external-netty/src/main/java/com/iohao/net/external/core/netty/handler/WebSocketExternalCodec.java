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
package com.iohao.net.external.core.netty.handler;

import com.iohao.net.external.core.message.*;
import com.iohao.net.framework.protocol.*;
import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.handler.codec.*;
import io.netty.handler.codec.http.websocketx.*;
import java.util.*;

/**
 * Netty codec that converts between {@link BinaryWebSocketFrame} and {@link CommunicationMessage}.
 *
 * @author 渔民小镇
 * @date 2023-02-21
 */
public final class WebSocketExternalCodec extends MessageToMessageCodec<BinaryWebSocketFrame, CommunicationMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, CommunicationMessage message, List<Object> out) {
        // Encode outbound business messages as binary WebSocket frames.
        byte[] bytes = CommunicationMessageKit.encode(message);

        ByteBuf byteBuf = ctx.alloc().buffer(bytes.length);
        byteBuf.writeBytes(bytes);

        BinaryWebSocketFrame socketFrame = new BinaryWebSocketFrame(byteBuf);
        out.add(socketFrame);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame binary, List<Object> out) {
        // Decode inbound binary WebSocket frames into communication messages.
        ByteBuf contentBuf = binary.content();
        byte[] bytes = new byte[contentBuf.readableBytes()];
        contentBuf.readBytes(bytes);

        var message = CommunicationMessageKit.decode(bytes);
        out.add(message);
    }
}
