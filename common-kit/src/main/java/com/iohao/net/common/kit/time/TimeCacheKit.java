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
package com.iohao.net.common.kit.time;

import com.iohao.net.common.kit.concurrent.ExecutorKit;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 日期与时间的缓存工具，当开启缓存后，可减少时间相关对象的创建，但也会损失一些精度；缓存默认是关闭的，默认情况下使用实时数据。
 * 如果对时间要求不需要很精准的，建议启用。
 *
 * @author 渔民小镇
 * @date 2024-08-27
 * @since 21.16
 */
@UtilityClass
final class TimeCacheKit {
    @Setter
    volatile LocalTime localTime = LocalTime.now();
    @Setter
    volatile LocalDate localDate = LocalDate.now();
    @Setter
    volatile LocalDateTime localDateTime = LocalDateTime.now();
    @Setter
    volatile long currentTimeMillis = System.currentTimeMillis();
    @Setter
    volatile long currentNanoTime = System.nanoTime();

    boolean cache;

    ScheduledExecutorService scheduler;
    @Setter
    Runnable cacheTimeUpdateStrategy = () -> {
        var threadFactory = ExecutorKit.createSigleThreadFactory("TimeCacheKit");
        scheduler = ExecutorKit.newSingleScheduled(threadFactory);

        scheduler.scheduleAtFixedRate(() -> localDateTime = LocalDateTime.now(), 0, 1, TimeUnit.SECONDS);

        scheduler.scheduleAtFixedRate(() -> {
            localDate = LocalDate.now();
            localTime = LocalTime.now();
        }, 0, 1, TimeUnit.MINUTES);

        scheduler.scheduleAtFixedRate(() -> {
            currentTimeMillis = System.currentTimeMillis();
            currentNanoTime = System.nanoTime();
        }, 0, 1, TimeUnit.MILLISECONDS);
    };

    void enableCache() {
        if (!cache) {
            cache = true;
            cacheTimeUpdateStrategy.run();
        }
    }

    /**
     * get LocalDate
     *
     * @return LocalDate
     */
    LocalDate nowLocalDate() {
        return cache ? localDate : LocalDate.now();
    }

    /**
     * get LocalDateTime
     *
     * @return LocalDateTime
     */
    LocalDateTime nowLocalDateTime() {
        return cache ? localDateTime : LocalDateTime.now();
    }

    /**
     * get LocalTime
     *
     * @return LocalTime
     */
    LocalTime nowLocalTime() {
        return cache ? localTime : LocalTime.now();
    }

    /**
     * get currentTimeMillis
     *
     * @return System.currentTimeMillis()
     */
    long currentTimeMillis() {
        return cache ? currentTimeMillis : System.currentTimeMillis();
    }

    long currentNanoTime() {
        return cache ? currentNanoTime : System.nanoTime();
    }
}
