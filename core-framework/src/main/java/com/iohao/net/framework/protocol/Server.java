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

import com.iohao.net.framework.core.BarSkeleton;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Map;

/**
 * Server
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
    public byte[] getPayload(String name) {
        return payloadMap.get(name);
    }

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
