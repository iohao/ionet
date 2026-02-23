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
package com.iohao.net.framework.core;

import com.iohao.net.common.kit.CollKit;
import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * Global registry that maps logic-server IDs to their {@link BarSkeleton} instances.
 * <p>
 * Each logic server registers its skeleton during startup so that other components
 * (e.g. routing, inter-server calls) can look it up by server ID at runtime.
 *
 * @author 渔民小镇
 * @date 2025-09-02
 * @since 25.1
 */
@UtilityClass
public final class BarSkeletonManager {
    static final Map<Integer, BarSkeleton> barSkeletonMap = CollKit.ofConcurrentHashMap();

    /**
     * Register a {@link BarSkeleton} for the given server ID.
     *
     * @param serverId    unique identifier of the logic server
     * @param barSkeleton the skeleton instance to register
     */
    public void putBarSkeleton(int serverId, BarSkeleton barSkeleton) {
        barSkeletonMap.put(serverId, barSkeleton);
    }

    /**
     * Retrieve the {@link BarSkeleton} associated with the given server ID.
     *
     * @param serverId unique identifier of the logic server
     * @return the registered skeleton, or {@code null} if none is registered
     */
    public BarSkeleton getBarSkeleton(int serverId) {
        return barSkeletonMap.get(serverId);
    }
}
