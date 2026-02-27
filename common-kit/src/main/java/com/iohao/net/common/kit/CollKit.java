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
import java.util.concurrent.*;
import lombok.experimental.*;

/**
 * Collection manipulation utilities.
 *
 * @author 渔民小镇
 * @date 2022-01-14
 */
@UtilityClass
public class CollKit {
    /**
     * Check whether the collection is not {@code null} and not empty.
     *
     * @param collection the collection to check
     * @return {@code true} if the collection contains at least one element
     */
    public boolean notEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * Check whether the collection is {@code null} or empty.
     *
     * @param collection the collection to check
     * @return {@code true} if the collection is {@code null} or contains no elements
     */
    public boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Create a new concurrent {@link Set} backed by a {@link ConcurrentHashMap}.
     *
     * @param <T> the element type
     * @return a new thread-safe set
     */
    public <T> Set<T> ofConcurrentSet() {
        return ConcurrentHashMap.newKeySet();
    }

    /**
     * Create a new {@link ConcurrentHashMap}.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return a new thread-safe map
     */
    public <K, V> Map<K, V> ofConcurrentHashMap() {
        return new ConcurrentHashMap<>();
    }
}
