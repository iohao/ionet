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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A map that associates each key with a collection of values.
 * Base interface for {@link ListMultiMap} and {@link SetMultiMap}.
 *
 * @author 渔民小镇
 * @date 2023-12-07
 */
interface MultiMap<K, V> {
    /**
     * The actual internal map implementation
     *
     * @return map
     */
    Map<K, ? extends Collection<V>> asMap();

    /**
     * Get an element collection by key, creating the collection if it does not exist.
     *
     * @param key key
     * @return collection, guaranteed to be non-null
     */
    Collection<V> of(K key);

    /**
     * Get an element collection
     *
     * @param key key
     * @return collection
     */
    Collection<V> get(K key);

    /**
     * Clear all elements within the collection corresponding to the key.
     *
     * @param key key
     * @return the collection after clearing, guaranteed to be non-null
     */
    default Collection<V> clearAll(K key) {
        var collection = this.of(key);

        collection.clear();

        return collection;
    }

    /**
     * The number of key-collection mappings in the map
     *
     * @return size
     */
    default int size() {
        return this.asMap().size();
    }

    /**
     * The total number of all values across all collections in the map.
     *
     * @return value size
     */
    default int sizeValue() {
        return this.asMap().values().stream()
                .mapToInt(Collection::size)
                .sum();
    }

    /**
     * Add an element to the collection associated with the specified key.
     *
     * @param key   key
     * @param value element
     * @return true if this collection changed as a result of the call
     */
    default boolean put(K key, V value) {
        var collection = this.of(key);
        return collection.add(value);
    }

    /**
     * Check whether this multimap contains no key-collection mappings.
     *
     * @return {@code true} if this multimap contains no mappings
     */
    default boolean isEmpty() {
        return this.asMap().isEmpty();
    }

    /**
     * Check whether this multimap contains a mapping for the specified key.
     *
     * @param key the key to look up
     * @return {@code true} if this multimap contains at least one mapping for the key
     */
    default boolean containsKey(K key) {
        return this.asMap().containsKey(key);
    }

    /**
     * Check whether any collection in this multimap contains the specified value.
     *
     * @param value the value to search for across all collections
     * @return {@code true} if the value is found in any collection
     */
    default boolean containsValue(V value) {
        for (Collection<V> vs : this.asMap().values()) {
            if (vs.contains(value)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Remove all key-collection mappings from this multimap.
     */
    default void clear() {
        this.asMap().clear();
    }

    /**
     * Return the set of keys that have at least one associated value.
     *
     * @return a set view of the keys contained in this multimap
     */
    default Set<K> keySet() {
        return this.asMap().keySet();
    }
}
