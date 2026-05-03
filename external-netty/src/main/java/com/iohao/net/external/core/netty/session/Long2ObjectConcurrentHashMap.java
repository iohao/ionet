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
package com.iohao.net.external.core.netty.session;

import java.util.*;
import java.util.concurrent.locks.*;
import org.agrona.collections.*;

/**
 * Lightweight long-key map wrapper with {@link StampedLock}-based concurrency control.
 * <p>
 * This type exists for hot session lookup paths where primitive {@code long} keys are used for user ids and
 * channel-scoped user ids. It keeps the Agrona {@link Long2ObjectHashMap} storage so lookups and updates avoid
 * boxing keys into {@link Long}, which helps reduce allocation pressure compared with {@code ConcurrentHashMap<Long, V>}.
 * </p>
 * <p>
 * {@link Long2ObjectHashMap} is not a concurrent collection, so every direct access to the backing map is protected by
 * the {@link StampedLock}. Read methods use a real read lock rather than optimistic reads because optimistic reads can
 * still touch the mutable backing arrays while a writer is changing them.
 * </p>
 * <p>
 * The {@link #values()} method returns a snapshot copy, allowing callers to iterate without holding this map's lock.
 * </p>
 *
 * @author 渔民小镇
 * @date 2025-09-13
 * @since 25.1
 */
@SuppressWarnings("all")
final class Long2ObjectConcurrentHashMap<V> {

    private final StampedLock lock = new StampedLock();
    private final Long2ObjectHashMap<V> map = new Long2ObjectHashMap<>();

    public V get(long key) {
        long stamp = lock.readLock();
        try {
            return map.get(key);
        } finally {
            lock.unlockRead(stamp);
        }
    }

    public V put(long key, V value) {
        long stamp = lock.writeLock();
        try {
            return map.put(key, value);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public V remove(long key) {
        long stamp = lock.writeLock();
        try {
            return map.remove(key);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public boolean remove(long key, V value) {
        long stamp = lock.writeLock();
        try {
            return map.remove(key, value);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public boolean containsKey(long key) {
        long stamp = lock.readLock();
        try {
            return map.containsKey(key);
        } finally {
            lock.unlockRead(stamp);
        }
    }

    public int size() {
        long stamp = lock.readLock();
        try {
            return map.size();
        } finally {
            lock.unlockRead(stamp);
        }
    }

    public List<V> values() {
        long stamp = lock.readLock();
        try {
            return new ArrayList<>(map.values());
        } finally {
            lock.unlockRead(stamp);
        }
    }
}
