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

import com.iohao.net.common.kit.*;
import com.iohao.net.extension.client.*;
import com.iohao.net.extension.client.user.*;
import com.iohao.net.external.core.config.*;
import io.netty.bootstrap.*;
import io.netty.channel.*;
import io.netty.channel.socket.*;
import io.netty.channel.socket.nio.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.ssl.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.*;
import lombok.extern.slf4j.*;

/**
 * WebSocket client connector startup implementation, backed by Netty.
 *
 * @author 渔民小镇
 * @date 2023-07-04
 */
@Slf4j(topic = IonetLogName.CommonStdout)
class WebSocketClientStartup implements ClientConnect {
    static final int HTTP_AGGREGATOR_MAX_CONTENT_LENGTH = 65536;

    @Override
    public void connect(ClientConnectOption option) {
        ClientUser clientUser = option.getClientUser();
        ClientMessageHandler clientMessageHandler = new ClientMessageHandler(clientUser);

        URI uri = parseUri(option.getWsUrl());
        String host = uri.getHost();
        int port = resolvePort(uri);

        var bootstrap = new Bootstrap();
        bootstrap.group(ClientConnects.eventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        configurePipeline(ch, uri, port, option, clientMessageHandler);
                    }
                });

        try {
            Channel channel = bootstrap.connect(host, port).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("WebSocket client connection interrupted: {}", uri, e);
        } catch (Exception e) {
            log.error("WebSocket client connection failed: {}", uri, e);
        }
    }

    static URI parseUri(String wsUrl) {
        URI uri = URI.create(wsUrl);
        String scheme = uri.getScheme();

        if (!("ws".equalsIgnoreCase(scheme) || "wss".equalsIgnoreCase(scheme))) {
            throw new IllegalArgumentException("WebSocket URI scheme must be ws or wss: " + wsUrl);
        }

        if (uri.getHost() == null) {
            throw new IllegalArgumentException("WebSocket URI must include a host: " + wsUrl);
        }

        return uri;
    }

    static int resolvePort(URI uri) {
        if (uri.getPort() != -1) {
            return uri.getPort();
        }

        return isSecure(uri) ? 443 : 80;
    }

    static void configurePipeline(
            Channel channel,
            URI uri,
            int port,
            ClientConnectOption option,
            ClientMessageHandler clientMessageHandler
    ) {
        ChannelPipeline pipeline = channel.pipeline();

        if (isSecure(uri)) {
            pipeline.addLast("ssl", newSslHandler(channel, uri.getHost(), port));
        }

        int maxFramePayloadLength = ExternalGlobalConfig.maxFramePayloadLength;
        var handshaker = WebSocketClientHandshakerFactory.newHandshaker(
                uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders(), maxFramePayloadLength);

        pipeline.addLast("httpCodec", new HttpClientCodec());
        pipeline.addLast("httpAggregator", new HttpObjectAggregator(HTTP_AGGREGATOR_MAX_CONTENT_LENGTH));
        pipeline.addLast("webSocketProtocol", new WebSocketClientProtocolHandler(handshaker));
        pipeline.addLast("codec", new ClientWebSocketExternalCodec());
        pipeline.addLast("webSocketHandshake", new WebSocketHandshakeHandler(option));
        pipeline.addLast("clientMessage", clientMessageHandler);
    }

    private static boolean isSecure(URI uri) {
        return "wss".equalsIgnoreCase(uri.getScheme());
    }

    private static SslHandler newSslHandler(Channel channel, String host, int port) {
        SslHandler sslHandler = SslContextHolder.CONTEXT.newHandler(channel.alloc(), host, port);
        SSLParameters sslParameters = sslHandler.engine().getSSLParameters();
        sslParameters.setEndpointIdentificationAlgorithm("HTTPS");
        sslHandler.engine().setSSLParameters(sslParameters);
        return sslHandler;
    }

    private static final class SslContextHolder {
        static final SslContext CONTEXT = createContext();

        private static SslContext createContext() {
            try {
                return SslContextBuilder.forClient().build();
            } catch (SSLException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    /**
     * Completes client wiring after the WebSocket handshake succeeds. Sending business messages
     * before the handshake finishes would be dropped, so callback and channel wiring are deferred
     * until {@link WebSocketClientProtocolHandler.ClientHandshakeStateEvent#HANDSHAKE_COMPLETE}.
     *
     * @author 渔民小镇
     * @date 2026-07-16
     */
    static final class WebSocketHandshakeHandler extends ChannelInboundHandlerAdapter {
        final ClientConnectOption option;
        boolean handshakeComplete;

        WebSocketHandshakeHandler(ClientConnectOption option) {
            this.option = option;
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
            if (!this.handshakeComplete
                    && evt == WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE) {
                this.handshakeComplete = true;
                Channel channel = ctx.channel();
                ClientUser clientUser = this.option.getClientUser();
                ClientUserChannel clientUserChannel = clientUser.getClientUserChannel();

                clientUserChannel.setChannelAccept(channel::writeAndFlush);
                clientUserChannel.setCloseChannel(channel::close);

                Optional.ofNullable(this.option.getConnectedCallback()).ifPresent(Runnable::run);

                clientUser.getClientUserInputCommands().start();
            }

            ctx.fireUserEventTriggered(evt);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            this.option.getClientUser().getClientUserChannel().closeChannel();
            log.info("WebSocket client disconnected: {}", this.option.getWsUrl());
            ctx.fireChannelInactive();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            this.option.getClientUser().getClientUserChannel().closeChannel();
            log.error("WebSocket client error: {}", this.option.getWsUrl(), cause);
            ctx.close();
        }
    }
}
