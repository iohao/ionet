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
package com.iohao.net.extension.client.join;

import com.iohao.net.external.core.message.*;
import com.iohao.net.framework.core.codec.*;
import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.handler.codec.*;
import io.netty.handler.codec.http.websocketx.*;
import java.util.*;

/**
 * Client codec that converts between {@link BinaryWebSocketFrame} and {@link ExternalMessage}.
 *
 * <p>Mirrors the server-side {@code WebSocketExternalCodec}: raw {@link DataCodecManager} bytes are
 * carried directly inside a binary WebSocket frame, without any length prefix, since WebSocket
 * framing already preserves message boundaries.
 *
 * @author 渔民小镇
 * @date 2026-07-16
 */
public class ClientWebSocketExternalCodec extends MessageToMessageCodec<BinaryWebSocketFrame, ExternalMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ExternalMessage message, List<Object> out) {
        var codec = DataCodecManager.getDataCodec();
        byte[] bytes = codec.encode(message);

        ByteBuf byteBuf = ctx.alloc().buffer(bytes.length);
        byteBuf.writeBytes(bytes);

        out.add(new BinaryWebSocketFrame(byteBuf));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame binary, List<Object> out) {
        ByteBuf contentBuf = binary.content();
        byte[] msgBytes = new byte[contentBuf.readableBytes()];
        contentBuf.readBytes(msgBytes);

        ExternalMessage message = DataCodecManager.decode(msgBytes, ExternalMessage.class);
        out.add(message);
    }
}
