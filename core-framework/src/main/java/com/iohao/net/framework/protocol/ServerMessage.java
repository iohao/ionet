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
package com.iohao.net.framework.protocol;

import java.util.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Sealed base class for server registration and connection messages exchanged with the center server.
 * <p>
 * Carries the server's identity (id, name, tag), network coordinates (ip, netId, pubName),
 * server type, the set of command routes it handles ({@code cmdMerges}), and an extensible
 * payload map. Permitted subclasses: {@link ServerRequestMessage} for registration requests
 * and {@link ConnectResponseMessage} for connection responses.
 *
 * @author 渔民小镇
 * @date 2025-09-05
 * @since 25.1
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public sealed class ServerMessage permits ServerRequestMessage, ConnectResponseMessage {
    final Map<String, byte[]> payloadMap = new HashMap<>();

    int id;
    String name;
    String tag;
    ServerTypeEnum serverType;
    int netId;
    String ip;
    int[] cmdMerges;
    String pubName;

    /**
     * Store a payload entry by name.
     *
     * @param name the payload key
     * @param data the payload byte array
     */
    public void addPayload(String name, byte[] data) {
        payloadMap.put(name, data);
    }

    /**
     * Retrieve a payload entry by name.
     *
     * @param name the payload key
     * @return the payload byte array, or {@code null} if not present
     */
    public byte[] getPayload(String name) {
        return payloadMap.get(name);
    }
}
