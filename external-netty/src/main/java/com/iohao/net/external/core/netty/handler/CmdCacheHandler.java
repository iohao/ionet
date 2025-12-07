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

import com.iohao.net.framework.protocol.CommunicationMessage;
import com.iohao.net.external.core.config.ExternalGlobalConfig;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Objects;

/**
 * CmdCacheHandler, externalServer data cache
 *
 * @author 渔民小镇
 * @date 2023-07-02
 */
@ChannelHandler.Sharable
public final class CmdCacheHandler extends SimpleChannelInboundHandler<CommunicationMessage> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (Objects.isNull(ExternalGlobalConfig.externalCmdCache)) {
            // remove self
            ctx.pipeline().remove(this);
        }

        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CommunicationMessage message) {
        var cache = ExternalGlobalConfig.externalCmdCache.getCache(message);
        if (cache != null) {
            ctx.writeAndFlush(cache);
            return;
        }

        ctx.fireChannelRead(message);
    }

    public CmdCacheHandler() {
    }

    public static CmdCacheHandler me() {
        return Holder.ME;
    }

    private static class Holder {
        static final CmdCacheHandler ME = new CmdCacheHandler();
    }
}
