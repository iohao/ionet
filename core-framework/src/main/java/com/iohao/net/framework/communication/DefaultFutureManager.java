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

import com.iohao.net.common.kit.*;
import com.iohao.net.framework.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Default implementation of {@link FutureManager} backed by a concurrent map.
 * <p>
 * Manages pending {@link CompletableFuture} instances keyed by auto-incremented IDs.
 * Each future is automatically timed out after the configured timeout period
 * (from {@link CoreGlobalConfig#getFutureTimeoutMillis()}).
 *
 * @author 渔民小镇
 * @date 2025-09-16
 * @since 25.1
 */
public final class DefaultFutureManager implements FutureManager {
    final Map<Long, CompletableFuture<?>> futureMap = CollKit.ofConcurrentHashMap();
    final AtomicLong idGenerator = new AtomicLong(1);
    final long futureTimeoutMillis;

    /** Create a new manager using the global future timeout configuration. */
    public DefaultFutureManager() {
        futureTimeoutMillis = CoreGlobalConfig.getFutureTimeoutMillis();
    }

    /**
     * Generate the next unique future ID.
     *
     * @return a monotonically increasing future ID
     */
    public long nextFutureId() {
        return idGenerator.getAndIncrement();
    }

    /**
     * Create and register a new {@link CompletableFuture} for the given ID.
     * <p>
     * The future will automatically time out and be removed from the map
     * after {@code futureTimeoutMillis} milliseconds.
     *
     * @param futureId the unique ID to associate with the future
     * @param <T>      the expected result type
     * @return the newly created future
     */
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

    /**
     * Remove and return the future associated with the given ID.
     *
     * @param futureId the ID of the future to remove
     * @param <T>      the expected result type
     * @return the removed future, or {@code null} if no future was registered for the ID
     */
    @SuppressWarnings("unchecked")
    public <T> CompletableFuture<T> remove(long futureId) {
        return (CompletableFuture<T>) futureMap.remove(futureId);
    }
}