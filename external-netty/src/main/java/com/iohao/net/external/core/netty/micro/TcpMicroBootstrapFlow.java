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

import com.iohao.net.external.core.config.ExternalGlobalConfig;
import com.iohao.net.external.core.micro.PipelineContext;
import com.iohao.net.external.core.netty.handler.TcpExternalCodec;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Netty bootstrap flow for TCP-based external client connections.
 *
 * @author 渔民小镇
 * @date 2023-05-28
 */
public class TcpMicroBootstrapFlow extends AbstractSocketMicroBootstrapFlow {

    @Override
    public void option(ServerBootstrap bootstrap) {
        bootstrap
                // Keep child connections alive.
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                /*
                 * Enables TCP keepalive on the listening socket.
                 * The OS-level keepalive probe is only triggered after long periods of inactivity.
                 */
                .option(ChannelOption.SO_KEEPALIVE, true)
                /*
                 * Maximum queued completed connections waiting for accept when the server is busy.
                 * Defaults to 100 here for predictable behavior across environments.
                 */
                .option(ChannelOption.SO_BACKLOG, 100)
                /*
                 * Disable Nagle's algorithm so gameplay messages are flushed with lower latency.
                 */
                .option(ChannelOption.TCP_NODELAY, true);
    }

    @Override
    public void pipelineCodec(PipelineContext context) {
//        context.addLast("tcp-check", new TcpProtocolSanityCheckHandler());

        // Frame length = length field value + offset + field length + adjustment.
        context.addLast(new LengthFieldBasedFrameDecoder(
                ExternalGlobalConfig.maxFramePayloadLength,
                // Length field offset (starts at 0).
                0,
                // Length field size: 4 bytes because TcpExternalCodec writes an int length prefix.
                4,
                // No length adjustment is needed because the field stores the payload length directly.
                0,
                // Do not strip initial bytes; TcpExternalCodec expects to read the length prefix.
                0));

        context.addLast("codec", new TcpExternalCodec());
    }
}
