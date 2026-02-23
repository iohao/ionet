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
package com.iohao.net.server.fragment;

import com.iohao.net.framework.protocol.Server;
import com.iohao.net.common.kit.CollKit;
import com.iohao.net.common.kit.concurrent.executor.ExecutorRegionKit;
import com.iohao.net.sbe.ConnectResponseMessageDecoder;
import com.iohao.net.server.NetServerSetting;
import com.iohao.net.server.ServerManager;
import com.iohao.net.server.balanced.BalancedManager;
import com.iohao.net.server.listener.ServerListener;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * Shared online/offline processing helpers for peer state transitions.
 *
 * @author 渔民小镇
 * @date 2025-09-20
 * @since 25.1
 */
@Slf4j
@UtilityClass
public final class ServerLineKit {
    private final Set<Integer> idRecordSet = CollKit.ofConcurrentSet();
    private final int threadIndex = ConnectResponseMessageDecoder.TEMPLATE_ID;

    public void onlineProcess(Server otherServer, NetServerSetting setting) {
        if (idRecordSet.contains(otherServer.id())) {
            return;
        }

        idRecordSet.add(otherServer.id());
        ServerManager.addServer(otherServer);

        ExecutorRegionKit.getSimpleThreadExecutor(threadIndex).executeTry(() -> {

            for (ServerListener listener : setting.listenerList()) {
                listener.onlineServer(otherServer, setting);
            }

            BalancedManager balancedManager = setting.balancedManager();
            balancedManager.register(otherServer);
        });
    }

    public void offlineProcess(Server otherServer, NetServerSetting setting) {
        idRecordSet.remove(otherServer.id());

        ServerManager.removeServer(otherServer);

        ExecutorRegionKit.getSimpleThreadExecutor(threadIndex).executeTry(() -> {
            for (ServerListener listener : setting.listenerList()) {
                listener.offlineServer(otherServer, setting);
            }

            BalancedManager balancedManager = setting.balancedManager();
            balancedManager.unregister(otherServer);
        });
    }
}
