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

import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.core.kit.CmdKit;
import com.iohao.net.framework.core.flow.ActionMethodInOut;
import com.iohao.net.framework.core.flow.FlowContext;
import com.iohao.net.framework.i18n.Bundle;
import com.iohao.net.framework.i18n.MessageKey;
import com.iohao.net.common.kit.CollKit;
import com.iohao.net.common.kit.MoreKit;
import com.iohao.net.common.kit.exception.ThrowKit;
import com.iohao.net.common.kit.time.TimeKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/// PluginInOut - StatActionInOut - <a href="https://iohao.github.io/ionet/docs/core_plugin/action_stat">Action Call Statistics Plugin</a>
///
/// StatActionInOut is an action call statistics plugin that can be used to collect relevant data
/// for each action call, such as the **execution count**, **total time cost**, **average time cost**, **maximum time cost**, **number of exceptions triggered**, and other related statistics.
/// Developers can use this data to analyze the **hotspot methods** and **time-consuming methods** in the project, thus achieving precise optimization.
///
/// ```text
///// StatAction statistics record print preview
///     "StatAction{cmd[1-0], Executed [1] times, Total Time Cost [8], Average Time Cost [8], Maximum Time Cost [8], Exceptions [0] times}"
///```
/// for example
/// ```java
/// BarSkeletonBuilder builder = ...;
///// Action Call Statistics Plugin, add the plugin to the business framework
/// var statActionInOut = new StatActionInOut();
/// builder.addInOut(statActionInOut);
///
///// Set the listener for processing after the StatAction statistics record is updated
/// statActionInOut.setListener((statAction, time, flowContext) -> {
///// Simply print the statistics record value StatAction
///     System.out.println(statAction);
///});
///
///// Statistics region (manager of statistical values)
/// StatActionInOut.StatActionRegion region = statActionInOut.getRegion();
///
///// Iterate over all statistics data
/// region.forEach((cmdInfo, statAction) -> {
///// Simply print the statistics record value StatAction
///     System.out.println(statAction);
///// Developers can periodically save this data to logs or a DB for subsequent analysis
///});
///```
///
/// @author 渔民小镇
/// @date 2023-11-17
/// @see StatAction
/// @see StatActionRegion
/// @see StatActionChangeListener
public final class StatActionInOut implements ActionMethodInOut {
    /** Statistics region (manages StatAction) */
    @Getter
    final StatActionRegion region = new StatActionRegion();
    /** Listener for statistics value update */
    @Setter
    StatActionChangeListener listener;

    @Override
    public void fuckIn(FlowContext flowContext) {
    }

    @Override
    public void fuckOut(FlowContext flowContext) {
        long time = TimeKit.elapsedMillis(flowContext.getNanoTime());

        // StatAction and action have a one-to-one correspondence
        this.region.update(time, flowContext);
    }

    public final class StatActionRegion {
        final Map<CmdInfo, StatAction> map = CollKit.ofConcurrentHashMap();

        void update(long time, FlowContext flowContext) {
            CmdInfo cmdInfo = flowContext.getCmdInfo();
            StatAction statAction = getStatAction(cmdInfo);
            statAction.update(flowContext, time);

            // Callback method executed after the statistics value is updated
            if (Objects.nonNull(StatActionInOut.this.listener)) {
                StatActionInOut.this.listener.flow(statAction, time, flowContext);
            }
        }

        public StatAction getStatAction(CmdInfo cmdInfo) {
            StatAction statAction = this.map.get(cmdInfo);

            if (Objects.isNull(statAction)) {
                var newValue = new StatAction(cmdInfo);
                return MoreKit.putIfAbsent(this.map, cmdInfo, newValue);
            }

            return statAction;
        }

        public void forEach(BiConsumer<CmdInfo, StatAction> action) {
            this.map.forEach(action);
        }

        public Stream<StatAction> stream() {
            return this.map.values().stream();
        }

        @Override
        public String toString() {
            return map.values().stream()
                    .map(StatAction::toString)
                    .collect(Collectors.joining("\n"));
        }
    }

    @Getter
    public final class StatAction {
        static final List<TimeRange> emptyRangeList = List.of(TimeRange.create(Long.MAX_VALUE - 1, Long.MAX_VALUE, ""));
        /** Time range list */
        final List<TimeRange> timeRangeList;
        @Getter(AccessLevel.PRIVATE)
        final TimeRange lastTimeRange;

        final CmdInfo cmdInfo;
        /** Action execution count statistics */
        final LongAdder executeCount = new LongAdder();
        /** Total time cost */
        final LongAdder totalTime = new LongAdder();
        /** Action exception trigger count */
        final LongAdder errorCount = new LongAdder();
        /** Maximum time cost */
        volatile long maxTime;

        private StatAction(CmdInfo cmdInfo) {

            this.timeRangeList = Objects.isNull(StatActionInOut.this.listener)
                    ? emptyRangeList
                    : StatActionInOut.this.listener.createTimeRangeList();

            if (CollKit.isEmpty(this.timeRangeList)) {
                ThrowKit.ofIllegalArgumentException("this.timeRangeList is empty");
            }

            this.cmdInfo = cmdInfo;
            this.lastTimeRange = this.timeRangeList.getLast();
        }

        private void update(FlowContext flowContext, long time) {
            // Execution count +1
            this.executeCount.increment();

            if (flowContext.hasError()) {
                this.errorCount.increment();
            }

            if (time == 0) {
                return;
            }

            // Increase total time cost
            this.totalTime.add(time);

            // Record maximum time cost
            if (time > maxTime) {
                this.maxTime = time;
            }
        }

        /**
         * Gets the corresponding TimeRange object based on the time cost.
         * If no corresponding time range is found, the last element in the configured List is used.
         *
         * @param time Time cost
         * @return TimeRange
         */
        public TimeRange getTimeRange(long time) {
            return this.timeRangeList.stream()
                    .filter(timeRange -> timeRange.inRange(time))
                    .findFirst()
                    .orElse(this.lastTimeRange);
        }

        /**
         * Average time cost
         *
         * @return Average time cost
         */
        public long getAvgTime() {
            return this.totalTime.sum() / this.executeCount.sum();
        }

        /** %s, Executed [%s] times, Exceptions [%s] times, Average Time Cost [%d], Maximum Time Cost [%s], Total Time Cost [%s] %s */
        private final String statActionInOutToString = Bundle.getMessage(MessageKey.statActionInOutStatAction);

        @Override
        public String toString() {
            String rangeStr = "";
            if (Objects.nonNull(StatActionInOut.this.listener)) {
                var builder = new StringBuilder();
                for (TimeRange timeRange : this.timeRangeList) {
                    if (timeRange.count.sum() == 0) {
                        continue;
                    }

                    builder.append("\n\t").append(timeRange);
                }

                rangeStr = builder.toString();
            }

            return String.format(statActionInOutToString
                    , CmdKit.toString(this.cmdInfo.cmdMerge())
                    , this.executeCount
                    , this.errorCount
                    , this.getAvgTime()
                    , this.maxTime
                    , this.totalTime
                    , rangeStr
            );
        }
    }

    /**
     * PluginInOut - StatActionInOut - Action Call Statistics Plugin - StatAction Update Listener
     */
    public interface StatActionChangeListener {
        /**
         * Called after the StatAction statistics record is updated
         *
         * @param statAction  action statistics record
         * @param time        action execution time cost
         * @param flowContext flowContext
         */
        void changed(StatAction statAction, long time, FlowContext flowContext);

        /**
         * Creates a time range. If you want finer-grained statistics, just create more time ranges.
         * <p>
         * for example
         * <pre>{@code
         * List.of(
         * TimeRange.create(500, 1000),
         * TimeRange.create(1000, 1500),
         * TimeRange.create(1500, 2000),
         * TimeRange.create(2000, Long.MAX_VALUE, "> 2000"))
         * }
         * </pre>
         *
         * @return Time range list, the List must be non-empty
         */
        default List<TimeRange> createTimeRangeList() {
            return List.of(
                    TimeRange.create(500, 1000),
                    TimeRange.create(1000, 1500),
                    TimeRange.create(1500, 2000),
                    TimeRange.create(2000, Long.MAX_VALUE, "> 2000"));
        }

        /**
         * Trigger condition, a prerequisite for triggering the updateTimeRange method
         * <p>
         * Developers can typically use this method to decide whether to trigger the updateTimeRange method.
         * For example, it can be used to judge whether to monitor only a certain or some specific users within this method.
         *
         * @param statAction  action statistics record
         * @param time        action execution time cost
         * @param flowContext flowContext
         * @return true means the condition is met; when true, the updateTimeRange method will be called
         */
        default boolean triggerUpdateTimeRange(StatAction statAction, long time, FlowContext flowContext) {
            return false;
        }

        /**
         * Called while the StatAction statistics record is being updated, called when the trigger method returns true
         *
         * @param statAction  action statistics record
         * @param time        action execution time cost
         * @param flowContext flowContext
         */
        default void updateTimeRange(StatAction statAction, long time, FlowContext flowContext) {
            statAction.getTimeRange(time).increment();
        }

        /**
         * StatAction update listener flow
         * <p>
         * The default implementation flow is:
         * <pre>
         * 1 First, check if triggerUpdateTimeRange condition is met
         * 2 When triggerUpdateTimeRange is true, updateTimeRange will be executed
         * 3 changed will always be executed regardless
         * </pre>
         *
         * @param statAction  action statistics record
         * @param time        action execution time cost
         * @param flowContext flowContext
         */
        default void flow(StatAction statAction, long time, FlowContext flowContext) {
            if (this.triggerUpdateTimeRange(statAction, time, flowContext)) {
                this.updateTimeRange(statAction, time, flowContext);
            }

            this.changed(statAction, time, flowContext);
        }
    }

    /**
     * PluginInOut - StatActionInOut - Action Call Statistics Plugin - Time Range Record
     *
     * @param start Start time, inclusive
     * @param end   End time, inclusive
     * @param count Execution count triggered within this time range
     * @param name  Name
     */
    public record TimeRange(long start, long end, LongAdder count, String name) {
        /**
         * Create time range
         *
         * @param start Start time, inclusive
         * @param end   End time, inclusive
         * @return TimeRange
         */
        public static TimeRange create(long start, long end) {
            return create(start, end, start + " ~ " + end);
        }

        /**
         * Create time range and specify a name
         *
         * @param start Start time, inclusive
         * @param end   End time, inclusive
         * @param name  Name for printing
         * @return TimeRange
         */
        public static TimeRange create(long start, long end, String name) {
            return new TimeRange(start, end, new LongAdder(), name);
        }

        boolean inRange(long time) {
            return time >= this.start && time <= this.end;
        }

        void increment() {
            this.count.increment();
        }

        /** %s ms requests total [%d] */
        private static final String statActionInOutTimeRange = Bundle.getMessage(MessageKey.statActionInOutTimeRange);

        @NonNull
        @Override
        public String toString() {
            return String.format(statActionInOutTimeRange, this.name, this.count.sum());
        }
    }
}