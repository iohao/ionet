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

import io.netty.buffer.*;
import io.netty.channel.embedded.*;
import io.netty.handler.codec.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests TCP client frame boundary behavior.
 *
 * @author 渔民小镇
 * @date 2026-05-03
 * @since 25.4
 */
class TcpClientStartupTest {
    int packageMaxSize;

    @BeforeEach
    void setUp() {
        this.packageMaxSize = TcpClientStartup.PACKAGE_MAX_SIZE;
    }

    @AfterEach
    void tearDown() {
        TcpClientStartup.PACKAGE_MAX_SIZE = this.packageMaxSize;
    }

    @Test
    void lengthFieldDecoderAllowsConfiguredMaximumPayloadLength() {
        TcpClientStartup.PACKAGE_MAX_SIZE = 8;
        var channel = new EmbeddedChannel(TcpClientStartup.newFrameDecoder());
        ByteBuf frame = Unpooled.buffer(Integer.BYTES + TcpClientStartup.PACKAGE_MAX_SIZE);
        frame.writeInt(TcpClientStartup.PACKAGE_MAX_SIZE);
        frame.writeZero(TcpClientStartup.PACKAGE_MAX_SIZE);

        assertTrue(channel.writeInbound(frame));

        ByteBuf decoded = channel.readInbound();
        assertEquals(Integer.BYTES + TcpClientStartup.PACKAGE_MAX_SIZE, decoded.readableBytes());
        decoded.release();
        assertFalse(channel.finish());
    }

    @Test
    void lengthFieldDecoderRejectsPayloadLargerThanConfiguredMaximum() {
        TcpClientStartup.PACKAGE_MAX_SIZE = 8;
        var channel = new EmbeddedChannel(TcpClientStartup.newFrameDecoder());
        ByteBuf frame = Unpooled.buffer(Integer.BYTES + TcpClientStartup.PACKAGE_MAX_SIZE + 1);
        frame.writeInt(TcpClientStartup.PACKAGE_MAX_SIZE + 1);
        frame.writeZero(TcpClientStartup.PACKAGE_MAX_SIZE + 1);

        assertThrows(TooLongFrameException.class, () -> channel.writeInbound(frame));
        assertFalse(channel.finish());
    }
}
