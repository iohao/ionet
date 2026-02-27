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

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import lombok.experimental.*;

/**
 * Development and performance monitoring utilities for measuring IPC latency and request
 * processing times. Internal use only.
 *
 * @author 渔民小镇
 * @date 2025-11-06
 * @since 25.1
 */
@UtilityClass
@SuppressWarnings("all")
public final class DevKit {
    public long nanoTimeStart;

    public AtomicBoolean devEnterprise = new AtomicBoolean();

    public BlockingQueue<Long> requestOnFragmentTimes = new LinkedBlockingQueue<>();
    public BlockingQueue<Long> handleTimes = new LinkedBlockingQueue<>();
    public BlockingQueue<Long> callTimes = new LinkedBlockingQueue<>();
    public BlockingQueue<Long> ofFutureTimes = new LinkedBlockingQueue<>();
    public BlockingQueue<Long> ofFutureGetTimes = new LinkedBlockingQueue<>();
    public BlockingQueue<Long> requestSbeTimes = new LinkedBlockingQueue<>();
    public BlockingQueue<Long> responseSbeTimes = new LinkedBlockingQueue<>();
    public LongAdder avgMicrosTime = new LongAdder();
    public boolean print = true;
    public IpcLatencyMonitor monitor = new IpcLatencyMonitor();
    public int userResponseMessageOnFragmentInc;
    public int externalId;

    /**
     * Record SBE request encoding time elapsed since the given nano time.
     *
     * @param nanoTime the starting nano time
     */
    public void requestSbeTimes(long nanoTime) {
        requestSbeTimes.offer(System.nanoTime() - nanoTime);
    }

    /**
     * Record SBE response encoding time elapsed since the given nano time.
     *
     * @param nanoTime the starting nano time
     */
    public void responseSbeTimes(long nanoTime) {
        responseSbeTimes.offer(System.nanoTime() - nanoTime);
    }

    /**
     * Record future creation time elapsed since the given nano time.
     *
     * @param nanoTime the starting nano time
     */
    public void ofFutureTimes(long nanoTime) {
        ofFutureTimes.offer(System.nanoTime() - nanoTime);
    }

    /**
     * Record future get time elapsed since the given nano time.
     *
     * @param nanoTime the starting nano time
     */
    public void ofFutureGetTimes(long nanoTime) {
        ofFutureGetTimes.offer(System.nanoTime() - nanoTime);
    }

    /**
     * Record Aeron fragment receive time elapsed since the given nano time.
     *
     * @param nanoTime the starting nano time
     */
    public void requestOnFragmentTimes(long nanoTime) {
        requestOnFragmentTimes.offer(System.nanoTime() - nanoTime);
    }

    /**
     * Record request handling time elapsed since the given nano time.
     *
     * @param nanoTime the starting nano time
     */
    public void handleTimes(long nanoTime) {
        handleTimes.offer(System.nanoTime() - nanoTime);
    }

    /**
     * Record cross-logic-server call time elapsed since the given nano time.
     *
     * @param nanoTime the starting nano time
     */
    public void callTimes(long nanoTime) {
        callTimes.offer(System.nanoTime() - nanoTime);
    }

    /** Reset all collected timing data. */
    public void reset() {
        ofFutureTimes.clear();
        requestSbeTimes.clear();
        ofFutureGetTimes.clear();

        requestOnFragmentTimes.clear();
        handleTimes.clear();
        responseSbeTimes.clear();


        callTimes.clear();
        avgMicrosTime.reset();
        monitor.reset();
    }

    /**
     * Format timing data as a human-readable statistics panel.
     *
     * @param title the panel title
     * @param times the collected timing values in nanoseconds
     * @return formatted statistics string
     */
    public String toString(String title, BlockingQueue<Long> times) {
        return new DataPanel(title, times).toString();
    }

    private static class DataPanel {
        final String title;
        final BlockingQueue<Long> times;
        long sumTime;

        DataPanel(String title, BlockingQueue<Long> times) {
            this.title = title;
            this.times = times;
        }

        @Override
        public String toString() {

            int size = times.size();

            Long value;
            while ((value = times.poll()) != null) {
                sumTime += value;
            }

            avgMicrosTime.add(sumTime / size / 1_000);

            return """
                    title: %s
                    size: %s
                    sumTime: %,d
                    avgMicros: %,d
                    avgMillis: %,d
                    -----------------------
                    """.formatted(title
                    , size
                    , sumTime
                    , sumTime / size / 1_000
                    , sumTime / size / 1_000_000
            );
        }
    }
}
