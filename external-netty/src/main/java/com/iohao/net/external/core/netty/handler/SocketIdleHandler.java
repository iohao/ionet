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
import com.iohao.net.external.core.ExternalSetting;
import com.iohao.net.external.core.ExternalSettingAware;
import com.iohao.net.external.core.hook.IdleHandler;
import com.iohao.net.external.core.hook.IdleHook;
import com.iohao.net.framework.protocol.CmdCodeConst;
import com.iohao.net.external.core.netty.session.SocketUserSession;
import com.iohao.net.external.core.session.UserSessions;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * IdleHandler
 *
 * @author 渔民小镇
 * @date 2023-02-18
 */
@ChannelHandler.Sharable
public final class SocketIdleHandler extends ChannelInboundHandlerAdapter implements ExternalSettingAware, IdleHandler {
    /** Heartbeat event callback */
    IdleHook<IdleStateEvent> idleHook;
    /** true : Respond with a heartbeat to the client */
    boolean pong;
    UserSessions<ChannelHandlerContext, SocketUserSession> userSessions;

    @Override
    @SuppressWarnings("unchecked")
    public void setExternalSetting(ExternalSetting setting) {
        if (this.userSessions != null) {
            return;
        }

        var idleProcessSetting = setting.idleProcessSetting();
        this.idleHook = (IdleHook<IdleStateEvent>) idleProcessSetting.idleHook();
        this.pong = idleProcessSetting.pong();
        this.userSessions = (UserSessions<ChannelHandlerContext, SocketUserSession>) setting.userSessions();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        var message = (CommunicationMessage) msg;
        int cmdCode = message.getCmdCode();
        if (cmdCode != CmdCodeConst.IDLE) {
            ctx.fireChannelRead(msg);
            return;
        }

        if (this.pong) {
            if (this.idleHook != null) {
                // Processing before heartbeat response
                this.idleHook.pongBefore(message);
            }

            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {

            boolean close = true;

            var userSession = userSessions.getUserSession(ctx);

            if (this.idleHook != null) {
                close = idleHook.callback(userSession, event);
            }

            // close ctx
            if (close) {
                this.userSessions.removeUserSession(userSession);
            }

        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
