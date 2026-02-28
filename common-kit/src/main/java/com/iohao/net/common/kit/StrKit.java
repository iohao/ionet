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

import java.util.*;
import lombok.experimental.*;
import org.jspecify.annotations.*;

/**
 * String manipulation utilities.
 *
 * @author 渔民小镇
 * @date 2022-05-28
 */
@UtilityClass
public class StrKit {

    /**
     * Convert the first character of the string to upper case.
     *
     * @param value the source string
     * @return the string with its first character converted to upper case,
     *         or the original string if already upper case
     */
    public String firstCharToUpperCase(String value) {
        char firstChar = value.charAt(0);
        if (firstChar >= 'a' && firstChar <= 'z') {
            char[] arr = value.toCharArray();
            arr[0] -= 32;
            return String.valueOf(arr);
        }

        return value;
    }

    /**
     * Convert the first character of the string to lower case.
     *
     * @param value the source string
     * @return the string with its first character converted to lower case,
     *         or the original string if already lower case
     */
    public String firstCharToLowerCase(String value) {
        char firstChar = value.charAt(0);
        if (firstChar >= 'A' && firstChar <= 'Z') {
            char[] arr = value.toCharArray();
            arr[0] += 32;
            return String.valueOf(arr);
        }

        return value;
    }

    /**
     * Check whether the string is {@code null} or blank (whitespace only).
     *
     * @param str the string to check
     * @return {@code true} if the string is {@code null} or blank
     */
    public boolean isEmpty(String str) {
        return str == null || str.isBlank();
    }

    /**
     * Check whether the character sequence is {@code null} or empty.
     *
     * @param str the character sequence to check
     * @return {@code true} if the sequence is {@code null} or empty
     */
    public boolean isEmpty(CharSequence str) {
        return str == null || str.isEmpty();
    }

    /**
     * Check whether the string is neither {@code null} nor blank.
     *
     * @param str the string to check
     * @return {@code true} if the string is not {@code null} and not blank
     */
    public boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Replace {@code {key}} placeholders in the template with corresponding map values.
     *
     * <p>Entries whose value is {@code null} are skipped. All occurrences of each
     * placeholder are replaced.
     *
     * @param template the template string containing {@code {key}} placeholders
     * @param map      the key-value pairs used for substitution
     * @return the formatted string with placeholders replaced
     */
    public String format(@NonNull String template, @NonNull Map<?, ?> map) {
        if (template.isEmpty() || map.isEmpty()) {
            return template;
        }

        var result = new StringBuilder(template);

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            var value = entry.getValue();
            if (value == null) {
                continue;
            }

            String placeholder = "{" + entry.getKey() + "}";
            String stringValue = value.toString();
            int index = result.indexOf(placeholder);

            while (index != -1) {
                result.replace(index, index + placeholder.length(), stringValue);
                index = result.indexOf(placeholder, index + stringValue.length());
            }
        }

        return result.toString();
    }
}