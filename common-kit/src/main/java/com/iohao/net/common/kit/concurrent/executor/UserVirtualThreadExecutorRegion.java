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
import com.iohao.net.common.kit.concurrent.*;
import java.util.concurrent.*;

/**
 * A {@link ThreadExecutorRegion} backed by virtual threads, distributing tasks by user ID.
 * <p>
 * Behaves like {@link UserThreadExecutorRegion} but each executor uses a virtual-thread-based
 * {@link java.util.concurrent.ExecutorService} instead of a platform-thread pool.
 *
 * @author 渔民小镇
 * @date 2023-12-19
 */
final class UserVirtualThreadExecutorRegion extends AbstractThreadExecutorRegion {
    final int executorLength;

    /** Create a region with a pool size equal to the nearest power-of-two of available processors. */
    UserVirtualThreadExecutorRegion() {
        super("UserVirtual", RuntimeKit.availableProcessors2n);
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

    /**
     * {@inheritDoc}
     * <p>
     * Overrides the default to return a virtual-thread-based executor.
     *
     * @param name the thread name prefix for the executor
     * @return a virtual-thread executor service
     */
    @Override
    protected ExecutorService createExecutorService(String name) {
        return ExecutorKit.newVirtualExecutor(name);
    }
}
