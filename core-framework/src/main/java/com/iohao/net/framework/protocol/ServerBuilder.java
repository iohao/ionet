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

import com.iohao.net.common.kit.*;
import com.iohao.net.framework.*;
import com.iohao.net.framework.core.*;
import java.util.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Builder for constructing {@link Server} instances with sensible defaults.
 * <p>
 * Provides a fluent API to configure server identity (id, name, tag), network address,
 * server type, and the associated {@link BarSkeleton}. When {@link #build()} is called,
 * unset fields receive default values: a random id, the name as tag, LOGIC server type,
 * and the local IP address. If a {@link BarSkeleton} is provided, its command routes
 * are extracted and registered with {@link BarSkeletonManager}.
 *
 * @author 渔民小镇
 * @date 2025-08-26
 * @since 25.1
 */
@Setter
@Getter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ServerBuilder {
    int id;
    String name;
    String tag;
    String ip;
    BarSkeleton barSkeleton;
    ServerTypeEnum serverType;

    /**
     * Build and return a new {@link Server} instance from the current configuration.
     * <p>
     * Applies default values for any unset fields, extracts command routes from the
     * {@link BarSkeleton} if present, and registers the skeleton with {@link BarSkeletonManager}.
     *
     * @return the constructed {@link Server}
     * @throws NullPointerException if {@code name} has not been set
     */
    public Server build() {
        Objects.requireNonNull(name);

        defaultSetting();

        int netId = CoreGlobalConfig.getNetId();
        var builder = Server.recordBuilder()
                .setId(this.id)
                .setName(this.name)
                .setTag(this.tag)
                .setServerType(this.serverType)
                .setNetId(netId)
                .setIp(this.ip)
                .setBarSkeleton(this.barSkeleton)
                .setPubName(String.valueOf(netId))
                .setPayloadMap(new HashMap<>());

        if (this.barSkeleton != null) {
            List<Integer> cmdMergeList = this.barSkeleton.actionCommandRegions.listCmdMerge();
            builder.setCmdMerges(ArrayKit.toArrayInt(cmdMergeList));
            BarSkeletonManager.putBarSkeleton(this.id, this.barSkeleton);
        }

        var server = builder.build();

        if (this.barSkeleton != null) {
            this.barSkeleton.server = server;
        }

        return server;
    }

    private void defaultSetting() {
        if (id == 0) {
            id = RandomKit.randomInt(2_000_000, 10_000_000);
        }

        if (tag == null) {
            tag = name;
        }

        if (serverType == null) {
            serverType = ServerTypeEnum.LOGIC;
        }

        if (this.ip == null) {
            this.ip = NetworkKit.LOCAL_IP;
        }
    }
}
