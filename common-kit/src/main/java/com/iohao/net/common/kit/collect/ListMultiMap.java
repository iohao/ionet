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
package com.iohao.net.common.kit.collect;

import java.util.*;
import java.util.function.*;

/**
 * A thread-safe multimap that associates each key with a {@link java.util.List} of values, allowing duplicates.
 * <p>
 * for example
 * <pre>{@code
 * ListMultiMap<Integer, String> map = ListMultiMap.of();
 * map.put(1, "a");
 * map.put(1, "a");
 * map.put(1, "b");
 *
 * List<String> list2 = map.get(2); // is null
 * List<String> list2 = map.of(2); // is empty list
 *
 * list2.add("2 - a");
 * list2.add("2 - a");
 * map.sizeValue(); // sizeValue == 5
 *
 * map.containsValue("a"); // true
 * map.containsValue("b"); // true
 *
 * Set<Integer> keySet = this.map.keySet();
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-07
 */
public interface ListMultiMap<K, V> extends MultiMap<K, V> {
    @Override
    Map<K, List<V>> asMap();

    /**
     * Get an element collection by key, creating the collection if it does not exist.
     * <pre>
     * The returned element collection is guaranteed to be non-null; a new one will be created if absent.
     *
     * The first time the collection for a key is created, the consumer will be called with the newly created collection.
     *
     * Developers can use the consumer for any necessary initialization.
     * </pre>
     *
     * @param key      key
     * @param consumer consumer
     * @return the collection
     */
    List<V> ofIfAbsent(K key, Consumer<List<V>> consumer);

    @Override
    default List<V> of(K key) {
        return this.ofIfAbsent(key, null);
    }

    @Override
    default List<V> get(K key) {
        return asMap().get(key);
    }

    /**
     * Return a set view of the key-list mappings contained in this multimap.
     *
     * @return a set of map entries where each value is a {@link List} of elements
     */
    default Set<Map.Entry<K, List<V>>> entrySet() {
        return this.asMap().entrySet();
    }

    /**
     * Create a ListMultiMap (framework internal implementation). Please use {@link ListMultiMap#of()} instead.
     *
     * @param <K> k
     * @param <V> v
     * @return ListMultiMap
     */
    static <K, V> ListMultiMap<K, V> create() {
        return of();
    }

    /**
     * Create a ListMultiMap (framework internal implementation)
     *
     * @param <K> k
     * @param <V> v
     * @return ListMultiMap
     */
    static <K, V> ListMultiMap<K, V> of() {
        return new NonBlockingListMultiMap<>();
    }
}