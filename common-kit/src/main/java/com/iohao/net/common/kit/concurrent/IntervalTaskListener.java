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
package com.iohao.net.common.kit.concurrent;

/**
 * Scheduled task listener, using HashedWheelTimer to simulate ScheduledExecutorService scheduling.
 * <p>
 * {@code triggerUpdate and onUpdate} methods will only be executed when {@code isActive } returns true.
 * <p>
 * example
 * <pre>{@code
 * // Called every minute
 * TaskKit.runIntervalMinute(() -> log.info("tick 1 Minute"), 1);
 * // Called every 2 minutes
 * TaskKit.runIntervalMinute(() -> log.info("tick 2 Minute"), 2);
 *
 * // Called every 2 seconds
 * TaskKit.runInterval(() -> log.info("tick 2 Seconds"), 2, TimeUnit.SECONDS);
 * // Called every 30 minutes
 * TaskKit.runInterval(() -> log.info("tick 30 Minute"), 30, TimeUnit.MINUTES);
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-01
 * @see TaskKit
 */
public interface IntervalTaskListener extends CommonTaskListener {
    /**
     * Whether the task is active
     *
     * @return false means inactive, and the current TimerListener will be removed from the listener list
     */
    default boolean isActive() {
        return true;
    }
}