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
package com.iohao.net.framework.core;

import com.iohao.net.framework.CoreGlobalConfig;
import com.iohao.net.framework.core.kit.CmdKit;
import com.iohao.net.common.kit.CollKit;
import com.iohao.net.common.kit.MoreKit;

import java.util.Map;

/**
 * Internal flyweight interface and its implementations for caching {@link CmdInfo} instances.
 * <p>
 * Three strategies are provided:
 * <ul>
 *   <li>{@code MapCmdInfoFlyweight} -- lazy, concurrent-map-backed cache suitable for large/sparse command spaces.</li>
 *   <li>{@code TwoArrayCmdInfoFlyweight} -- eagerly pre-allocated 2-D array indexed by {@code [cmd][subCmd]}.</li>
 *   <li>{@code SpaceForTimeCmdInfoFlyweight} -- eagerly pre-allocated flat array indexed by the merged command value.</li>
 * </ul>
 */

interface CmdInfoFlyweight {
    /**
     * Obtain a cached {@link CmdInfo} for the given command pair.
     *
     * @param cmd    the primary command ID
     * @param subCmd the sub-command ID
     * @return the cached {@link CmdInfo}
     */
    CmdInfo of(int cmd, int subCmd);

    /**
     * Obtain a cached {@link CmdInfo} for the given merged command value.
     *
     * @param cmdMerge the merged command value
     * @return the cached {@link CmdInfo}
     */
    CmdInfo of(int cmdMerge);
}

/** Map-backed flyweight that lazily creates and caches {@link CmdInfo} instances. */
final class MapCmdInfoFlyweight implements CmdInfoFlyweight {
    final Map<Integer, CmdInfo> cmdInfoMap = CollKit.ofConcurrentHashMap();

    @Override
    public CmdInfo of(int cmd, int subCmd) {
        return of(CmdKit.merge(cmd, subCmd));
    }

    @Override
    public CmdInfo of(int cmdMerge) {
        var cmdInfo = cmdInfoMap.get(cmdMerge);

        if (cmdInfo == null) {
            var cmd = CmdKit.getCmd(cmdMerge);
            var subCmd = CmdKit.getSubCmd(cmdMerge);
            return MoreKit.putIfAbsent(cmdInfoMap, cmdMerge, new CmdInfo(cmd, subCmd, cmdMerge));
        }

        return cmdInfo;
    }
}

/** Two-dimensional array flyweight that eagerly pre-allocates all {@link CmdInfo} instances indexed by {@code [cmd][subCmd]}. */
final class TwoArrayCmdInfoFlyweight implements CmdInfoFlyweight {
    final CmdInfo[][] cmdInfoArray = new CmdInfo[CoreGlobalConfig.setting.cmdMaxLen][CoreGlobalConfig.setting.subCmdMaxLen];

    TwoArrayCmdInfoFlyweight() {
        var cmdMax = CoreGlobalConfig.setting.cmdMaxLen;
        var subCmdMax = CoreGlobalConfig.setting.subCmdMaxLen;

        for (int cmd = 0; cmd < cmdMax; cmd++) {
            for (int subCmd = 0; subCmd < subCmdMax; subCmd++) {
                cmdInfoArray[cmd][subCmd] = new CmdInfo(cmd, subCmd, CmdKit.merge(cmd, subCmd));
            }
        }
    }

    @Override
    public CmdInfo of(int cmd, int subCmd) {
        return cmdInfoArray[cmd][subCmd];
    }

    @Override
    public CmdInfo of(int cmdMerge) {
        return of(CmdKit.getCmd(cmdMerge), CmdKit.getSubCmd(cmdMerge));
    }
}

/** Flat-array flyweight that trades memory for O(1) lookup time using the merged command value as the array index. */
final class SpaceForTimeCmdInfoFlyweight implements CmdInfoFlyweight {
    final CmdInfo[] cmdInfoArray;

    SpaceForTimeCmdInfoFlyweight() {
        var maxIndex = CmdKit.merge(CoreGlobalConfig.setting.cmdMaxLen, CoreGlobalConfig.setting.subCmdMaxLen);
        cmdInfoArray = new CmdInfo[maxIndex + 1];

        var cmdMax = CoreGlobalConfig.setting.cmdMaxLen;
        var subCmdMax = CoreGlobalConfig.setting.subCmdMaxLen;

        for (int cmd = 0; cmd < cmdMax; cmd++) {
            for (int subCmd = 0; subCmd < subCmdMax; subCmd++) {
                int cmdMerge = CmdKit.merge(cmd, subCmd);
                cmdInfoArray[cmdMerge] = new CmdInfo(cmd, subCmd, cmdMerge);
            }
        }
    }

    @Override
    public CmdInfo of(int cmd, int subCmd) {
        return cmdInfoArray[CmdKit.merge(cmd, subCmd)];
    }

    @Override
    public CmdInfo of(int cmdMerge) {
        return cmdInfoArray[cmdMerge];
    }
}

