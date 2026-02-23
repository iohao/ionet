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
package com.iohao.net.server;

import com.iohao.net.framework.core.SkeletonThreadPipeline;
import com.iohao.net.framework.communication.CommunicationAggregation;
import com.iohao.net.framework.communication.FutureManager;
import com.iohao.net.common.Publisher;
import com.iohao.net.server.balanced.BalancedManager;
import com.iohao.net.server.cmd.CmdRegions;
import com.iohao.net.server.connection.ConnectionManager;
import com.iohao.net.server.connection.ServerShutdownHook;
import com.iohao.net.server.listener.ServerListener;
import io.aeron.Aeron;
import lombok.Builder;

import java.util.List;

/**
 * Immutable runtime setting assembled by {@link NetServerBuilder}.
 *
 * @author 渔民小镇
 * @date 2025-09-08
 * @since 25.1
 */
@Builder(setterPrefix = "set")
public record NetServerSetting(
        int netId
        , Aeron aeron
        , CmdRegions cmdRegions
        , ConnectionManager connectionManager
        , SkeletonThreadPipeline skeletonThreadPipeline
        , CommunicationAggregation communicationAggregation
        , FindServer findServer
        , BalancedManager balancedManager
        , FutureManager futureManager
        , List<ServerShutdownHook> serverShutdownHookList
        , List<ServerListener> listenerList
        , Publisher publisher
        , ConvenientCommunication convenientCommunication
) {
}
