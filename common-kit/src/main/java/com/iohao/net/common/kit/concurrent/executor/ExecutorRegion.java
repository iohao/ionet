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

/**
 * Executor region, managing implementations of {@link ThreadExecutorRegion} (Thread Executor Region).
 * <pre>
 * Manages {@link UserThreadExecutorRegion}, {@link UserVirtualThreadExecutorRegion}, and {@link SimpleThreadExecutorRegion}.
 * Even if multiple logical servers are started in the same process, thread-related resources will be shared
 * to avoid creating too many threads.
 *
 * {@link UserThreadExecutorRegion} - User Thread Executor Region
 * This executor is mainly used to consume action-related business logic, or user-related business logic.
 * The corresponding ThreadExecutor is obtained through userId to execute the business logic, thereby avoiding concurrency issues.
 *
 * {@link UserVirtualThreadExecutorRegion} - User Virtual Thread Executor Region
 * This executor is mainly used to consume IO-related business logic (such as DB persistence).
 *
 * {@link SimpleThreadExecutorRegion} - Simple Thread Executor Region
 * This executor is similar to {@link UserThreadExecutorRegion}.
 * The corresponding ThreadExecutor can be obtained through an index to execute the business logic, thereby avoiding concurrency issues.
 * This executor can be used if the business logic is compute-intensive and you don't want to occupy the thread resources of {@link UserThreadExecutorRegion}.
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-01-11
 * @see UserThreadExecutorRegion User Thread Executor Region
 * @see UserVirtualThreadExecutorRegion User Virtual Thread Executor Region
 * @see SimpleThreadExecutorRegion Simple Thread Executor Region
 */
public interface ExecutorRegion {
    /**
     * User thread executor region
     *
     * @return User thread executor region
     */
    ThreadExecutorRegion getUserThreadExecutorRegion();

    /**
     * User virtual thread executor region
     *
     * @return User virtual thread executor region
     */
    ThreadExecutorRegion getUserVirtualThreadExecutorRegion();

    /**
     * Simple thread executor region
     *
     * @return Simple thread executor region
     */
    ThreadExecutorRegion getSimpleThreadExecutorRegion();

    /**
     * User thread executor
     *
     * @param index index
     * @return User thread executor
     */
    default ThreadExecutor getUserThreadExecutor(long index) {
        return this.getUserThreadExecutorRegion().getThreadExecutor(index);
    }

    /**
     * User virtual thread executor
     *
     * @param index index
     * @return User virtual thread executor
     */
    default ThreadExecutor getUserVirtualThreadExecutor(long index) {
        return this.getUserVirtualThreadExecutorRegion().getThreadExecutor(index);
    }

    /**
     * Simple thread executor
     *
     * @param index index
     * @return Simple thread executor
     */
    default ThreadExecutor getSimpleThreadExecutor(long index) {
        return this.getSimpleThreadExecutorRegion().getThreadExecutor(index);
    }
}
