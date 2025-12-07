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

import com.iohao.net.framework.protocol.Server;
import com.iohao.net.external.core.ExternalSetting;
import com.iohao.net.external.core.ExternalSettingAware;
import com.iohao.net.external.core.netty.session.SocketUserSession;
import com.iohao.net.external.core.netty.session.SocketUserSessions;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Setter;

/**
 * UserSessionHandler
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
        // Add to session management
        SocketUserSession userSession = userSessions.add(ctx);
        userSession.setExternalServerId(id);

        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // Remove from session management
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
