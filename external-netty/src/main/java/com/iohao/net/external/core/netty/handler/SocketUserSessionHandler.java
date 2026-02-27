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

import com.iohao.net.external.core.*;
import com.iohao.net.external.core.netty.session.*;
import com.iohao.net.framework.protocol.*;
import io.netty.channel.*;
import lombok.*;

/**
 * Creates and removes {@link SocketUserSession} instances on Netty channel lifecycle events.
 *
 * @author 渔民小镇
 * @date 2023-02-19
 */
@Setter
@ChannelHandler.Sharable
public final class SocketUserSessionHandler extends ChannelInboundHandlerAdapter implements ExternalSettingAware {

    Server server;
    SocketUserSessions userSessions;

    @Override
    public void setExternalSetting(ExternalSetting setting) {
        if (this.userSessions != null) {
            return;
        }

        this.server = setting.server();
        this.userSessions = (SocketUserSessions) setting.userSessions();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int id = server.id();
        // Register the new channel immediately so downstream handlers can resolve the session.
        SocketUserSession userSession = userSessions.add(ctx);
        userSession.setExternalServerId(id);

        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // Clean up session state when the channel closes normally.
        var userSession = this.userSessions.getUserSession(ctx);
        this.userSessions.removeUserSession(userSession);

        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        var userSession = this.userSessions.getUserSession(ctx);
        this.userSessions.removeUserSession(userSession);

        super.exceptionCaught(ctx, cause);
    }
}
