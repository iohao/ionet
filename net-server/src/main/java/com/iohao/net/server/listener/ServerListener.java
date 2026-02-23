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
package com.iohao.net.server.listener;

import com.iohao.net.framework.protocol.Server;
import com.iohao.net.server.NetServerSetting;

/**
 * Listener for net-server peer lifecycle events (before connect, online, offline).
 *
 * @author 渔民小镇
 * @date 2025-09-20
 * @since 25.1
 */
public interface ServerListener {
    default void connectBefore(Server message, NetServerSetting setting) {
    }

    /**
     * Called for servers that are already online or become newly connected.
     *
     * @param server server metadata
     * @param setting runtime setting
     */
    default void onlineServer(Server server, NetServerSetting setting) {
    }

    /**
     * Called when another server goes offline.
     *
     * @param server server metadata
     * @param setting runtime setting
     */
    default void offlineServer(Server server, NetServerSetting setting) {
    }
}
