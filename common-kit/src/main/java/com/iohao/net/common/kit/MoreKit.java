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

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * General-purpose utility methods.
 *
 * @author 渔民小镇
 * @date 2023-12-07
 */
@UtilityClass
public class MoreKit {
    /**
     * Return the first non-null argument, or throw if both are null.
     *
     * @param <T>    the value type
     * @param first  the first candidate
     * @param second the second candidate
     * @return the first non-null value
     * @throws NullPointerException if both arguments are null
     */
    public <T> T firstNonNull(T first, T second) {
        if (first != null) {
            return first;
        }

        if (second != null) {
            return second;
        }

        throw new NullPointerException("Both are Null.");
    }

    /**
     * Put the value into the map if the key is absent, and return the effective value.
     * <p>
     * If the key already exists, the existing value is returned; otherwise the newly
     * inserted value is returned.
     *
     * @param <K>   the key type
     * @param <T>   the value type
     * @param map   the target map
     * @param key   the key to check
     * @param value the value to insert if absent
     * @return the existing value for the key, or {@code value} if newly inserted
     */
    public <K, T> T putIfAbsent(Map<K, T> map, K key, T value) {
        var first = map.putIfAbsent(key, value);
        return firstNonNull(first, value);
    }

    /**
     * Execute the runnable on the given executor, or run it on the current thread if
     * the executor is null.
     *
     * @param executor the executor to use, or null for direct execution
     * @param runnable the task to execute
     */
    public void execute(Executor executor, Runnable runnable) {
        if (executor == null) {
            runnable.run();
        } else {
            executor.execute(runnable);
        }
    }
}
