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
package com.iohao.net.framework.core.flow.internal;

import com.iohao.net.framework.core.flow.ActionMethodInOut;
import com.iohao.net.framework.core.flow.FlowContext;
import com.iohao.net.framework.i18n.Bundle;
import com.iohao.net.framework.i18n.MessageKey;
import com.iohao.net.common.kit.MoreKit;
import com.iohao.net.common.kit.concurrent.executor.ThreadExecutor;
import com.iohao.net.common.kit.time.TimeKit;
import lombok.Getter;
import com.iohao.net.common.kit.CollKit;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * PluginInOut - ThreadMonitorInOut - <a href="https://iohao.github.io/ionet/docs/core_plugin/action_thread_monitor">Business Thread Monitoring Plugin</a>
 * <p>
 * for example
 * <pre>{@code
 * BarSkeletonBuilder builder = ...;
 *     // Business thread monitoring plugin, add the plugin to the business framework
 *     var threadMonitorInOut = new ThreadMonitorInOut();
 *     builder.addInOut(threadMonitorInOut);
 * }</pre>
 * <p>
 * Print preview
 * <pre>
 * Business thread [RequestMessage-8-1] executed 1 business task in total, average time consumption 1 ms, 91 tasks remaining to be executed
 * Business thread [RequestMessage-8-2] executed 1 business task in total, average time consumption 1 ms, 0 tasks remaining to be executed
 * Business thread [RequestMessage-8-3] executed 1 business task in total, average time consumption 1 ms, 36 tasks remaining to be executed
 * Business thread [RequestMessage-8-4] executed 1 business task in total, average time consumption 1 ms, 0 tasks remaining to be executed
 * Business thread [RequestMessage-8-5] executed 1 business task in total, average time consumption 1 ms, 88 tasks remaining to be executed
 * Business thread [RequestMessage-8-6] executed 1 business task in total, average time consumption 1 ms, 0 tasks remaining to be executed
 * Business thread [RequestMessage-8-7] executed 7 business tasks in total, average time consumption 1 ms, 56 tasks remaining to be executed
 * Business thread [RequestMessage-8-8] executed 1 business task in total, average time consumption 1 ms, 0 tasks remaining to be executed
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-11-22
 */
@Getter
public final class ThreadMonitorInOut implements ActionMethodInOut {
    final ThreadMonitorRegion region = new ThreadMonitorRegion();

    /**
     * No-op before action method execution.
     *
     * @param flowContext the current request flow context
     */
    @Override
    public void fuckIn(FlowContext flowContext) {
    }

    /**
     * Record thread execution statistics after action method execution.
     * <p>
     * Captures the elapsed time and updates the {@link ThreadMonitorRegion} with the
     * current thread executor's metrics.
     *
     * @param flowContext the current request flow context
     */
    @Override
    public void fuckOut(FlowContext flowContext) {
        ThreadExecutor threadExecutor = flowContext.getCurrentThreadExecutor();
        if (Objects.nonNull(threadExecutor)) {
            var millis = TimeKit.elapsedMillis(flowContext.getNanoTime());
            region.update(millis, threadExecutor);
        }
    }

    /**
     * Region that manages {@link ThreadMonitor} instances, one per business thread.
     */
    @Getter
    public static class ThreadMonitorRegion {
        final Map<String, ThreadMonitor> map = CollKit.ofConcurrentHashMap();

        /**
         * Get or create the {@link ThreadMonitor} for the given thread executor.
         *
         * @param threadExecutor the thread executor
         * @return the monitoring record for the thread
         */
        private ThreadMonitor getStatThread(ThreadExecutor threadExecutor) {
            String name = threadExecutor.name();
            ThreadMonitor threadMonitor = this.map.get(name);

            if (Objects.isNull(threadMonitor)) {
                ThreadMonitor newValue = ThreadMonitor.create(name, threadExecutor);
                return MoreKit.putIfAbsent(this.map, name, newValue);
            }

            return threadMonitor;
        }

        /**
         * Update the monitoring record for the given thread executor.
         *
         * @param time           elapsed time in milliseconds
         * @param threadExecutor the thread executor
         */
        void update(long time, ThreadExecutor threadExecutor) {
            this.getStatThread(threadExecutor).increment(time);
        }

        /**
         * Iterate over all non-empty thread monitors.
         *
         * @param action the action to perform for each monitor
         */
        public void forEach(Consumer<ThreadMonitor> action) {
            this.map.values()
                    .stream()
                    .filter(ThreadMonitor::notEmpty)
                    .forEach(action);
        }

        @Override
        public String toString() {
            Map<String, ThreadMonitor> sortMap = new TreeMap<>(this.map);
            return sortMap.values().stream()
                    .filter(ThreadMonitor::notEmpty)
                    .map(ThreadMonitor::toString)
                    .collect(Collectors.joining("\n"));
        }
    }

    /**
     * Per-thread monitoring record holding execution count, total time, and remaining queue size.
     *
     * @param name         Business thread name
     * @param executeCount Number of tasks already executed by the thread
     * @param totalTime    Total time consumed for executing tasks
     * @param executor     ThreadPoolExecutor
     */
    public record ThreadMonitor(String name, LongAdder executeCount, LongAdder totalTime, ThreadExecutor executor) {

        /**
         * Create a new monitoring record for the given thread.
         *
         * @param name     the thread name
         * @param executor the thread executor
         * @return a new {@code ThreadMonitor} instance
         */
        public static ThreadMonitor create(String name, ThreadExecutor executor) {
            return new ThreadMonitor(name, new LongAdder(), new LongAdder(), executor);
        }

        /**
         * Increment the execution count and accumulate the elapsed time.
         *
         * @param time elapsed time in milliseconds
         */
        void increment(long time) {
            this.executeCount.increment();
            this.totalTime.add(time);
        }

        /**
         * Check whether this monitor has recorded any executions.
         *
         * @return {@code true} if at least one task has been executed
         */
        boolean notEmpty() {
            return this.executeCount.sum() > 0;
        }

        /**
         * Average time consumption
         *
         * @return Average time consumption
         */
        public long getAvgTime() {
            return this.totalTime.sum() / this.executeCount.sum();
        }

        /**
         * The number of tasks remaining that have not been executed yet
         *
         * @return The number of tasks remaining that have not been executed yet
         */
        public int countRemaining() {
            return Optional.ofNullable(this.executor)
                    .map(ThreadExecutor::getWorkQueue)
                    .orElse(0);
        }

        /** Business thread [%s] executed %s tasks in total, average time cost %d ms, %d tasks remaining */
        private static final String threadMonitorInOutThreadMonitor = Bundle.getMessage(MessageKey.threadMonitorInOutThreadMonitor);

        @NonNull
        @Override
        public String toString() {
            return String.format(threadMonitorInOutThreadMonitor
                    , this.name
                    , this.executeCount.sum()
                    , this.getAvgTime()
                    , this.countRemaining()
            );
        }
    }
}
