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

import com.iohao.net.common.kit.*;
import com.iohao.net.framework.*;
import com.iohao.net.framework.i18n.*;
import com.iohao.net.framework.toy.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Registry of all {@link ActionCommandRegion} instances, indexed by cmd.
 * Converts the region map into a 2D array for O(1) command lookup at runtime.
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
@FieldDefaults(level = AccessLevel.PUBLIC)
public final class ActionCommandRegions {
    private static final ActionCommand[][] EMPTY = new ActionCommand[0][0];

    /**
     * Action map.
     * key : cmd
     */
    final Map<Integer, ActionCommandRegion> regionMap = CollKit.ofConcurrentHashMap();

    /**
     * ActionCommands
     */
    ActionCommand[][] actionCommands = EMPTY;

    /**
     * Collect all merged command IDs across all registered regions.
     *
     * @return list of merged command IDs ({@code cmdMerge} values)
     */
    public List<Integer> listCmdMerge() {
        return regionMap.values()
                // Concurrent stream
                .parallelStream()
                // Merge map.values into a list
                .flatMap((Function<ActionCommandRegion, Stream<ActionCommand>>) actionCommandRegion -> actionCommandRegion.values().parallelStream())
                // Convert to command routing information
                .map(o -> o.cmdInfo)
                // Convert to merged routing
                .map(CmdInfo::cmdMerge)
                .collect(Collectors.toList())
                ;
    }

    /**
     * Stream all registered {@link ActionCommandRegion} instances.
     *
     * @return parallel stream of action command regions
     */
    public Stream<ActionCommandRegion> streamActionCommandRegion() {
        return this.regionMap.values().parallelStream();
    }

    ActionCommandRegion getActionCommandRegion(int cmd) {

        var actionCommandRegion = this.regionMap.get(cmd);

        if (actionCommandRegion == null) {
            var newValue = new ActionCommandRegion(cmd);
            return MoreKit.putIfAbsent(this.regionMap, cmd, newValue);
        }

        return actionCommandRegion;
    }

    void initActionCommandArray() {
        this.actionCommands = this.convertArray();
    }

    /**
     * Convert the map into a two-dimensional array
     * <p>
     * First dimension: cmd
     * Second dimension: subCmd under the cmd
     *
     * @return Two-dimensional array
     */
    private ActionCommand[][] convertArray() {

        if (this.regionMap.isEmpty()) {
            return EMPTY;
        }

        int max = getMaxCmd();
        var behaviors = new ActionCommand[max][1];

        this.regionMap.keySet().forEach(cmd -> {
            var actionCommandRegion = this.regionMap.get(cmd);
            behaviors[cmd] = actionCommandRegion.arrayActionCommand();
        });

        return behaviors;
    }

    private int getMaxCmd() {

        var setting = CoreGlobalConfig.setting;

        int max = this.regionMap
                .keySet()
                .stream()
                .max(Integer::compareTo)
                .orElse(0) + 1;

        if (max > setting.cmdMaxLen) {
            /*
             * %s exceeds the maximum default value.
             * Please set the capacity manually if necessary.
             * Default maximum capacity %d, current capacity %d
             */
            var info = Bundle.getMessage(MessageKey.cmdMergeLimit).formatted(
                    "cmd", setting.cmdMaxLen, max
            );

            IonetBanner.ofRuntimeException(info);
        }

        // subCmd
        for (ActionCommandRegion actionCommandRegion : this.regionMap.values()) {

            int subCmdMax = actionCommandRegion.getMaxSubCmd() + 1;

            if (subCmdMax > setting.subCmdMaxLen) {
                var info = Bundle.getMessage(MessageKey.cmdMergeLimit).formatted(
                        "subCmd", setting.subCmdMaxLen, subCmdMax
                );

                IonetBanner.ofRuntimeException(info);
            }
        }

        return max;
    }
}