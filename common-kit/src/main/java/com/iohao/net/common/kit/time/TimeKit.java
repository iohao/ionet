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

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.time.*;

/**
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

    public long toEpochMilli(LocalDateTime localDateTime) {
        return localDateTime.atZone(TimeKit.defaultZoneId)
                .toInstant()
                .toEpochMilli();
    }

    public LocalDateTime toLocalDateTime(long millis) {
        var instant = Instant.ofEpochMilli(millis);
        var zonedDateTime = instant.atZone(TimeKit.defaultZoneId);
        return zonedDateTime.toLocalDateTime();
    }

    public long elapsedMillis(long nanoTime) {
        return (System.nanoTime() - nanoTime) / 1_000_000;
    }

    public long elapsedMicros(long nanoTime) {
        return (System.nanoTime() - nanoTime) / 1_000;
    }

    /**
     * LocalDate EpochDay 过期检测，与当前时间做比较
     *
     * @param epochDay LocalDate epochDay
     * @return true 表示日期已经过期
     */
    public boolean expireLocalDate(long epochDay) {
        var localDate = TimeCacheKit.nowLocalDate();
        return localDate.toEpochDay() > epochDay;
    }

    /**
     * LocalDate 过期检测，与当前时间做比较
     *
     * @param localDate localDate
     * @return true 表示日期已经过期
     */
    public boolean expireLocalDate(LocalDate localDate) {
        return expireLocalDate(localDate.toEpochDay());
    }

    /**
     * 过期检测，与当前时间做比较，查看是否过期
     *
     * @param milli 需要检测的时间
     * @return true milliseconds 已经过期
     */
    public boolean expireMillis(long milli) {
        // 时间 - 当前时间
        return (milli - TimeKit.currentTimeMillis()) <= 0;
    }
}
