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

import com.iohao.net.framework.*;

/**
 * Factory that produces cached {@link CmdInfo} instances using the flyweight pattern.
 * <p>
 * The underlying caching strategy is selected at class-load time based on
 * {@link CmdInfoFlyweightStrategy} configured in {@link CoreGlobalConfig#setting}.
 * When the command space exceeds 128 in either dimension the factory automatically
 * falls back to a {@code Map}-based implementation.
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public final class CmdInfoFlyweightFactory {

    static final CmdInfoFlyweight flyweight;

    static {
        var setting = CoreGlobalConfig.setting;

        if (setting.cmdMaxLen > 128 || setting.subCmdMaxLen > 128) {
            setting.cmdInfoFlyweightStrategy = CmdInfoFlyweightStrategy.MAP;
        }

        flyweight = switch (setting.cmdInfoFlyweightStrategy) {
            case TWO_ARRAY -> new TwoArrayCmdInfoFlyweight();
            case SPACE_FOR_TIME -> new SpaceForTimeCmdInfoFlyweight();
            default -> new MapCmdInfoFlyweight();
        };
    }

    /**
     * Obtain a cached {@link CmdInfo} for the given command and sub-command pair.
     *
     * @param cmd    the primary command ID
     * @param subCmd the sub-command ID
     * @return the flyweight {@link CmdInfo} instance
     */
    public static CmdInfo of(int cmd, int subCmd) {
        return flyweight.of(cmd, subCmd);
    }

    /**
     * Obtain a cached {@link CmdInfo} for the given merged command value.
     *
     * @param cmdMerge the merged command value (cmd and subCmd packed into one int)
     * @return the flyweight {@link CmdInfo} instance
     */
    public static CmdInfo of(int cmdMerge) {
        return flyweight.of(cmdMerge);
    }
}
