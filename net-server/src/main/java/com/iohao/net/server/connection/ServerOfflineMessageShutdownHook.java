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
package com.iohao.net.server.connection;

import com.iohao.net.framework.protocol.ServerOfflineMessage;
import com.iohao.net.framework.protocol.Server;
import com.iohao.net.common.kit.MoreKit;
import com.iohao.net.server.NetServerSetting;
import com.iohao.net.server.ServerManager;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 渔民小镇
 * @date 2025-09-26
 * @since 25.1
 */
@Slf4j
public final class ServerOfflineMessageShutdownHook implements ServerShutdownHook {
    @Override
    public void shutdownHook(NetServerSetting setting) {
        List<Server> localServerList = new ArrayList<>();
        List<Server> remoteServerList = new ArrayList<>();

        Map<Integer, NetServerGroup> remoteGroupMap = new HashMap<>();

        ServerManager.forEach((_, server) -> {
            int netId = server.netId();

            if (netId == setting.netId()) {
                localServerList.add(server);
            } else {
                remoteServerList.add(server);

                if (!remoteGroupMap.containsKey(netId)) {
                    MoreKit.putIfAbsent(remoteGroupMap, netId, new NetServerGroup(netId, server.pubName()));
                }
            }
        });

        log.info("remoteServerList: {}", remoteServerList.size());
        log.info("localServerList: {}", localServerList.size());

        if (remoteServerList.isEmpty()) {
            log.info("remoteServerList is empty");
            return;
        }

        for (Server server : localServerList) {
            var message = new ServerOfflineMessage(server.id());
            remoteGroupMap.forEach((netId, group) -> {
                log.info("{} Offline, send offline message to remote {} ", server.name(), netId);
                setting.communicationAggregation().publishMessage(group.pubName(), message);
            });
        }
    }

    private record NetServerGroup(int netId, String pubName) {
    }
}
