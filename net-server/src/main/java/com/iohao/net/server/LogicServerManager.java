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
package com.iohao.net.server;

import com.iohao.net.common.kit.CollKit;
import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * LogicServerManager Kit
 *
 * @author 渔民小镇
 * @date 2025-10-31
 * @since 25.1
 */
@UtilityClass
public final class LogicServerManager {
    Map<Integer, LogicServer> logicServerMap = CollKit.ofConcurrentHashMap();

    public void put(int serverId, LogicServer logicServer) {
        if (logicServerMap != null) {
            logicServerMap.put(serverId, logicServer);
        }
    }

    public LogicServer remove(int serverId) {
        if (logicServerMap == null) {
            return null;
        }

        return logicServerMap.remove(serverId);
    }

    public void clearSelf() {
        logicServerMap = null;
    }
}
