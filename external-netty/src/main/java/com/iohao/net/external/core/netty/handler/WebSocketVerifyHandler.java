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

import com.iohao.net.external.core.ExternalSetting;
import com.iohao.net.external.core.ExternalSettingAware;
import com.iohao.net.external.core.netty.session.SocketUserSession;
import com.iohao.net.external.core.netty.session.SocketUserSessions;
import com.iohao.net.server.ConvenientCommunication;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handler for WebSocket token authentication before connection
 *
 * @author 渔民小镇
 * @date 2023-08-03
 */
@ChannelHandler.Sharable
public class WebSocketVerifyHandler extends ChannelInboundHandlerAdapter implements ExternalSettingAware {

    protected SocketUserSessions userSessions;
    protected ConvenientCommunication convenientCommunication;

    @Override
    public void setExternalSetting(ExternalSetting setting) {
        this.userSessions = (SocketUserSessions) setting.userSessions();
        this.convenientCommunication = setting.convenientCommunication();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest request) {
            String uri = request.uri();
            var params = getParams(uri);

            // Developers can override the verify method for extension.
            var userSession = userSessions.getUserSession(ctx);
            boolean verify = verify(userSession, params);

            if (verify) {
                ctx.pipeline().remove(this);
            } else {
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED);
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                return;
            }
        }

        super.channelRead(ctx, msg);
    }

    /**
     * verify
     *
     * @param userSession ctx
     * @param params      params
     * @return Returning false indicates a validation failure, and the framework will close the connection.
     */
    protected boolean verify(SocketUserSession userSession, Map<String, String> params) {
        return true;
    }

    protected Map<String, String> getParams(String uri) {
        return new QueryStringDecoder(uri)
                .parameters()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getFirst()));
    }
}