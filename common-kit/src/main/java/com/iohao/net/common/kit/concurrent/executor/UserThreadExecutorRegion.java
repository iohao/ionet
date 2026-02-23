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

import com.iohao.net.common.kit.RuntimeKit;

/**
 * A {@link ThreadExecutorRegion} that distributes tasks by user ID, ensuring same-user tasks
 * execute on the same thread.
 * <p>
 * The user ID is mapped to an executor via a bitmask, so the pool size must be a power of two.
 *
 * @author 渔民小镇
 * @date 2023-12-01
 */
final class UserThreadExecutorRegion extends AbstractThreadExecutorRegion {
    final int executorLength;

    /** Create a region with a pool size equal to the nearest power-of-two of available processors. */
    UserThreadExecutorRegion() {
        super("User", RuntimeKit.availableProcessors2n);
        this.executorLength = RuntimeKit.availableProcessors2n - 1;
    }

    /**
     * {@inheritDoc}
     *
     * @param userId the user identifier; tasks with the same user ID always run on the same executor
     * @return the {@link ThreadExecutor} assigned to the given user ID
     */
    @Override
    public ThreadExecutor getThreadExecutor(long userId) {
        return this.threadExecutors[(int) (userId & this.executorLength)];
    }
}
