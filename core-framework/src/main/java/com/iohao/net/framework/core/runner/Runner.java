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
package com.iohao.net.framework.core.runner;

import com.iohao.net.framework.core.*;

/**
 * Runner mechanism, which triggers once before and once after the network connection is established on the logic server.
 * <pre>
 * 1.Before establishing the network connection, {@link Runner#onStart(BarSkeleton)} method is triggered.
 * 2.After establishing the network connection, {@link Runner#onStartAfter(BarSkeleton)} method is triggered.
 * </pre>
 *
 * </pre>
 * for example
 * <pre>{@code
 * BarSkeletonBuilder builder = ...
 *
 * builder.addRunner(new Runner() {
 * @Override
 * public void onStart(BarSkeleton skeleton) {
 * }
 *
 * @Override
 * public void onStartAfter(BarSkeleton skeleton) {
 * }
 * });
 *
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-04-23
 */
public interface Runner {
    /**
     * Before establishing the network connection
     *
     * @param skeleton Business framework
     */
    void onStart(BarSkeleton skeleton);

    /**
     * After establishing the network connection
     *
     * @param skeleton Business framework
     */
    default void onStartAfter(BarSkeleton skeleton) {
    }

    /**
     * runner name
     *
     * @return name
     */
    default String name() {
        return this.getClass().getName();
    }
}