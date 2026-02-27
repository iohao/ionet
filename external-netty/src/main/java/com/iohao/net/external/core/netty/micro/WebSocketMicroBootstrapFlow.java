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
package com.iohao.net.external.core.netty.micro;

import com.iohao.net.external.core.config.*;
import com.iohao.net.external.core.micro.*;
import com.iohao.net.external.core.netty.handler.*;
import io.netty.bootstrap.*;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;

/**
 * Netty bootstrap flow for WebSocket-based external client connections.
 *
 * @author 渔民小镇
 * @date 2023-05-31
 */
public class WebSocketMicroBootstrapFlow extends AbstractSocketMicroBootstrapFlow {
    public WebSocketVerifyHandler verifyHandler;

    @Override
    public void option(ServerBootstrap bootstrap) {
        bootstrap
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_BACKLOG, 100)
                .childOption(ChannelOption.SO_REUSEADDR, true);
    }

    @Override
    public void pipelineCodec(PipelineContext context) {
        // Add http related handlers
        this.httpHandler(context);

        // Verification handler before establishing connection
        if (verifyHandler != null) {
            context.addLast("WebSocketVerifyHandler", verifyHandler);
        }

        // Add WebSocket related handlers
        this.websocketHandler(context);

        // WebSocket Codec
        context.addLast("codec", new WebSocketExternalCodec());
    }

    /**
     * Install the WebSocket protocol handler.
     *
     * @param context Netty pipeline adapter
     */
    public void websocketHandler(PipelineContext context) {
        var config = WebSocketServerProtocolConfig.newBuilder()
                .websocketPath(ExternalGlobalConfig.websocketPath)
                .maxFramePayloadLength(ExternalGlobalConfig.maxFramePayloadLength)
                .checkStartsWith(true)
                .allowExtensions(true)
                .build();

        context.addLast("WebSocketServerProtocolHandler", new WebSocketServerProtocolHandler(config));
    }

    /**
     * Install HTTP handlers required before the WebSocket protocol upgrade.
     *
     * @param context Netty pipeline adapter
     */
    public void httpHandler(PipelineContext context) {
        context.addLast("http-codec", new HttpServerCodec());
        context.addLast("aggregator", new HttpObjectAggregator(65536));
        context.addLast("http-fallback", HttpFallbackHandler.me());
    }
}
