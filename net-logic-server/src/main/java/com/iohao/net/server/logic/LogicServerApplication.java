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
package com.iohao.net.server.logic;

import com.iohao.net.framework.core.*;
import com.iohao.net.framework.core.kit.*;
import com.iohao.net.framework.protocol.*;
import com.iohao.net.server.*;

/**
 * Bootstraps a {@link LogicServer} into a {@link Server} definition for net-server startup.
 *
 * @author 渔民小镇
 * @date 2025-09-04
 * @since 25.1
 */
public final class LogicServerApplication {
    /**
     * Builds the logic-server skeleton and server metadata, then registers startup callbacks.
     *
     * @param logicServer logic-server implementation
     * @return built server metadata
     */
    public static Server startup(LogicServer logicServer) {
        var barSkeletonBuilder = BarSkeleton.builder();
        logicServer.settingBarSkeletonBuilder(barSkeletonBuilder);
        var barSkeleton = barSkeletonBuilder.build();

        var serverBuilder = new ServerBuilder();
        logicServer.settingServerBuilder(serverBuilder);
        serverBuilder.setBarSkeleton(barSkeleton);

        Server server = serverBuilder.build();

        var actionCommandRegions = barSkeleton.actionCommandRegions;
        ActionCommandRegionGlobalCheckKit.putActionCommandRegions(server.tag(), actionCommandRegions);

        LogicServerManager.put(server.id(), logicServer);

        return server;
    }
}
