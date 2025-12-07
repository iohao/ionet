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
package com.iohao.net.framework.core.kit;

import com.iohao.net.framework.core.CmdInfo;

/**
 * CmdKit
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public class CmdKit {
    public static int getCmd(int cmdMerge) {
        return cmdMerge >>> 16;
    }

    public static int getSubCmd(int cmdMerge) {
        return cmdMerge & 0xFFFF;
    }

    public static int merge(int cmd, int subCmd) {
        return (cmd << 16) | subCmd;
    }

    public static String toString(int cmdMerge) {
        return "[cmd:%d-%d %d]".formatted(
                getCmd(cmdMerge),
                getSubCmd(cmdMerge),
                cmdMerge
        );
    }

    public static String toSimpleString(CmdInfo cmdInfo) {
        return cmdInfo.cmd() + "-" + cmdInfo.subCmd();
    }
}
