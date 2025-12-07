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
import org.jspecify.annotations.NonNull;

import java.util.Map;

/**
 * @author 渔民小镇
 * @date 2022-05-28
 */
@UtilityClass
public class StrKit {

    public String firstCharToUpperCase(String value) {
        char firstChar = value.charAt(0);
        if (firstChar >= 'a' && firstChar <= 'z') {
            char[] arr = value.toCharArray();
            arr[0] -= 32;
            return String.valueOf(arr);
        }

        return value;
    }

    public String firstCharToLowerCase(String value) {
        char firstChar = value.charAt(0);
        if (firstChar >= 'A' && firstChar <= 'Z') {
            char[] arr = value.toCharArray();
            arr[0] += 32;
            return String.valueOf(arr);
        }

        return value;
    }

    public boolean isEmpty(String str) {
        return str == null || str.isBlank();
    }

    public boolean isEmpty(CharSequence str) {
        return str == null || str.isEmpty();
    }

    public boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

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
            int index = result.indexOf(placeholder);

            while (index != -1) {
                result.replace(index, index + placeholder.length(), value.toString());
                index = result.indexOf(placeholder, index + value.toString().length());
            }
        }

        return result.toString();
    }
}