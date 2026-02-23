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

import com.iohao.net.framework.toy.IonetBanner;
import com.iohao.net.common.kit.IonetLogName;
import com.iohao.net.external.core.micro.MicroBootstrap;
import com.iohao.net.external.core.micro.MicroBootstrapFlow;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Server for connecting with real players, handling netty servers for tcp and websocket.
 *
 * @author 渔民小镇
 * @date 2023-05-27
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j(topic = IonetLogName.ExternalTopic)
public final class SocketMicroBootstrap implements MicroBootstrap {
    /**
     * Cast the transport-agnostic flow to the concrete bootstrap type expected by this implementation.
     *
     * @param microBootstrapFlow generic bootstrap flow
     * @return typed bootstrap flow
     */
    @SuppressWarnings("unchecked")
    private <T> MicroBootstrapFlow<T> map(MicroBootstrapFlow<?> microBootstrapFlow) {
        return (MicroBootstrapFlow<T>) microBootstrapFlow;
    }

    @Override
    public void startup(int port, MicroBootstrapFlow<?> microBootstrapFlow) {

        var groupChannelOption = GroupChannelKit.groupChannelOption;
        var bossGroup = groupChannelOption.bossGroup();
        var workerGroup = groupChannelOption.workerGroup();
        var channelClass = groupChannelOption.channelClass();
        var bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(channelClass);

        // Developers can selectively override the process methods to customize the business logic for their own projects.
        var flow = map(microBootstrapFlow);
        flow.option(bootstrap);
        flow.channelInitializer(bootstrap);

        // Port for real players to connect.
        ChannelFuture channelFuture = bootstrap.bind(port);

        try {
            IonetBanner.render();
            channelFuture.channel().closeFuture().syncUninterruptibly();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            bossGroup.shutdownGracefully(0, 5, TimeUnit.SECONDS);
            workerGroup.shutdownGracefully(0, 5, TimeUnit.SECONDS);
        }
    }
}
