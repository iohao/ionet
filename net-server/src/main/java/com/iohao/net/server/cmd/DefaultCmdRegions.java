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
package com.iohao.net.server.cmd;


import com.iohao.net.common.kit.CollKit;
import com.iohao.net.common.kit.MoreKit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Default command-region registry that maps command merges to candidate logic servers.
 *
 * @author 渔民小镇
 * @date 2023-04-30
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class DefaultCmdRegions implements CmdRegions {
    /** key: cmdMerge */
    final Map<Integer, CmdRegion> cmdRegionMap = CollKit.ofConcurrentHashMap();

    /** key: logicServerId */
    final Map<Integer, Set<CmdRegion>> serverCmdRegionMap = CollKit.ofConcurrentHashMap();

    @Override
    public void loading(int serverId, int[] cmdMerges) {
        for (var cmdMerge : cmdMerges) {
            var cmdRegion = this.getCmdRegion(cmdMerge);
            cmdRegion.addServerId(serverId);

            var cmdRegionSet = this.getCmdRegionSet(serverId);
            cmdRegionSet.add(cmdRegion);
        }
    }


    @Override
    public void unLoading(int serverId) {
        this.getCmdRegionSet(serverId).forEach(cmdRegion -> cmdRegion.removeByServerId(serverId));
    }

    @Override
    public boolean existCmdMerge(int cmdMerge) {
        var cmdRegion = this.cmdRegionMap.get(cmdMerge);
        return Objects.nonNull(cmdRegion) && cmdRegion.hasServerId();
    }

    @Override
    public int endpointLogicServerId(int cmdMerge, int[] logicServerIds) {
        var cmdRegion = this.cmdRegionMap.get(cmdMerge);
        if (cmdRegion == null) {
            return 0;
        }

        return cmdRegion.endpointLogicServerId(logicServerIds);
    }

    @Override
    public int getServerIdByCmdMerge(int cmdMerge) {
        var cmdRegion = this.cmdRegionMap.get(cmdMerge);
        if (cmdRegion == null) {
            return 0;
        }

        return cmdRegion.getAnyServerId();
    }

    private CmdRegion getCmdRegion(int cmdMerge) {
        var cmdRegion = this.cmdRegionMap.get(cmdMerge);

        if (Objects.isNull(cmdRegion)) {
            CmdRegion newValue = new DefaultCmdRegion(cmdMerge);
            return MoreKit.putIfAbsent(cmdRegionMap, cmdMerge, newValue);
        }

        return cmdRegion;
    }

    private Set<CmdRegion> getCmdRegionSet(int serverId) {
        var cmdRegionSet = this.serverCmdRegionMap.get(serverId);
        if (Objects.isNull(cmdRegionSet)) {
            return MoreKit.putIfAbsent(this.serverCmdRegionMap, serverId, CollKit.ofConcurrentSet());
        }

        return cmdRegionSet;
    }
}
