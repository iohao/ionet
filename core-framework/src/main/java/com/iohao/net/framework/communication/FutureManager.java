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

import com.iohao.net.framework.protocol.FutureMessage;

import java.util.concurrent.CompletableFuture;

/**
 * Manage the lifecycle of {@link CompletableFuture} instances used for asynchronous request-response
 * communication between servers.
 * <p>
 * Each future is identified by a unique ID generated via {@link #nextFutureId()}. Futures are
 * created and registered with {@link #ofFuture(long)}, completed when a response arrives via
 * {@link #complete(FutureMessage)}, and can be cleaned up on timeout via {@link #cleanTimeouts()}.
 *
 * @author 渔民小镇
 * @date 2025-09-04
 * @since 25.1
 */
public interface FutureManager {
    /**
     * Generate the next unique future identifier.
     *
     * @return a monotonically increasing future ID
     */
    long nextFutureId();

    /**
     * Create and register a new {@link CompletableFuture} associated with the given ID.
     *
     * @param futureId the unique identifier for the future
     * @param <T>      the expected result type
     * @return a new completable future bound to the given ID
     */
    <T> CompletableFuture<T> ofFuture(long futureId);

    /**
     * Remove and return the {@link CompletableFuture} associated with the given ID.
     *
     * @param futureId the unique identifier of the future to remove
     * @param <T>      the expected result type
     * @return the removed future, or {@code null} if no future was registered for the ID
     */
    <T> CompletableFuture<T> remove(long futureId);

    /**
     * Clean up any futures that have exceeded their timeout threshold.
     * <p>
     * The default implementation is a no-op; concrete implementations may override
     * to perform periodic housekeeping.
     */
    default void cleanTimeouts() {
    }

    /**
     * Complete the future matching the given message's future ID with the message itself.
     * <p>
     * If no future is found for the ID (e.g., it already timed out), the message is silently discarded.
     *
     * @param message the response message containing the future ID and result data
     */
    default void complete(FutureMessage message) {
        long futureId = message.getFutureId();
        var future = remove(futureId);
        if (future != null) {
            future.complete(message);
        }
    }
}
