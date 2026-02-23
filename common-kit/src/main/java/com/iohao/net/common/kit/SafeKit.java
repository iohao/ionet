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
package com.iohao.net.common.kit;

import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Null-safe value retrieval utilities. Guarantees non-null return values by providing defaults.
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
@UtilityClass
public class SafeKit {
    /**
     * Return the int value, or 0 if {@code value} is null.
     *
     * @param value the nullable Integer
     * @return the unboxed int, or 0
     */
    public int getInt(Integer value) {
        return getInt(value, 0);
    }

    /**
     * Return the int value, or {@code defaultValue} if {@code value} is null.
     *
     * @param value        the nullable Integer
     * @param defaultValue the fallback value
     * @return the unboxed int, or {@code defaultValue}
     */
    public int getInt(Integer value, int defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * Parse the string as an int, or return {@code defaultValue} on failure.
     *
     * @param value        the string to parse
     * @param defaultValue the fallback value
     * @return the parsed int, or {@code defaultValue} if parsing fails
     */
    public int getInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Return the long value, or 0 if {@code value} is null.
     *
     * @param value the nullable Long
     * @return the unboxed long, or 0
     */
    public long getLong(Long value) {
        return getLong(value, 0);
    }

    /**
     * Return the long value, or {@code defaultValue} if {@code value} is null.
     *
     * @param value        the nullable Long
     * @param defaultValue the fallback value
     * @return the unboxed long, or {@code defaultValue}
     */
    public long getLong(Long value, long defaultValue) {
        return Objects.isNull(value) ? defaultValue : value;
    }

    /**
     * Parse the string as a long, or return {@code defaultValue} on failure.
     *
     * @param value        the string to parse
     * @param defaultValue the fallback value
     * @return the parsed long, or {@code defaultValue} if parsing fails
     */
    public long getLong(String value, long defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Return the boolean value, or {@code false} if {@code value} is null.
     *
     * @param value the nullable Boolean
     * @return the unboxed boolean, or {@code false}
     */
    public boolean getBoolean(Boolean value) {
        return getBoolean(value, false);
    }

    /**
     * Return the boolean value, or {@code defaultValue} if {@code value} is null.
     *
     * @param value        the nullable Boolean
     * @param defaultValue the fallback value
     * @return the unboxed boolean, or {@code defaultValue}
     */
    public boolean getBoolean(Boolean value, boolean defaultValue) {
        return Objects.isNull(value) ? defaultValue : value;
    }

    /**
     * Return the string value, or {@code defaultValue} if it is null or empty.
     *
     * @param value        the string to check
     * @param defaultValue the fallback value
     * @return the original string, or {@code defaultValue} if blank/null
     */
    public String getString(String value, String defaultValue) {
        if (StrKit.isEmpty(value)) {
            return defaultValue;
        }

        return value;
    }

    /**
     * Return the size of the list, or 0 if the list is null.
     *
     * @param list the nullable list
     * @return the list size, or 0
     */
    public int size(List<?> list) {
        return list == null ? 0 : list.size();
    }
}
