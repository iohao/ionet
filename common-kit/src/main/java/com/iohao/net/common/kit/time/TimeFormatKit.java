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

import com.iohao.net.common.kit.CollKit;
import com.iohao.net.common.kit.MoreKit;
import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Map;
import java.util.Objects;

/**
 * Date/time formatting utilities with cached {@link java.time.format.DateTimeFormatter} instances.
 *
 * @author 渔民小镇
 * @date 2024-08-27
 * @since 21.16
 */
@UtilityClass
public final class TimeFormatKit {
    private final Map<String, DateTimeFormatter> map = CollKit.ofConcurrentHashMap();
    private final DateTimeFormatter defaultFormatter = ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * get singleton DateTimeFormatter by pattern
     *
     * @param pattern the pattern to use, not null
     * @return the formatter based on the pattern, not null
     */
    public DateTimeFormatter ofPattern(String pattern) {
        var dateTimeFormatter = map.get(pattern);
        if (Objects.isNull(dateTimeFormatter)) {
            return MoreKit.putIfAbsent(map, pattern, DateTimeFormatter.ofPattern(pattern));
        }

        return dateTimeFormatter;
    }

    /**
     * Format the current date/time as {@code yyyy-MM-dd HH:mm:ss}.
     *
     * @return the formatted current date/time string
     */
    public String format() {
        return format(TimeKit.nowLocalDateTime());
    }

    /**
     * Format the given epoch milliseconds as {@code yyyy-MM-dd HH:mm:ss}.
     *
     * @param timeMillis the epoch milliseconds to format
     * @return the formatted date/time string
     */
    public String format(long timeMillis) {
        var localDateTime = TimeKit.toLocalDateTime(timeMillis);
        return format(localDateTime);
    }

    /**
     * Format the given temporal as {@code yyyy-MM-dd HH:mm:ss}.
     *
     * @param temporal the temporal to format
     * @return the formatted date/time string
     */
    public String format(TemporalAccessor temporal) {
        return defaultFormatter.format(temporal);
    }

    /**
     * Format the given temporal using the specified pattern.
     *
     * @param temporal the temporal to format
     * @param pattern  the date/time pattern to use
     * @return the formatted date/time string
     */
    public String format(TemporalAccessor temporal, String pattern) {
        return ofPattern(pattern).format(temporal);
    }
}
