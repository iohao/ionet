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

import com.iohao.net.common.kit.*;
import com.iohao.net.common.kit.exception.*;
import com.iohao.net.framework.core.*;
import com.iohao.net.framework.i18n.*;
import java.util.*;
import lombok.experimental.*;

/**
 * Global duplicate route detection tool
 *
 * @author 渔民小镇
 * @date 2022-07-31
 */
@UtilityClass
public class ActionCommandRegionGlobalCheckKit {

    Map<String, ActionCommandRegions> map = CollKit.ofConcurrentHashMap();

    /**
     * Register an ActionCommandRegions instance for global duplicate route detection.
     *
     * @param key                    the unique identifier for this command regions instance
     * @param actionCommandRegions   the command regions to register
     */
    public void putActionCommandRegions(String key, ActionCommandRegions actionCommandRegions) {

        if (map.containsKey(key)) {
            return;
        }

        map.put(key, actionCommandRegions);
    }

    /**
     * Scan all registered command regions and throw if duplicate routes are found.
     *
     * @throws CommonRuntimeException if any duplicate routes are detected
     */
    public void detectGlobalDuplicateRoutes() {

        Map<Integer, ActionCommand> cmdMap = new HashMap<>(100);

        // All business framework command region managers under multi-server single process
        var actionCommandRegionList = map
                .values()
                .parallelStream()
                .flatMap(ActionCommandRegions::streamActionCommandRegion)
                .toList();

        for (ActionCommandRegion actionCommandRegion : actionCommandRegionList) {
            // Routes within the command region
            for (ActionCommand actionCommand : actionCommandRegion.values()) {
                // Route information
                var cmdInfo = actionCommand.cmdInfo;

                int cmdMerge = cmdInfo.cmdMerge();
                // If it's a duplicate route, throw an exception
                if (cmdMap.containsKey(cmdMerge)) {
                    ActionCommand existCommand = cmdMap.get(cmdMerge);

                    var template = Bundle.getMessage(MessageKey.detectGlobalDuplicateRoutes);
                    var message = String.format(template,
                            actionCommandRegion.cmd,
                            cmdInfo.subCmd(),
                            actionCommand.actionControllerClass
                    );

                    System.out.println("existCommand: " + existCommand.actionControllerClass.getSimpleName());
                    throw new CommonRuntimeException(message);
                }

                cmdMap.put(cmdMerge, actionCommand);
            }
        }

        map.clear();
        map = null;
    }
}
