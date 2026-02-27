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

import com.iohao.net.common.kit.*;
import com.iohao.net.common.kit.concurrent.executor.*;
import com.iohao.net.framework.protocol.*;
import com.iohao.net.sbe.*;
import com.iohao.net.server.*;
import com.iohao.net.server.balanced.*;
import com.iohao.net.server.listener.*;
import java.util.*;
import lombok.experimental.*;
import lombok.extern.slf4j.*;

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
