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
package com.iohao.net.common.kit.concurrent.executor;

import com.iohao.net.common.kit.*;
import lombok.*;
import lombok.experimental.*;

/**
 * A general-purpose {@link ThreadExecutorRegion} for non-user-specific task distribution.
 * <p>
 * Tasks are assigned to executors using a bitmask on the supplied index, providing
 * a fast modulo distribution across the available pool.
 *
 * @author 渔民小镇
 * @date 2023-12-01
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
final class SimpleThreadExecutorRegion extends AbstractThreadExecutorRegion {
    final int executorLength;

    /** Create a region with a pool size equal to the nearest power-of-two of available processors. */
    SimpleThreadExecutorRegion() {
        super("Simple", RuntimeKit.availableProcessors2n);
        this.executorLength = RuntimeKit.availableProcessors2n - 1;
    }

    /**
     * {@inheritDoc}
     *
     * @param i arbitrary index used to select an executor via bitmask
     * @return the {@link ThreadExecutor} mapped to the given index
     */
    @Override
    public ThreadExecutor getThreadExecutor(long i) {
        return this.threadExecutors[(int) (i & this.executorLength)];
    }
}
