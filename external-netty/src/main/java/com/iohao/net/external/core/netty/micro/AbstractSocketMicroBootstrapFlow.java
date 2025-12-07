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

import com.iohao.net.external.core.ExternalSetting;
import com.iohao.net.external.core.config.ExternalGlobalConfig;
import com.iohao.net.external.core.hook.internal.IdleProcessSetting;
import com.iohao.net.external.core.micro.MicroBootstrapFlow;
import com.iohao.net.external.core.micro.PipelineContext;
import com.iohao.net.external.core.netty.SettingOption;
import com.iohao.net.external.core.netty.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * SocketMicroBootstrapFlow
 *
 * @author 渔民小镇
 * @date 2023-05-31
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
abstract class AbstractSocketMicroBootstrapFlow implements MicroBootstrapFlow<ServerBootstrap> {
    ExternalSetting setting;

    @Override
    public void setExternalSetting(ExternalSetting setting) {
        this.setting = setting;
    }

    @Override
    public void channelInitializer(ServerBootstrap bootstrap) {
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                pipelineFlow(new DefaultPipelineContext(ch, setting));
            }
        });
    }

    @Override
    public void pipelineIdle(PipelineContext context) {
        IdleProcessSetting idleProcessSetting = this.setting.idleProcessSetting();

        if (idleProcessSetting == null) {
            // 如果服务器没有配置心跳相关的内容，则排除心跳数据（不做任何处理）
            // If the server is not configured with heartbeat processing, exclude heartbeat data
            context.addLast("SocketIdleExcludeHandler", SocketIdleExcludeHandler.me());
            return;
        }

        // IdleStateHandler
        context.addLast("idleStateHandler", new IdleStateHandler(
                idleProcessSetting.readerIdleTime(),
                idleProcessSetting.writerIdleTime(),
                idleProcessSetting.allIdleTime(),
                idleProcessSetting.timeUnit())
        );

        // IdleHandler
        SocketIdleHandler socketIdleHandler = setting.option(SettingOption.socketIdleHandler);
        context.addLast("idleHandler", socketIdleHandler);
    }

    @Override
    public void pipelineCustom(PipelineContext context) {
        if (ExternalGlobalConfig.enableLoggerHandler) {
            context.addLast("SimpleLoggerHandler", SimpleLoggerHandler.me());
        }

        // Check for route existence.
        context.addLast("CmdCheckHandler", CmdCheckHandler.me());

        // UserSession
        SocketUserSessionHandler socketUserSessionHandler = setting.option(SettingOption.socketUserSessionHandler);
        context.addLast("UserSessionHandler", socketUserSessionHandler);

        // Route access authentication
        SocketCmdAccessAuthHandler socketCmdAccessAuthHandler = setting.option(SettingOption.socketCmdAccessAuthHandler);
        context.addLast("CmdAccessAuthHandler", socketCmdAccessAuthHandler);

        // ExternalServer data cache
        if (ExternalGlobalConfig.externalCmdCache != null) {
            context.addLast("CmdCacheHandler", CmdCacheHandler.me());
        }

        // Forward the request to the logic server.
        var userRequestHandler = setting.option(SettingOption.userRequestHandler);
        context.addLast("UserRequestHandler", userRequestHandler);
    }
}
