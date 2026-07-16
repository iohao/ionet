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

import com.iohao.net.extension.client.*;
import com.iohao.net.extension.client.kit.*;
import com.iohao.net.extension.client.user.*;
import io.netty.channel.*;
import io.netty.channel.embedded.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.ssl.*;
import java.net.*;
import java.util.concurrent.atomic.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests WebSocket client startup configuration and lifecycle.
 *
 * @author 渔民小镇
 * @date 2026-07-16
 * @since 25.6
 */
class WebSocketClientStartupTest {
    boolean closeScanner;

    @BeforeEach
    void setUp() {
        this.closeScanner = ClientUserConfigs.closeScanner;
        ClientUserConfigs.closeScanner = true;
    }

    @AfterEach
    void tearDown() {
        ClientUserConfigs.closeScanner = this.closeScanner;
        ClientConnects.shutdownGracefully();
    }

    @Test
    void resolvesWebSocketUrisAndPorts() {
        URI ws = WebSocketClientStartup.parseUri("ws://localhost/websocket");
        URI wss = WebSocketClientStartup.parseUri("wss://localhost/websocket");
        URI explicit = WebSocketClientStartup.parseUri("wss://localhost:9443/websocket");

        assertEquals(80, WebSocketClientStartup.resolvePort(ws));
        assertEquals(443, WebSocketClientStartup.resolvePort(wss));
        assertEquals(9443, WebSocketClientStartup.resolvePort(explicit));
    }

    @Test
    void rejectsInvalidWebSocketUris() {
        assertThrows(IllegalArgumentException.class,
                () -> WebSocketClientStartup.parseUri("http://localhost/websocket"));
        assertThrows(IllegalArgumentException.class,
                () -> WebSocketClientStartup.parseUri("ws:///websocket"));
    }

    @Test
    void configuresTlsBeforeHttpWithHostnameVerification() {
        ClientConnectOption option = newOption("wss://localhost/websocket");
        URI uri = WebSocketClientStartup.parseUri(option.getWsUrl());
        var channel = new EmbeddedChannel();

        WebSocketClientStartup.configurePipeline(
                channel,
                uri,
                WebSocketClientStartup.resolvePort(uri),
                option,
                new ClientMessageHandler(option.getClientUser()));

        assertTrue(channel.pipeline().names().indexOf("ssl") < channel.pipeline().names().indexOf("httpCodec"));
        SslHandler sslHandler = channel.pipeline().get(SslHandler.class);
        assertNotNull(sslHandler);
        assertEquals("HTTPS", sslHandler.engine().getSSLParameters().getEndpointIdentificationAlgorithm());
        channel.finishAndReleaseAll();
    }

    @Test
    void bindsClientChannelOnlyOnceAfterHandshake() {
        ClientConnectOption option = newOption("ws://localhost/websocket");
        var callbackCount = new AtomicInteger();
        option.setConnectedCallback(callbackCount::incrementAndGet);
        var channel = new EmbeddedChannel(new WebSocketClientStartup.WebSocketHandshakeHandler(option));

        channel.pipeline().fireUserEventTriggered(
                WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE);
        channel.pipeline().fireUserEventTriggered(
                WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE);

        ClientUserChannel clientUserChannel = option.getClientUser().getClientUserChannel();
        assertEquals(1, callbackCount.get());
        assertNotNull(clientUserChannel.getChannelAccept());
        assertNotNull(clientUserChannel.getCloseChannel());

        channel.finishAndReleaseAll();
        assertFalse(option.getClientUser().isActive());
    }

    @Test
    void closesClientAfterPipelineException() {
        ClientConnectOption option = newOption("ws://localhost/websocket");
        var channel = new EmbeddedChannel(new WebSocketClientStartup.WebSocketHandshakeHandler(option));

        channel.pipeline().fireExceptionCaught(new IllegalStateException("test"));

        assertFalse(option.getClientUser().isActive());
        assertFalse(channel.isOpen());
        channel.finishAndReleaseAll();
    }

    @Test
    void sharesShutsDownAndRecreatesEventLoopGroup() {
        EventLoopGroup first = ClientConnects.eventLoopGroup();

        assertSame(first, ClientConnects.eventLoopGroup());

        ClientConnects.shutdownGracefully();
        assertTrue(first.isTerminated());

        EventLoopGroup recreated = ClientConnects.eventLoopGroup();
        assertNotSame(first, recreated);
    }

    private static ClientConnectOption newOption(String wsUrl) {
        var option = new ClientConnectOption();
        option.setWsUrl(wsUrl);
        option.setClientUser(new DefaultClientUser());
        return option;
    }
}
