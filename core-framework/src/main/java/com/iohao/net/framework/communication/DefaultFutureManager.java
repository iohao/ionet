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
package com.iohao.net.framework.communication;

import com.iohao.net.framework.CoreGlobalConfig;
import com.iohao.net.common.kit.CollKit;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * DefaultFutureManager
 *
 * @author 渔民小镇
 * @date 2025-09-16
 * @since 25.1
 */
public final class DefaultFutureManager implements FutureManager {
    final Map<Long, CompletableFuture<?>> futureMap = CollKit.ofConcurrentHashMap();
    final AtomicLong idGenerator = new AtomicLong(1);
    final long futureTimeoutMillis;

    public DefaultFutureManager() {
        futureTimeoutMillis = CoreGlobalConfig.getFutureTimeoutMillis();
    }

    public long nextFutureId() {
        return idGenerator.getAndIncrement();
    }

    public <T> CompletableFuture<T> ofFuture(long futureId) {
        var future = new CompletableFuture<T>();
        futureMap.put(futureId, future);

        future.orTimeout(futureTimeoutMillis, TimeUnit.MILLISECONDS).exceptionally(ex -> {
            if (ex instanceof TimeoutException) {
                futureMap.remove(futureId);
            }

            return null;
        });

        return future;
    }

    @SuppressWarnings("unchecked")
    public <T> CompletableFuture<T> remove(long futureId) {
        return (CompletableFuture<T>) futureMap.remove(futureId);
    }
}