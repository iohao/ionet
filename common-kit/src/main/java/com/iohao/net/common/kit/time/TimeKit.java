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
package com.iohao.net.common.kit.time;

import java.time.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Time utilities providing cached or real-time date/time access.
 *
 * @author 渔民小镇
 * @date 2025-09-09
 * @since 25.1
 */
@UtilityClass
public final class TimeKit {
    @Getter
    @Setter
    ZoneId defaultZoneId = ZoneId.systemDefault();

    /**
     * Enable time caching to reduce object creation at the cost of precision.
     */
    public void enableCache() {
        TimeCacheKit.enableCache();
    }

    /**
     * get currentTimeMillis
     *
     * @return System.currentTimeMillis()
     */
    public long currentTimeMillis() {
        return TimeCacheKit.currentTimeMillis();
    }

    /**
     * Get the current nano time, cached if caching is enabled.
     *
     * @return the current nano time
     */
    public long currentNanoTime() {
        return TimeCacheKit.currentNanoTime();
    }

    /**
     * get LocalDate
     *
     * @return LocalDate
     */
    public LocalDate nowLocalDate() {
        return TimeCacheKit.nowLocalDate();
    }

    /**
     * get LocalDateTime
     *
     * @return LocalDateTime
     */
    public LocalDateTime nowLocalDateTime() {
        return TimeCacheKit.nowLocalDateTime();
    }

    /**
     * get LocalTime
     *
     * @return LocalTime
     */
    public LocalTime nowLocalTime() {
        return TimeCacheKit.nowLocalTime();
    }

    /**
     * Convert a LocalDateTime to epoch milliseconds using the default zone.
     *
     * @param localDateTime the local date/time to convert
     * @return the epoch milliseconds
     */
    public long toEpochMilli(LocalDateTime localDateTime) {
        return localDateTime.atZone(TimeKit.defaultZoneId)
                .toInstant()
                .toEpochMilli();
    }

    /**
     * Convert epoch milliseconds to a LocalDateTime using the default zone.
     *
     * @param millis the epoch milliseconds
     * @return the corresponding LocalDateTime
     */
    public LocalDateTime toLocalDateTime(long millis) {
        var instant = Instant.ofEpochMilli(millis);
        var zonedDateTime = instant.atZone(TimeKit.defaultZoneId);
        return zonedDateTime.toLocalDateTime();
    }

    /**
     * Calculate elapsed milliseconds since the given nano time.
     *
     * @param nanoTime the starting nano time
     * @return the elapsed time in milliseconds
     */
    public long elapsedMillis(long nanoTime) {
        return (System.nanoTime() - nanoTime) / 1_000_000;
    }

    /**
     * Calculate elapsed microseconds since the given nano time.
     *
     * @param nanoTime the starting nano time
     * @return the elapsed time in microseconds
     */
    public long elapsedMicros(long nanoTime) {
        return (System.nanoTime() - nanoTime) / 1_000;
    }

    /**
     * Check if the given epoch day has expired compared to the current date.
     *
     * @param epochDay the epoch day to check
     * @return {@code true} if the date has expired
     */
    public boolean expireLocalDate(long epochDay) {
        var localDate = TimeCacheKit.nowLocalDate();
        return localDate.toEpochDay() > epochDay;
    }

    /**
     * Check if the given LocalDate has expired compared to the current date.
     *
     * @param localDate the date to check
     * @return {@code true} if the date has expired
     */
    public boolean expireLocalDate(LocalDate localDate) {
        return expireLocalDate(localDate.toEpochDay());
    }

    /**
     * Check if the given timestamp in milliseconds has expired.
     *
     * @param milli the timestamp in milliseconds to check
     * @return {@code true} if the timestamp has expired
     */
    public boolean expireMillis(long milli) {
        // time - current time
        return (milli - TimeKit.currentTimeMillis()) <= 0;
    }
}
