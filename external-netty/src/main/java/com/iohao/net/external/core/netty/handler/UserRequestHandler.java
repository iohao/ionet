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

import com.iohao.net.common.*;
import com.iohao.net.external.core.*;
import com.iohao.net.external.core.config.*;
import com.iohao.net.external.core.netty.session.*;
import com.iohao.net.framework.*;
import com.iohao.net.framework.protocol.*;
import com.iohao.net.server.*;
import io.netty.channel.*;

/**
 * Forwards validated user requests to the selected logic server.
 *
 * @author 渔民小镇
 * @date 2023-02-19
 */
@ChannelHandler.Sharable
public final class UserRequestHandler extends SimpleChannelInboundHandler<CommunicationMessage>
        implements ExternalSettingAware {

    SocketUserSessions userSessions;
    Publisher publisher;
    FindServer findServer;

    @Override
    public void setExternalSetting(ExternalSetting setting) {
        if (this.userSessions != null) {
            return;
        }

        this.userSessions = (SocketUserSessions) setting.userSessions();
        var netServerSetting = setting.netServerSetting();
        this.publisher = netServerSetting.publisher();
        this.findServer = netServerSetting.findServer();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CommunicationMessage message) {
        var userSession = this.userSessions.getUserSession(ctx);
        userSession.employ(message);

        var netId = CoreGlobalConfig.getNetId();
        message.setNetId(netId);

        var server = this.findServer.getServer(message);
        if (server == null) {
            userSession.writeAndFlush(message);
            return;
        }

        ExternalGlobalConfig.userRequestEnhance.enhance(message);
        this.publisher.publishMessage(server.pubName(), message);
    }
}
