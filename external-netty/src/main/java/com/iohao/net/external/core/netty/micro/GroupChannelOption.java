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

import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;

/**
 * Provides OS-specific Netty event-loop groups and server channel class selection.
 *
 * @author 渔民小镇
 * @date 2023-02-18
 */
public interface GroupChannelOption {
    /**
     * Create the boss event-loop group.
     *
     * @return boss event-loop group
     */
    EventLoopGroup bossGroup();

    /**
     * Create the worker event-loop group.
     *
     * @return worker event-loop group
     */
    EventLoopGroup workerGroup();

    /**
     * Get the server channel implementation class.
     *
     * @return server channel class
     */
    Class<? extends ServerChannel> channelClass();
}
