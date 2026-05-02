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
import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.channel.embedded.*;
import java.util.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests TCP pipeline frame boundary behavior.
 *
 * @author 渔民小镇
 * @date 2026-05-02
 * @since 25.4
 */
class TcpMicroBootstrapFlowTest {
    int maxFramePayloadLength;

    @BeforeEach
    void setUp() {
        this.maxFramePayloadLength = ExternalGlobalConfig.maxFramePayloadLength;
    }

    @AfterEach
    void tearDown() {
        ExternalGlobalConfig.maxFramePayloadLength = this.maxFramePayloadLength;
    }

    @Test
    void lengthFieldDecoderAllowsConfiguredMaximumPayloadLength() {
        ExternalGlobalConfig.maxFramePayloadLength = 8;
        var pipelineContext = new RecordingPipelineContext();
        new TcpMicroBootstrapFlow().pipelineCodec(pipelineContext);
        var decoder = (ChannelHandler) pipelineContext.handlers.getFirst();
        var channel = new EmbeddedChannel(decoder);
        ByteBuf frame = Unpooled.buffer(Integer.BYTES + ExternalGlobalConfig.maxFramePayloadLength);
        frame.writeInt(ExternalGlobalConfig.maxFramePayloadLength);
        frame.writeZero(ExternalGlobalConfig.maxFramePayloadLength);

        assertTrue(channel.writeInbound(frame));

        ByteBuf decoded = channel.readInbound();
        assertEquals(Integer.BYTES + ExternalGlobalConfig.maxFramePayloadLength, decoded.readableBytes());
        decoded.release();
        assertFalse(channel.finish());
    }

    private static final class RecordingPipelineContext implements PipelineContext {
        final List<Object> handlers = new ArrayList<>();

        @Override
        public void addFirst(String name, Object handler) {
            this.handlers.addFirst(handler);
        }

        @Override
        public void addLast(String name, Object handler) {
            this.handlers.add(handler);
        }

        @Override
        public void remove(String name) {
        }
    }
}
