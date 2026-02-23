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
 * Command ID manipulation utilities. Packs cmd and subCmd into a single int
 * using bit shifting (cmd occupies upper 16 bits, subCmd occupies lower 16 bits).
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public class CmdKit {
    /**
     * Extract the cmd (upper 16 bits) from a merged command ID.
     *
     * @param cmdMerge the merged command ID
     * @return the cmd value
     */
    public static int getCmd(int cmdMerge) {
        // Unsigned right-shift by 16 to isolate the upper 16 bits (cmd)
        return cmdMerge >>> 16;
    }

    /**
     * Extract the subCmd (lower 16 bits) from a merged command ID.
     *
     * @param cmdMerge the merged command ID
     * @return the subCmd value
     */
    public static int getSubCmd(int cmdMerge) {
        // Mask with 0xFFFF to isolate the lower 16 bits (subCmd)
        return cmdMerge & 0xFFFF;
    }

    /**
     * Merge cmd and subCmd into a single int ({@code cmd << 16 | subCmd}).
     *
     * @param cmd    the command module ID (upper 16 bits)
     * @param subCmd the sub-command ID (lower 16 bits)
     * @return the merged command ID
     */
    public static int merge(int cmd, int subCmd) {
        // Left-shift cmd into upper 16 bits, OR with subCmd in lower 16 bits
        return (cmd << 16) | subCmd;
    }

    /**
     * Format a merged command ID as a human-readable string {@code [cmd:X-Y Z]}.
     *
     * @param cmdMerge the merged command ID
     * @return formatted string representation
     */
    public static String toString(int cmdMerge) {
        return "[cmd:%d-%d %d]".formatted(
                getCmd(cmdMerge),
                getSubCmd(cmdMerge),
                cmdMerge
        );
    }

    /**
     * Format a {@link CmdInfo} as a simple {@code "cmd-subCmd"} string.
     *
     * @param cmdInfo the command info record
     * @return simple string representation
     */
    public static String toSimpleString(CmdInfo cmdInfo) {
        return cmdInfo.cmd() + "-" + cmdInfo.subCmd();
    }
}
