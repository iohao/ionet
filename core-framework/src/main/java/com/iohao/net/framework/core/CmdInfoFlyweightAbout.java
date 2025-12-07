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

interface CmdInfoFlyweight {
    CmdInfo of(int cmd, int subCmd);

    CmdInfo of(int cmdMerge);
}

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

