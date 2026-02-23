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

import com.iohao.net.framework.core.kit.CmdKit;
import org.jspecify.annotations.NonNull;

/**
 * Command routing information composed of a cmd (module) and subCmd (action) pair.
 * Uses flyweight pattern for instance caching via {@link CmdInfoFlyweightFactory}.
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public record CmdInfo(int cmd, int subCmd, int cmdMerge) {

    /**
     * Get or create a CmdInfo for the given cmd and subCmd pair.
     *
     * @param cmd    the module-level command ID
     * @param subCmd the action-level sub-command ID
     * @return a cached or newly created {@code CmdInfo} instance
     */
    public static CmdInfo of(int cmd, int subCmd) {
        return CmdInfoFlyweightFactory.of(cmd, subCmd);
    }

    /**
     * Get or create a CmdInfo from a merged command ID.
     *
     * @param cmdMerge the merged command ID encoding both cmd and subCmd
     * @return a cached or newly created {@code CmdInfo} instance
     */
    public static CmdInfo of(int cmdMerge) {
        return CmdInfoFlyweightFactory.of(cmdMerge);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CmdInfo cmdInfo)) {
            return false;
        }

        return cmdMerge == cmdInfo.cmdMerge;
    }

    @Override
    public int hashCode() {
        return cmdMerge;
    }

    @NonNull
    @Override
    public String toString() {
        return CmdKit.toString(this.cmdMerge);
    }
}
