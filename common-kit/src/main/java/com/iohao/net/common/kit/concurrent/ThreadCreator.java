/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.net.common.kit.concurrent;

import java.util.concurrent.atomic.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Base class for creating threads with configurable name prefix, priority, daemon flag, and thread group.
 *
 * @author 渔民小镇
 * @date 2023-04-02
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
public class ThreadCreator {
    String threadNamePrefix;

    int threadPriority = Thread.NORM_PRIORITY;

    boolean daemon = false;

    ThreadGroup threadGroup;

    final AtomicInteger threadCount = new AtomicInteger();

    public ThreadCreator(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    /**
     * Set the thread group by name.
     *
     * @param name the thread group name
     */
    public void setThreadGroupName(String name) {
        this.threadGroup = new ThreadGroup(name);
    }

    /**
     * Create a new thread with the configured group, name, priority, and daemon flag.
     *
     * @param runnable the task to execute
     * @return the newly created thread
     */
    public Thread createThread(Runnable runnable) {
        Thread thread = new Thread(threadGroup, runnable, nextThreadName());
        thread.setPriority(threadPriority);
        thread.setDaemon(daemon);
        return thread;
    }

    /**
     * Generate the next thread name using the prefix and an auto-incrementing counter.
     *
     * @return the next thread name
     */
    protected String nextThreadName() {
        String format = "%s-%d";
        return String.format(format, this.threadNamePrefix, this.threadCount.incrementAndGet());
    }
}
