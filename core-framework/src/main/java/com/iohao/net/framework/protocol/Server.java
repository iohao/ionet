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

import com.iohao.net.framework.core.*;
import java.util.*;
import lombok.*;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.*;

/**
 * Immutable record representing a registered server instance in the ionet cluster.
 * <p>
 * Holds the server's identity (id, name, tag), network coordinates (ip, netId, pubName),
 * the set of command routes it handles ({@code cmdMerges}), an extensible payload map,
 * and a reference to its {@link BarSkeleton} execution engine. Equality and hashing
 * are based solely on the server {@link #id}.
 *
 * @param id           unique server identifier
 * @param name         human-readable server name
 * @param tag          grouping tag (defaults to name)
 * @param serverType   the type of this server (e.g., LOGIC, EXTERNAL)
 * @param netId        network-level identifier
 * @param ip           the IP address of this server
 * @param pubName      the publication name used for Aeron channels
 * @param cmdMerges    array of merged command route keys this server handles
 * @param payloadMap   extensible key-value payload storage
 * @param barSkeleton  the execution engine associated with this server
 *
 * @author 渔民小镇
 * @date 2025-09-08
 * @since 25.1
 */
@Builder(setterPrefix = "set", builderClassName = "InternalBuilder", builderMethodName = "recordBuilder")
public record Server(
        int id,
        String name,
        String tag,
        ServerTypeEnum serverType,
        int netId,
        String ip,
        String pubName,
        int[] cmdMerges,
        Map<String, byte[]> payloadMap,
        BarSkeleton barSkeleton
) {
    /**
     * Retrieve a payload entry by name.
     *
     * @param name the payload key
     * @return the payload byte array, or {@code null} if not present
     */
    public byte[] getPayload(String name) {
        return payloadMap.get(name);
    }

    /**
     * Store a payload entry by name.
     *
     * @param name the payload key
     * @param data the payload byte array
     */
    public void addPayload(String name, byte[] data) {
        payloadMap.put(name, data);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Server server)) {
            return false;
        }

        return id == server.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Server{" +
                "serverType=" + serverType +
                ", tag='" + tag + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", netId=" + netId +
                ", ip='" + ip + '\'' +
                ", payloadMap=" + (payloadMap == null ? "{}" : payloadMap.keySet()) +
                ", cmdMerges=" + Arrays.toString(cmdMerges) +
                '}';
    }
}
