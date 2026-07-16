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
import io.netty.channel.embedded.*;
import io.netty.handler.codec.http.websocketx.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests WebSocket client message codec behavior.
 *
 * @author 渔民小镇
 * @date 2026-07-16
 * @since 25.6
 */
class ClientWebSocketExternalCodecTest {
    @Test
    void encodesExternalMessageAsBinaryFrame() {
        ExternalMessage expected = newMessage();
        var channel = new EmbeddedChannel(new ClientWebSocketExternalCodec());

        assertTrue(channel.writeOutbound(expected));

        BinaryWebSocketFrame frame = channel.readOutbound();
        byte[] encoded = ByteBufUtil.getBytes(frame.content());
        ExternalMessage actual = DataCodecManager.decode(encoded, ExternalMessage.class);
        assertMessageEquals(expected, actual);

        frame.release();
        channel.finishAndReleaseAll();
    }

    @Test
    void decodesBinaryFrameAsExternalMessage() {
        ExternalMessage expected = newMessage();
        byte[] encoded = DataCodecManager.encode(expected);
        var frame = new BinaryWebSocketFrame(Unpooled.wrappedBuffer(encoded));
        var channel = new EmbeddedChannel(new ClientWebSocketExternalCodec());

        assertTrue(channel.writeInbound(frame));

        ExternalMessage actual = channel.readInbound();
        assertMessageEquals(expected, actual);
        channel.finishAndReleaseAll();
    }

    private static ExternalMessage newMessage() {
        var message = new ExternalMessage();
        message.setCmdCode(1);
        message.setProtocolSwitch(2);
        message.setCmdMerge(3);
        message.setErrorCode(4);
        message.setErrorMessage("error");
        message.setData(new byte[]{5, 6, 7});
        message.setMsgId(8);
        return message;
    }

    private static void assertMessageEquals(ExternalMessage expected, ExternalMessage actual) {
        assertEquals(expected.getCmdCode(), actual.getCmdCode());
        assertEquals(expected.getProtocolSwitch(), actual.getProtocolSwitch());
        assertEquals(expected.getCmdMerge(), actual.getCmdMerge());
        assertEquals(expected.getErrorCode(), actual.getErrorCode());
        assertEquals(expected.getErrorMessage(), actual.getErrorMessage());
        assertArrayEquals(expected.getData(), actual.getData());
        assertEquals(expected.getMsgId(), actual.getMsgId());
    }
}
