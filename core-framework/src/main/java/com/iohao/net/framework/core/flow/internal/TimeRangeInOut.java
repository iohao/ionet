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
import com.iohao.net.common.kit.CollKit;
import com.iohao.net.common.kit.concurrent.TaskKit;
import com.iohao.net.common.kit.time.TimeFormatKit;
import com.iohao.net.common.kit.time.TimeKit;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * PluginInOut - TimeRangeInOut - <a href="https://iohao.github.io/ionet/docs/core_plugin/action_time_range">Call Statistics Plugin for Each Time Period</a>
 *
 * <pre>{@code
 *     BarSkeletonBuilder builder = ...;
 *     // Add the plugin to the business framework
 *     var timeRangeInOut = new TimeRangeInOut();
 *     builder.addInOut(timeRangeInOut);
 * }
 * </pre>
 * Print Preview - Call statistics data for each hour and minute segment of the day
 * <pre>
 *  2023-11-29 Total action calls: [100] times
 * 	0:00 共 8 次; - [15~30 min 3 次] - [30~45 min 2 次] - [45~59 min 3 次]
 * 	1:00 共 9 次; - [0~15 min 1 次] - [15~30 min 4 次] - [30~45 min 1 次] - [45~59 min 3 次]
 * 	2:00 共 4 次; - [0~15 min 1 次] - [15~30 min 2 次] - [45~59 min 1 次]
 * 	3:00 共 2 次; - [0~15 min 1 次] - [15~30 min 1 次]
 * 	4:00 共 1 次; - [0~15 min 1 次]
 * 	5:00 共 4 次; - [0~15 min 1 次] - [15~30 min 1 次] - [30~45 min 1 次] - [45~59 min 1 次]
 * 	6:00 共 5 次; - [0~15 min 1 次] - [15~30 min 1 次] - [30~45 min 1 次] - [45~59 min 2 次]
 * 	7:00 共 4 次; - [15~30 min 2 次] - [30~45 min 1 次] - [45~59 min 1 次]
 * 	8:00 共 4 次; - [0~15 min 1 次] - [30~45 min 3 次]
 * 	9:00 共 4 次; - [15~30 min 2 次] - [30~45 min 2 次]
 * 	10:00 共 5 次; - [15~30 min 2 次] - [30~45 min 1 次] - [45~59 min 2 次]
 * 	11:00 共 3 次; - [15~30 min 2 次] - [45~59 min 1 次]
 * 	12:00 共 4 次; - [0~15 min 2 次] - [30~45 min 2 次]
 * 	13:00 共 1 次; - [30~45 min 1 次]
 * 	14:00 共 5 次; - [0~15 min 1 次] - [45~59 min 4 次]
 * 	15:00 共 6 次; - [0~15 min 1 次] - [15~30 min 2 次] - [45~59 min 3 次]
 * 	16:00 共 4 次; - [0~15 min 1 次] - [15~30 min 1 次] - [30~45 min 1 次] - [45~59 min 1 次]
 * 	17:00 共 7 次; - [0~15 min 1 次] - [15~30 min 3 次] - [30~45 min 3 次]
 * 	18:00 共 2 次; - [0~15 min 1 次] - [15~30 min 1 次]
 * 	19:00 共 7 次; - [0~15 min 1 次] - [15~30 min 3 次] - [30~45 min 3 次]
 * 	20:00 共 5 次; - [15~30 min 3 次] - [30~45 min 2 次]
 * 	21:00 共 3 次; - [15~30 min 2 次] - [30~45 min 1 次]
 * 	22:00 共 1 次; - [45~59 min 1 次]
 * 	23:00 共 2 次; - [15~30 min 1 次] - [45~59 min 1 次]
 * </pre>
 * set Listener example
 * <pre>{@code
 * private void setListener(TimeRangeInOut inOut) {
 *     inOut.setListener(new TimeRangeInOut.ChangeListener() {
 *         @Override
 *         public List<TimeRangeInOut.TimeRangeMinute> createListenerTimeRangeMinuteList() {
 *             return List.of(
 *                     // Only statistically count these 3 time points: 0, 1, and 59 minutes
 *                     TimeRangeInOut.TimeRangeMinute.create(0, 0),
 *                     TimeRangeInOut.TimeRangeMinute.create(1, 1),
 *                     TimeRangeInOut.TimeRangeMinute.create(59, 59)
 *             );
 *         }
 *     });
 * }
 *
 * }</pre>
 *
 * @author 渔民小镇
 * @date 2023-11-29
 * @see ChangeListener
 */
@Getter
public final class TimeRangeInOut implements ActionMethodInOut {

    final TimeRangeDayRegion region = new TimeRangeDayRegion();
    ChangeListener listener = new ChangeListener() {
    };

    /**
     * Set Listener
     *
     * @param listener listener
     */
    public void setListener(ChangeListener listener) {
        this.listener = Objects.requireNonNull(listener);
    }

    @Override
    public void fuckIn(FlowContext flowContext) {
    }

    @Override
    public void fuckOut(FlowContext flowContext) {
        LocalDate localDate = listener.nowLocalDate();
        LocalTime localTime = listener.nowLocalTime();

        this.region.update(localDate, localTime, flowContext);
    }

    /**
     * PluginInOut - TimeRangeInOut - Call statistics plugin for each time period - call statistics object domain
     */
    @Getter
    public final class TimeRangeDayRegion {
        final Map<LocalDate, TimeRangeDay> map = CollKit.ofConcurrentHashMap();

        public void forEach(BiConsumer<LocalDate, TimeRangeDay> action) {
            this.map.forEach(action);
        }

        void update(LocalDate localDate, LocalTime localTime, FlowContext flowContext) {

            TimeRangeDay timeRangeDay = this.getTimeRangeDay(localDate);
            timeRangeDay.increment(localTime);

            // 变更回调
            listener.changed(timeRangeDay, localTime, flowContext);
        }

        public TimeRangeDay getTimeRangeDay(LocalDate localDate) {

            TimeRangeDay timeRangeDay = this.map.get(localDate);

            // 无锁化
            if (Objects.isNull(timeRangeDay)) {

                TimeRangeDay rangeDay = TimeRangeInOut.this.listener.createTimeRangeDay(localDate);
                timeRangeDay = this.map.putIfAbsent(localDate, Objects.requireNonNull(rangeDay));

                if (Objects.isNull(timeRangeDay)) {
                    timeRangeDay = this.map.get(localDate);

                    /*
                     * 回调昨天的数据
                     * 原计划是想保留 map 中的数据让开发者自行处理，
                     * 考虑到开发者可能会忘记移除 map 中的数据，为防止造成隐患，这里就直接移除昨天的数据了。
                     */
                    TaskKit.execute(() -> {
                        LocalDate yesterdayLocalDate = localDate.minusDays(1);
                        Optional.ofNullable(this.map.remove(yesterdayLocalDate))
                                .ifPresent(timeRangeYesterday -> TimeRangeInOut.this.listener.callbackYesterday(timeRangeYesterday));
                    });
                }
            }

            return timeRangeDay;
        }
    }

    /**
     * PluginInOut - TimeRangeInOut - Call statistics plugin for each time period - call statistics object for one day
     *
     * @param localDate      Date
     * @param count          Total number of action calls for one day
     * @param timeRangeHours Time periods
     */
    public record TimeRangeDay(LocalDate localDate, LongAdder count, TimeRangeHour[] timeRangeHours) {
        public static TimeRangeDay create(LocalDate localDate, List<TimeRangeHour> timeRangeHours) {

            TimeRangeDay timeRangeDay = new TimeRangeDay(localDate, new LongAdder(), new TimeRangeHour[24]);

            for (TimeRangeHour timeRangeHour : timeRangeHours) {
                int hour = timeRangeHour.getHour();
                timeRangeDay.timeRangeHours[hour] = timeRangeHour;
            }

            return timeRangeDay;
        }

        public Stream<TimeRangeHour> stream() {
            return Arrays.stream(this.timeRangeHours)
                    .filter(Objects::nonNull);
        }

        public TimeRangeHour getTimeRangeHour(LocalTime localTime) {
            var hour = localTime.getHour();
            return this.timeRangeHours[hour];
        }

        public void increment(LocalTime localTime) {

            this.count.increment();

            var timeRangeHour = this.getTimeRangeHour(localTime);

            if (Objects.nonNull(timeRangeHour)) {
                timeRangeHour.increment(localTime);
            }
        }

        /** action 调用次数 共 [%d] 次 */
        private static final String dayTitle = Bundle.getMessage(MessageKey.timeRangeInOutDayTitle);

        @NonNull
        @Override
        public String toString() {

            String localDateFormat = TimeFormatKit.ofPattern("yyyy-MM-dd").format(this.localDate);

            List<TimeRangeHour> timeRangeHoursList = stream()
                    .filter(timeRangeHour -> timeRangeHour.count.sum() > 0)
                    .toList();

            if (CollKit.isEmpty(timeRangeHoursList)) {
                // TimeRange，action 暂无数据
                return localDateFormat + " action no data";
            }

            StringBuilder builder = new StringBuilder(localDateFormat);
            builder.append(" ").append(dayTitle.formatted(this.count.sum()));

            for (TimeRangeHour timeRangeHour : timeRangeHoursList) {
                builder.append("\n\t").append(timeRangeHour);
            }

            return builder.toString();
        }
    }

    /**
     * PluginInOut - TimeRangeInOut - Call statistics plugin for each time period - call statistics object for one hour
     *
     * @param hourTime   Hour
     * @param count      The number of action calls in one hour
     * @param minuteList Minute time ranges
     */
    public record TimeRangeHour(LocalTime hourTime, LongAdder count, List<TimeRangeMinute> minuteList) {

        public static TimeRangeHour create(int hour, List<TimeRangeMinute> minuteList) {
            var hourTime = LocalTime.of(hour, 0);
            return new TimeRangeHour(hourTime, new LongAdder(), minuteList);
        }

        void increment(LocalTime localTime) {
            this.count.increment();

            if (CollKit.isEmpty(this.minuteList)) {
                return;
            }

            int minute = localTime.getMinute();

            this.minuteList.stream()
                    .filter(timeRangeMinute -> timeRangeMinute.inRange(minute))
                    .findAny()
                    .ifPresent(TimeRangeMinute::increment);
        }

        public int getHour() {
            return this.hourTime.getHour();
        }

        /** %d:00 共 %s 次; */
        private static final String hourTitle = Bundle.getMessage(MessageKey.timeRangeInOutHourTitle);

        @NonNull
        @Override
        public String toString() {
            String hourStr = hourTitle.formatted(this.getHour(), this.count.sum());

            if (CollKit.isEmpty(this.minuteList)) {
                return hourStr;
            }

            StringBuilder builder = new StringBuilder();
            builder.append(hourStr);

            this.minuteList.stream()
                    .filter(timeRangeMinute -> timeRangeMinute.count.sum() > 0)
                    .forEach(timeRangeMinute -> builder.append(" - ").append(timeRangeMinute));

            return builder.toString();
        }
    }

    /**
     * PluginInOut - TimeRangeInOut - Call statistics plugin for each time period - recording in minute range
     *
     * @param start Start time (minute), inclusive
     * @param end   End time (minute), inclusive
     * @param count Number of executions triggered within this time range
     */
    public record TimeRangeMinute(int start, int end, LongAdder count) {
        /**
         * Create minute range record
         *
         * @param start Start time (minute), inclusive
         * @param end   End time (minute), inclusive
         * @return Minute range record
         */
        public static TimeRangeMinute create(int start, int end) {
            return new TimeRangeMinute(start, end, new LongAdder());
        }

        boolean inRange(int minute) {
            return minute >= this.start && minute <= this.end;
        }

        void increment() {
            this.count.increment();
        }

        /** [%d~%d分钟，%d 次] */
        private static final String minuteTitle = Bundle.getMessage(MessageKey.timeRangeInOutMinuteTitle);

        @NonNull
        @Override
        public String toString() {
            return minuteTitle.formatted(this.start, this.end, this.count.sum());
        }
    }

    /**
     * PluginInOut - TimeRangeInOut - Call statistics plugin for each time period - Listener
     */
    public interface ChangeListener {

        default void changed(TimeRangeDay timeRangeDay, LocalTime localTime, FlowContext flowContext) {
        }

        /**
         * The plugin will trigger the callbackYesterday method every day at 0:00, and pass yesterday's TimeRangeDay object into the method
         *
         * @param timeRangeYesterday Daily call statistics object (guaranteed not to be null)
         */
        default void callbackYesterday(TimeRangeDay timeRangeYesterday) {
        }

        /**
         * LocalDate now
         *
         * @return LocalDate
         */
        default LocalDate nowLocalDate() {
            return TimeKit.nowLocalDate();
        }

        /**
         * LocalTime now
         *
         * @return LocalTime
         */
        default LocalTime nowLocalTime() {
            return TimeKit.nowLocalTime();
        }

        /**
         * Create TimeRangeDay (Daily call statistics object)
         *
         * @param localDate Date
         * @return TimeRangeDay
         */
        default TimeRangeDay createTimeRangeDay(LocalDate localDate) {
            List<TimeRangeHour> timeRangeHourList = this.createListenerTimeRangeHourList();
            return TimeRangeDay.create(localDate, timeRangeHourList);
        }

        /**
         * create TimeRangeHour list, list of hour ranges to be statistically monitored
         *
         * @return list TimeRangeHour Daily call statistics object for one hour
         */
        default List<TimeRangeHour> createListenerTimeRangeHourList() {
            // Create data corresponding to 24 hours
            return IntStream.range(0, 24)
                    .mapToObj(this::createListenerTimeRangeHour)
                    .toList();
        }

        /**
         * create TimeRangeHour, the hour range to be statistically monitored
         *
         * @param hour Hour
         * @return Daily call statistics object for one hour
         */
        default TimeRangeHour createListenerTimeRangeHour(int hour) {
            List<TimeRangeMinute> timeRangeMinuteList = this.createListenerTimeRangeMinuteList();
            return TimeRangeHour.create(hour, timeRangeMinuteList);
        }

        /**
         * create TimeRangeMinute list, list of minute range records
         *
         * @return list Minute range record
         */
        default List<TimeRangeMinute> createListenerTimeRangeMinuteList() {
            return Collections.emptyList();
        }
    }
}