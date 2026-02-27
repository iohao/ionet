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
package com.iohao.net.external.core.config;

import lombok.*;

/**
 * External client transport types supported by the gateway.
 *
 * @author 渔民小镇
 * @date 2023-02-18
 */
@Getter
public enum ExternalJoinEnum {
    /** TCP socket */
    TCP("TCP", 1),
    /** WebSocket */
    WEBSOCKET("WebSocket", 2),
    /** UDP socket */
    UDP("UDP", 3),
    /** Custom external socket implementation. */
    EXT_SOCKET("ext socket", 4);

    final String name;
    final int index;

    ExternalJoinEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * Compute the default port for this transport based on a shared base port.
     *
     * @param port base external port
     * @return transport-specific port
     */
    public int cocPort(int port) {
        return switch (this) {
            case TCP -> port + 1;
            case UDP -> port + 2;
            default -> port;
        };
    }
}
