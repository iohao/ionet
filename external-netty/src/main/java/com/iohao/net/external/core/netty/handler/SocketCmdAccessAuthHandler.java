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

import com.iohao.net.common.kit.*;
import com.iohao.net.external.core.*;
import com.iohao.net.external.core.config.*;
import com.iohao.net.external.core.netty.session.*;
import com.iohao.net.external.core.session.*;
import com.iohao.net.framework.core.exception.*;
import com.iohao.net.framework.protocol.*;
import io.netty.channel.*;
import lombok.*;

/**
 * Netty handler that enforces external route access control and login requirements.
 *
 * @author 渔民小镇
 * @date 2023-05-05
 */
@Setter
@ChannelHandler.Sharable
public class SocketCmdAccessAuthHandler extends ChannelInboundHandlerAdapter implements ExternalSettingAware {
    protected UserSessions<?, ?> userSessions;

    @Override
    public void setExternalSetting(ExternalSetting setting) {
        if (this.userSessions != null) {
            return;
        }

        this.userSessions = setting.userSessions();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof CommunicationMessage message) {

            if (reject(ctx, message)) {
                return;
            }

            var socketUserSessions = (SocketUserSessions) this.userSessions;
            var userSession = socketUserSessions.getUserSession(ctx);
            var loginSuccess = userSession.isVerifyIdentity();
            if (notPass(ctx, message, loginSuccess)) {
                return;
            }

            ctx.fireChannelRead(message);
        } else {
            ctx.fireChannelRead(msg);

        }
    }

    protected boolean reject(ChannelHandlerContext ctx, CommunicationMessage message) {
        var accessAuthenticationHook = ExternalGlobalConfig.accessAuthenticationHook;
        int cmdMerge = message.getCmdMerge();
        var reject = accessAuthenticationHook.reject(cmdMerge);

        if (reject) {
            message.setError(ActionErrorEnum.cmdInfoErrorCode);
            message.setData(CommonConst.emptyBytes);

            ctx.writeAndFlush(message);
            return true;
        }

        return false;
    }

    protected boolean notPass(ChannelHandlerContext ctx, CommunicationMessage message, boolean loginSuccess) {
        var accessAuthenticationHook = ExternalGlobalConfig.accessAuthenticationHook;
        int cmdMerge = message.getCmdMerge();
        boolean pass = accessAuthenticationHook.pass(loginSuccess, cmdMerge);
        if (pass) {
            return false;
        }

        // Inform the player if access validation is unsuccessful.
        message.setError(ActionErrorEnum.verifyIdentity);
        message.setData(CommonConst.emptyBytes);

        ctx.writeAndFlush(message);
        return true;
    }
}
