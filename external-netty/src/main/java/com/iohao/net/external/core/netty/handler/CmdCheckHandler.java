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

import com.iohao.net.framework.core.exception.ActionErrorEnum;
import com.iohao.net.framework.protocol.CommunicationMessage;
import com.iohao.net.server.NetServerSetting;
import com.iohao.net.server.NetServerSettingAware;
import com.iohao.net.server.cmd.CmdRegions;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Check for route existence.
 * It can block the request when a route doesn't exist, preventing it from having to go through other servers.
 *
 * @author 渔民小镇
 * @date 2023-05-01
 */
@ChannelHandler.Sharable
public final class CmdCheckHandler extends ChannelInboundHandlerAdapter implements NetServerSettingAware {

    CmdRegions cmdRegions;

    @Override
    public void setNetServerSetting(NetServerSetting setting) {
        if (this.cmdRegions != null) {
            return;
        }

        cmdRegions = setting.cmdRegions();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof CommunicationMessage message) {
            int cmdMerge = message.getCmdMerge();
            if (cmdRegions.existCmdMerge(cmdMerge)) {
                ctx.fireChannelRead(message);
            } else {
                message.setError(ActionErrorEnum.cmdInfoErrorCode);
                ctx.writeAndFlush(message);
            }

        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private CmdCheckHandler() {
    }

    public static CmdCheckHandler me() {
        return Holder.ME;
    }


    private static class Holder {
        static final CmdCheckHandler ME = new CmdCheckHandler();
    }
}
