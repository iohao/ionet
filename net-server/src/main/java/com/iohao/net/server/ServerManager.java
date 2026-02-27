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

import com.iohao.net.common.kit.*;
import com.iohao.net.framework.protocol.*;
import java.util.*;
import java.util.function.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Global registry for server metadata known to the current net-server process.
 *
 * @author 渔民小镇
 * @date 2025-09-25
 * @since 25.1
 */
@UtilityClass
public final class ServerManager {
    @Getter
    final Map<Integer, Server> serverMap = CollKit.ofConcurrentHashMap();

    public void addServer(Server server) {
        serverMap.put(server.id(), server);
    }

    public void removeServer(Server server) {
        serverMap.remove(server.id());
    }

    public Server getServerById(int serverId) {
        return serverMap.get(serverId);
    }

    public void forEach(BiConsumer<Integer, Server> action) {
        serverMap.forEach(action);
    }

    public Server ofServer(ServerMessage server) {
        return Server.recordBuilder()
                .setId(server.getId())
                .setName(server.getName())
                .setTag(server.getTag())
                .setServerType(server.getServerType())
                .setNetId(server.getNetId())
                .setIp(server.getIp())
                .setPubName(String.valueOf(server.getNetId()))
                .setCmdMerges(server.getCmdMerges())
                .setPayloadMap(server.getPayloadMap())
                .build();
    }
}
