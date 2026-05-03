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

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Long2ObjectConcurrentHashMap} concurrent access behavior.
 *
 * @author 渔民小镇
 * @date 2026-05-03
 */
public class Long2ObjectConcurrentHashMapTest {

    @Test
    public void concurrentReadWriteShouldNotFail() throws Exception {
        var map = new Long2ObjectConcurrentHashMap<String>();
        int taskCount = 8;
        int loopCount = 5_000;
        var ready = new CountDownLatch(taskCount);
        var start = new CountDownLatch(1);
        var done = new CountDownLatch(taskCount);
        var failure = new AtomicReference<Throwable>();
        var executorService = Executors.newFixedThreadPool(taskCount);

        try {
            for (int i = 0; i < taskCount; i++) {
                int taskIndex = i;
                executorService.execute(() -> {
                    ready.countDown();
                    try {
                        start.await();
                        for (int j = 0; j < loopCount; j++) {
                            long key = taskIndex * (long) loopCount + j;
                            map.put(key, "v" + key);
                            map.get(key);
                            map.containsKey(key);
                            map.size();
                            if ((j & 1) == 0) {
                                map.remove(key);
                            }
                        }
                    } catch (Throwable e) {
                        failure.compareAndSet(null, e);
                    } finally {
                        done.countDown();
                    }
                });
            }

            assertTrue(ready.await(1, TimeUnit.SECONDS));
            start.countDown();
            assertTrue(done.await(5, TimeUnit.SECONDS));
            assertNull(failure.get());
        } finally {
            executorService.shutdownNow();
        }
    }

    @Test
    public void valuesShouldReturnStableSnapshot() {
        var map = new Long2ObjectConcurrentHashMap<String>();
        map.put(1, "a");
        map.put(2, "b");

        var values = map.values();
        map.put(3, "c");

        assertEquals(2, values.size());
        assertTrue(values.contains("a"));
        assertTrue(values.contains("b"));
        assertFalse(values.contains("c"));
    }

    @Test
    public void removeWithValueShouldOnlyRemoveExpectedMapping() {
        var map = new Long2ObjectConcurrentHashMap<Object>();
        var value = new Object();
        var otherValue = new Object();
        map.put(1, value);

        assertFalse(map.remove(1, otherValue));
        assertSame(value, map.get(1));
        assertTrue(map.remove(1, value));
        assertNull(map.get(1));
    }
}
