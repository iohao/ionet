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
package com.iohao.net.common.kit.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import lombok.experimental.*;
import org.jspecify.annotations.*;

/**
 * Thread executor factory utilities.
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
@UtilityClass
public class ExecutorKit {

    /**
     * Create a virtual thread executor.
     *
     * @param name thread name prefix
     * @return the virtual thread executor
     */
    public ExecutorService newVirtualExecutor(String name) {
        ThreadFactory factory = Thread.ofVirtual().name(name).factory();
        return newVirtualExecutor(factory);
    }

    /**
     * Create a virtual thread executor.
     *
     * @param name  thread name prefix
     * @param start starting value for the thread name counter
     * @return the virtual thread executor
     */
    public ExecutorService newVirtualExecutor(String name, int start) {
        ThreadFactory factory = Thread.ofVirtual().name(name, start).factory();
        return newVirtualExecutor(factory);
    }

    /**
     * Create a virtual thread executor.
     *
     * @param factory thread factory for virtual threads
     * @return the virtual thread executor
     */
    public ExecutorService newVirtualExecutor(ThreadFactory factory) {
        return Executors.newThreadPerTaskExecutor(factory);
    }

    /**
     * Create a single-thread executor.
     *
     * @param namePrefix thread name prefix
     * @return the single-thread executor
     */
    public ExecutorService newSingleThreadExecutor(String namePrefix) {
        ThreadFactory threadFactory = createThreadFactory(namePrefix);
        return newSingleThreadExecutor(threadFactory);
    }

    /**
     * Create a single-thread executor.
     *
     * @param threadFactory thread factory
     * @return the single-thread executor
     */
    public ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
        return Executors.newSingleThreadExecutor(threadFactory);
    }

    /**
     * Create a cached thread pool.
     *
     * @param namePrefix thread name prefix
     * @return the cached thread pool executor
     */
    public ExecutorService newCacheThreadPool(String namePrefix) {
        ThreadFactory threadFactory = createThreadFactory(namePrefix);
        return newCacheThreadPool(threadFactory);
    }

    /**
     * Create a cached thread pool.
     *
     * @param threadFactory thread factory
     * @return the cached thread pool executor
     */
    public ExecutorService newCacheThreadPool(ThreadFactory threadFactory) {
        return Executors.newCachedThreadPool(threadFactory);
    }

    /**
     * Create a fixed-size thread pool.
     *
     * @param corePoolSize  pool size
     * @param threadFactory thread factory
     * @return the fixed-size thread pool executor
     */
    public ExecutorService newFixedThreadPool(int corePoolSize, ThreadFactory threadFactory) {
        return Executors.newFixedThreadPool(corePoolSize, threadFactory);
    }

    /**
     * Create a fixed-size thread pool.
     *
     * @param corePoolSize pool size
     * @param namePrefix   thread name prefix
     * @return the fixed-size thread pool executor
     */
    public ExecutorService newFixedThreadPool(int corePoolSize, String namePrefix) {
        ThreadFactory threadFactory = createThreadFactory(namePrefix);
        return newFixedThreadPool(corePoolSize, threadFactory);
    }

    /**
     * Create a single-thread scheduled executor.
     *
     * @param threadFactory thread factory
     * @return the single-thread scheduled executor
     */
    public ScheduledExecutorService newSingleScheduled(ThreadFactory threadFactory) {
        return newScheduled(1, threadFactory);
    }

    /**
     * Create a single-thread scheduled executor.
     *
     * @param namePrefix thread name prefix
     * @return the single-thread scheduled executor
     */
    public ScheduledExecutorService newSingleScheduled(String namePrefix) {
        ThreadFactory threadFactory = createThreadFactory(namePrefix);
        return newScheduled(1, threadFactory);
    }

    /**
     * Create a scheduled executor with the specified pool size.
     *
     * @param corePoolSize pool size
     * @param namePrefix   thread name prefix
     * @return the scheduled executor
     */
    public ScheduledExecutorService newScheduled(int corePoolSize, String namePrefix) {
        ThreadFactory threadFactory = createThreadFactory(namePrefix);
        return newScheduled(corePoolSize, threadFactory);
    }

    /**
     * Create a scheduled executor with the specified pool size.
     *
     * @param corePoolSize  pool size
     * @param threadFactory thread factory
     * @return the scheduled executor
     */
    public ScheduledExecutorService newScheduled(int corePoolSize, ThreadFactory threadFactory) {
        return new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);
    }

    /**
     * Create a thread factory. Daemon is true by default.
     *
     * @param namePrefix thread name prefix
     * @return the thread factory
     */
    public ThreadFactory createThreadFactory(String namePrefix) {
        return createThreadFactory(namePrefix, true);
    }

    /**
     * Create a thread factory.
     *
     * @param namePrefix thread name prefix
     * @param daemon     whether threads are daemon threads
     * @return the thread factory
     */
    public ThreadFactory createThreadFactory(@NonNull String namePrefix, boolean daemon) {
        final AtomicLong threadNumber = new AtomicLong();

        return runnable -> {
            String threadName = "%s-%s".formatted(namePrefix, threadNumber.getAndIncrement());
            Thread thread = new Thread(runnable, threadName);
            thread.setDaemon(daemon);
            return thread;
        };
    }

    /**
     * Create a single-named daemon thread factory.
     *
     * @param name the fixed thread name
     * @return the thread factory
     */
    public ThreadFactory createSigleThreadFactory(@NonNull String name) {
        return runnable -> {
            Thread thread = new Thread(runnable, name);
            thread.setDaemon(true);
            return thread;
        };
    }
}
