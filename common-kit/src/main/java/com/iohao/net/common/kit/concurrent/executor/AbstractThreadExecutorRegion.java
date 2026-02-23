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

import com.iohao.net.common.kit.concurrent.FixedNameThreadFactory;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.*;

/**
 * Abstract base for thread executor regions that distribute tasks across a fixed pool of executors.
 * <p>
 * Subclasses define how an index maps to a specific {@link ThreadExecutor} and may override
 * {@link #createExecutorService(String)} to customize the underlying executor (e.g. virtual threads).
 *
 * @author 渔民小镇
 * @date 2023-12-01
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
abstract sealed class AbstractThreadExecutorRegion implements ThreadExecutorRegion
        permits
        UserThreadExecutorRegion,
        UserVirtualThreadExecutorRegion,
        SimpleThreadExecutorRegion {

    final ThreadExecutor[] threadExecutors;

    /**
     * Create an executor region with the given thread name prefix and pool size.
     * <p>
     * Each executor in the pool is backed by a single-thread {@link java.util.concurrent.ExecutorService}
     * created via {@link #createExecutorService(String)}, and is pre-warmed with a no-op task.
     *
     * @param threadName base name used to build each thread's name prefix
     * @param executorSize number of {@link ThreadExecutor} instances in the pool
     */
    AbstractThreadExecutorRegion(String threadName, int executorSize) {
        this.threadExecutors = new ThreadExecutor[executorSize];

        for (int i = 0; i < executorSize; i++) {
            int threadNo = i + 1;
            String threadNamePrefix = String.format("%s-%s-%s", threadName, executorSize, threadNo);
            var executor = this.createExecutorService(threadNamePrefix);
            this.threadExecutors[i] = new ThreadExecutor(threadNamePrefix, executor, threadNo);

            this.threadExecutors[i].execute(() -> {
                // preload
            });
        }
    }

    /**
     * Create the {@link java.util.concurrent.ExecutorService} for a single {@link ThreadExecutor}.
     * <p>
     * The default implementation returns a single-thread {@link java.util.concurrent.ThreadPoolExecutor}
     * with an unbounded queue. Subclasses may override this to provide alternative executors
     * (e.g. virtual-thread-based).
     *
     * @param name the thread name prefix for the executor
     * @return a new executor service instance
     */
    protected ExecutorService createExecutorService(String name) {
        ThreadFactory threadFactory = new FixedNameThreadFactory(name);

        return new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory);
    }
}
