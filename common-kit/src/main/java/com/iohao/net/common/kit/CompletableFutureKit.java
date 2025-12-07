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
package com.iohao.net.common.kit;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * CompletableFuture Kit.
 * see <a href="https://nurkiewicz.com/2013/05/java-8-completablefuture-in-action.html">CompletableFuture</a>
 *
 * @author 渔民小镇
 * @date 2022-07-27
 */
@UtilityClass
public class CompletableFutureKit {
    static final CompletableFuture<?>[] EMPTY_ARRAY = new CompletableFuture[0];

    /**
     * Executes multiple CompletableFuture tasks in parallel and waits for their results.
     *
     * @param futures A list of CompletableFuture tasks.
     * @param <T>     The type of the result.
     * @return A list containing the results of all completed futures.
     */
    public <T> List<T> sequence(List<CompletableFuture<T>> futures) {
        // Combine all futures using allOf
        var allDoneFuture = CompletableFuture.allOf(futures.toArray(EMPTY_ARRAY));

        return allDoneFuture.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        ).join();
    }

    /**
     * Executes multiple CompletableFuture tasks in parallel and waits for their results.
     *
     * @param futures array of CompletableFuture tasks.
     * @param <T>     The type of the result.
     * @return A list containing the results of all completed futures.
     */
    public <T> List<T> sequence(CompletableFuture<T>[] futures) {
        // Combine all futures using allOf
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures);

        return allDoneFuture.thenApply(v ->
                Arrays.stream(futures)
                        .map(CompletableFuture::join)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        ).join();
    }

    /**
     * Executes multiple CompletableFuture tasks in parallel and returns a new CompletableFuture
     * that will be completed when all tasks are done.
     *
     * @param futures A list of CompletableFuture tasks.
     * @param <U>     The type of the result.
     * @return A CompletableFuture that will complete with a list of results.
     */
    public <U> CompletableFuture<List<U>> sequenceAsync(List<CompletableFuture<U>> futures) {
        return CompletableFuture.allOf(futures.toArray(EMPTY_ARRAY)).thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
    }
}
