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

import com.iohao.net.common.kit.exception.*;
import com.iohao.net.framework.annotations.*;
import com.iohao.net.framework.core.*;
import com.iohao.net.framework.protocol.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

/**
 * Decorator providing cross-logic-server collect-style calls that aggregate responses from multiple logic servers.
 * <p>All methods in this interface are enterprise-only and throw {@link EnterpriseSupportException} by default.
 * The enterprise implementation overrides these defaults with actual collect-call logic.
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
@Enterprise
public interface LogicCallCollectCommunicationDecorator extends CommonDecorator, LogicCommunicationDecorator {

    //*********************** callCollect ***********************//

    /** Synchronous collect-call using a pre-built request message. @see #callCollect(CmdInfo, byte[]) */
    default ResponseCollect callCollect(RequestMessage message) {
        return this.getCommunicationAggregation().callCollect(message);
    }

    /**
     * Call all logic servers that handle the given command and collect their responses synchronously.
     *
     * @param cmdInfo the target command route
     * @param data    the raw byte payload
     * @return the aggregated response from all matching logic servers
     */
    default ResponseCollect callCollect(CmdInfo cmdInfo, byte[] data) {
        throw new EnterpriseSupportException();
    }

    /** Synchronous collect-call with no payload. @see #callCollect(CmdInfo, byte[]) */
    default ResponseCollect callCollect(CmdInfo cmdInfo) {
        throw new EnterpriseSupportException();
    }

    /** Synchronous collect-call with int payload. @see #callCollect(CmdInfo, byte[]) */
    default ResponseCollect callCollect(CmdInfo cmdInfo, int data) {
        throw new EnterpriseSupportException();
    }

    /** Synchronous collect-call with long payload. @see #callCollect(CmdInfo, byte[]) */
    default ResponseCollect callCollect(CmdInfo cmdInfo, long data) {
        throw new EnterpriseSupportException();
    }

    /** Synchronous collect-call with boolean payload. @see #callCollect(CmdInfo, byte[]) */
    default ResponseCollect callCollect(CmdInfo cmdInfo, boolean data) {
        throw new EnterpriseSupportException();
    }

    /** Synchronous collect-call with String payload. @see #callCollect(CmdInfo, byte[]) */
    default ResponseCollect callCollect(CmdInfo cmdInfo, String data) {
        throw new EnterpriseSupportException();
    }

    /** Synchronous collect-call with Object payload. @see #callCollect(CmdInfo, byte[]) */
    default ResponseCollect callCollect(CmdInfo cmdInfo, Object data) {
        throw new EnterpriseSupportException();
    }

    /** Synchronous collect-call with List payload. @see #callCollect(CmdInfo, byte[]) */
    default ResponseCollect callCollect(CmdInfo cmdInfo, List<?> dataList) {
        throw new EnterpriseSupportException();
    }

    /** Synchronous collect-call with List&lt;Integer&gt; payload. @see #callCollect(CmdInfo, byte[]) */
    default ResponseCollect callCollectListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        throw new EnterpriseSupportException();
    }

    /** Synchronous collect-call with List&lt;Long&gt; payload. @see #callCollect(CmdInfo, byte[]) */
    default ResponseCollect callCollectListLong(CmdInfo cmdInfo, List<Long> dataList) {
        throw new EnterpriseSupportException();
    }

    /** Synchronous collect-call with List&lt;Boolean&gt; payload. @see #callCollect(CmdInfo, byte[]) */
    default ResponseCollect callCollectListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        throw new EnterpriseSupportException();
    }

    /** Synchronous collect-call with List&lt;String&gt; payload. @see #callCollect(CmdInfo, byte[]) */
    default ResponseCollect callCollectListString(CmdInfo cmdInfo, List<String> dataList) {
        throw new EnterpriseSupportException();
    }

    //*********************** callCollect future ***********************//

    /** Future-based collect-call using a pre-built request message. @see #callCollectFuture(CmdInfo, byte[]) */
    default CompletableFuture<ResponseCollect> callCollectFuture(RequestMessage message) {
        throw new EnterpriseSupportException();
    }

    /**
     * Call all logic servers that handle the given command and return a CompletableFuture for the aggregated response.
     *
     * @param cmdInfo the target command route
     * @param data    the raw byte payload
     * @return a {@link CompletableFuture} that completes with the aggregated response
     */
    default CompletableFuture<ResponseCollect> callCollectFuture(CmdInfo cmdInfo, byte[] data) {
        throw new EnterpriseSupportException();
    }

    /** Future-based collect-call with no payload. @see #callCollectFuture(CmdInfo, byte[]) */
    default CompletableFuture<ResponseCollect> callCollectFuture(CmdInfo cmdInfo) {
        throw new EnterpriseSupportException();
    }

    /** Future-based collect-call with int payload. @see #callCollectFuture(CmdInfo, byte[]) */
    default CompletableFuture<ResponseCollect> callCollectFuture(CmdInfo cmdInfo, int data) {
        throw new EnterpriseSupportException();
    }

    /** Future-based collect-call with long payload. @see #callCollectFuture(CmdInfo, byte[]) */
    default CompletableFuture<ResponseCollect> callCollectFuture(CmdInfo cmdInfo, long data) {
        throw new EnterpriseSupportException();
    }

    /** Future-based collect-call with boolean payload. @see #callCollectFuture(CmdInfo, byte[]) */
    default CompletableFuture<ResponseCollect> callCollectFuture(CmdInfo cmdInfo, boolean data) {
        throw new EnterpriseSupportException();
    }

    /** Future-based collect-call with String payload. @see #callCollectFuture(CmdInfo, byte[]) */
    default CompletableFuture<ResponseCollect> callCollectFuture(CmdInfo cmdInfo, String data) {
        throw new EnterpriseSupportException();
    }

    /** Future-based collect-call with Object payload. @see #callCollectFuture(CmdInfo, byte[]) */
    default CompletableFuture<ResponseCollect> callCollectFuture(CmdInfo cmdInfo, Object data) {
        throw new EnterpriseSupportException();
    }

    /** Future-based collect-call with List payload. @see #callCollectFuture(CmdInfo, byte[]) */
    default CompletableFuture<ResponseCollect> callCollectFuture(CmdInfo cmdInfo, List<?> dataList) {
        throw new EnterpriseSupportException();
    }

    /** Future-based collect-call with List&lt;Integer&gt; payload. @see #callCollectFuture(CmdInfo, byte[]) */
    default CompletableFuture<ResponseCollect> callCollectFutureListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        throw new EnterpriseSupportException();
    }

    /** Future-based collect-call with List&lt;Long&gt; payload. @see #callCollectFuture(CmdInfo, byte[]) */
    default CompletableFuture<ResponseCollect> callCollectFutureListLong(CmdInfo cmdInfo, List<Long> dataList) {
        throw new EnterpriseSupportException();
    }

    /** Future-based collect-call with List&lt;Boolean&gt; payload. @see #callCollectFuture(CmdInfo, byte[]) */
    default CompletableFuture<ResponseCollect> callCollectFutureListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        throw new EnterpriseSupportException();
    }

    /** Future-based collect-call with List&lt;String&gt; payload. @see #callCollectFuture(CmdInfo, byte[]) */
    default CompletableFuture<ResponseCollect> callCollectFutureListString(CmdInfo cmdInfo, List<String> dataList) {
        throw new EnterpriseSupportException();
    }

    //*********************** callCollect callback ***********************//

    /** Async collect-call using a pre-built request message with the current executor. @see #callCollectAsync(CmdInfo, byte[], Consumer) */
    default void callCollectAsync(RequestMessage message, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    /** Async collect-call using a pre-built request message with a custom executor. @see #callCollectAsync(CmdInfo, byte[], Consumer) */
    default void callCollectAsync(RequestMessage message, Consumer<ResponseCollect> action, Executor executor) {
        throw new EnterpriseSupportException();
    }

    /**
     * Call all logic servers asynchronously, invoking the callback with the aggregated response.
     *
     * @param cmdInfo the target command route
     * @param data    the raw byte payload
     * @param action  the callback to invoke with the aggregated response
     */
    default void callCollectAsync(CmdInfo cmdInfo, byte[] data, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    /** Async collect-call with no payload. @see #callCollectAsync(CmdInfo, byte[], Consumer) */
    default void callCollectAsync(CmdInfo cmdInfo, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    /** Async collect-call with int payload. @see #callCollectAsync(CmdInfo, byte[], Consumer) */
    default void callCollectAsync(CmdInfo cmdInfo, int data, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    /** Async collect-call with long payload. @see #callCollectAsync(CmdInfo, byte[], Consumer) */
    default void callCollectAsync(CmdInfo cmdInfo, long data, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    /** Async collect-call with boolean payload. @see #callCollectAsync(CmdInfo, byte[], Consumer) */
    default void callCollectAsync(CmdInfo cmdInfo, boolean data, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    /** Async collect-call with String payload. @see #callCollectAsync(CmdInfo, byte[], Consumer) */
    default void callCollectAsync(CmdInfo cmdInfo, String data, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    /** Async collect-call with Object payload. @see #callCollectAsync(CmdInfo, byte[], Consumer) */
    default void callCollectAsync(CmdInfo cmdInfo, Object data, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    /** Async collect-call with List payload. @see #callCollectAsync(CmdInfo, byte[], Consumer) */
    default void callCollectAsync(CmdInfo cmdInfo, List<?> dataList, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    /** Async collect-call with List&lt;Integer&gt; payload. @see #callCollectAsync(CmdInfo, byte[], Consumer) */
    default void callCollectAsyncListInt(CmdInfo cmdInfo, List<Integer> dataList, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    /** Async collect-call with List&lt;Long&gt; payload. @see #callCollectAsync(CmdInfo, byte[], Consumer) */
    default void callCollectAsyncListLong(CmdInfo cmdInfo, List<Long> dataList, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    /** Async collect-call with List&lt;Boolean&gt; payload. @see #callCollectAsync(CmdInfo, byte[], Consumer) */
    default void callCollectAsyncListBool(CmdInfo cmdInfo, List<Boolean> dataList, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    /** Async collect-call with List&lt;String&gt; payload. @see #callCollectAsync(CmdInfo, byte[], Consumer) */
    default void callCollectAsyncListString(CmdInfo cmdInfo, List<String> dataList, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }
}
