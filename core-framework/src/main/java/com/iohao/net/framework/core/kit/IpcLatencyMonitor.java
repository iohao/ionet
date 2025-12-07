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
package com.iohao.net.framework.core.kit;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.LongAdder;

/**
 * Test Aeron IPC 延迟监控器。
 *
 * @author 渔民小镇
 * @date 2025-11-08
 * @since 25.1
 */
@Slf4j
public final class IpcLatencyMonitor {
    // 0~9999 微秒的分布桶
    private final int MAX_BUCKETS = 10000;
    private final AtomicLongArray buckets = new AtomicLongArray(MAX_BUCKETS);
    private final LongAdder totalCount = new LongAdder();
    private final LongAdder totalMicros = new LongAdder();
    private final AtomicLong maxMicros = new AtomicLong(0L);

    /** 记录一次 IPC 延迟（单位：微秒） */
    public void record(long micros) {
        if (micros <= 0) return;

        totalCount.increment();
        totalMicros.add(micros);
        updateMax(micros);

        int index = (int) Math.min(micros, MAX_BUCKETS - 1);
        buckets.incrementAndGet(index);
    }

    private void updateMax(long micros) {
        long prev;
        while (true) {
            prev = maxMicros.get();
            if (micros <= prev) {
                return;
            }
            if (maxMicros.compareAndSet(prev, micros)) {
                return;
            }
            // otherwise retry
        }
    }

    /** 打印统计结果 */
    public void printStats(String title) {
        long count = totalCount.sum();
        if (count == 0) {
            log.info("[{}] 无统计数据", title);
            return;
        }

        long avg = totalMicros.sum() / count;
        long max = maxMicros.get();
        long p95 = percentile(95);
        long p99 = percentile(99);

        log.info("""
                        [{}] IPC 延迟统计：
                        总次数: {}
                        平均: {} µs
                        P95: {} µs
                        P99: {} µs
                        最大: {} µs
                        """,
                title, count, avg, p95, p99, max
        );
    }

    /** 计算百分位延迟（简单桶累加法） */
    private long percentile(int percent) {
        long count = totalCount.sum();
        if (count == 0) return 0;

        // 目标序号（1-based），确保至少为 1
        long target = Math.max(1, (count * percent + 99) / 100); // 向上取整的近似
        long cumulative = 0;
        for (int i = 0; i < MAX_BUCKETS; i++) {
            cumulative += buckets.get(i);
            if (cumulative >= target) return i;
        }
        return MAX_BUCKETS - 1;
    }

    /** 重置统计数据 */
    public void reset() {
        for (int i = 0; i < MAX_BUCKETS; i++) {
            buckets.set(i, 0L);
        }

        totalCount.reset();
        totalMicros.reset();
        maxMicros.set(0L);
    }
}