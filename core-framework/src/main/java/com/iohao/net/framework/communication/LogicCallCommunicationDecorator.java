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

import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.core.codec.DataCodec;
import com.iohao.net.framework.core.codec.DataCodecManager;
import com.iohao.net.common.kit.CommonConst;
import com.iohao.net.framework.protocol.RequestMessage;
import com.iohao.net.framework.protocol.Response;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * Decorator providing cross-logic-server synchronous and asynchronous call methods with various data type overloads.
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
public interface LogicCallCommunicationDecorator extends CommonDecorator, LogicCommunicationDecorator {

    private DataCodec getInternalDataCodec() {
        return DataCodecManager.getInternalDataCodec();
    }

    //*********************** call ***********************//

    /** Synchronous call using a pre-built request message. @see #call(CmdInfo, byte[]) */
    default Response call(RequestMessage message) {
        return this.getCommunicationAggregation().call(message);
    }

    /**
     * Call another logic server synchronously and return the response.
     *
     * @param cmdInfo the target command route
     * @param data    the raw byte payload
     * @return the response from the target logic server
     */
    default Response call(CmdInfo cmdInfo, byte[] data) {
        var message = ofRequestMessage(cmdInfo, data);
        return call(message);
    }

    /** Synchronous call with no payload. @see #call(CmdInfo, byte[]) */
    default Response call(CmdInfo cmdInfo) {
        return call(cmdInfo, CommonConst.emptyBytes);
    }

    /** Synchronous call with int payload. @see #call(CmdInfo, byte[]) */
    default Response call(CmdInfo cmdInfo, int data) {
        return call(ofRequestMessage(cmdInfo, data));
    }

    /** Synchronous call with long payload. @see #call(CmdInfo, byte[]) */
    default Response call(CmdInfo cmdInfo, long data) {
        return call(ofRequestMessage(cmdInfo, data));
    }

    /** Synchronous call with boolean payload. @see #call(CmdInfo, byte[]) */
    default Response call(CmdInfo cmdInfo, boolean data) {
        return call(ofRequestMessage(cmdInfo, data));
    }

    /** Synchronous call with String payload. @see #call(CmdInfo, byte[]) */
    default Response call(CmdInfo cmdInfo, String data) {
        return call(ofRequestMessage(cmdInfo, data));
    }

    /** Synchronous call with Object payload. @see #call(CmdInfo, byte[]) */
    default Response call(CmdInfo cmdInfo, Object data) {
        return call(ofRequestMessage(cmdInfo, data));
    }

    /** Synchronous call with List payload. @see #call(CmdInfo, byte[]) */
    default Response call(CmdInfo cmdInfo, List<?> dataList) {
        return call(ofRequestMessage(cmdInfo, dataList));
    }

    /** Synchronous call with List&lt;Integer&gt; payload. @see #call(CmdInfo, byte[]) */
    default Response callListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        return call(ofRequestMessageListInt(cmdInfo, dataList));
    }

    /** Synchronous call with List&lt;Long&gt; payload. @see #call(CmdInfo, byte[]) */
    default Response callListLong(CmdInfo cmdInfo, List<Long> dataList) {
        return call(ofRequestMessageListLong(cmdInfo, dataList));

    }

    /** Synchronous call with List&lt;Boolean&gt; payload. @see #call(CmdInfo, byte[]) */
    default Response callListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        return call(ofRequestMessageListBool(cmdInfo, dataList));
    }

    /** Synchronous call with List&lt;String&gt; payload. @see #call(CmdInfo, byte[]) */
    default Response callListString(CmdInfo cmdInfo, List<String> dataList) {
        return call(ofRequestMessageListString(cmdInfo, dataList));

    }

    //*********************** call future ***********************//

    /** Future-based call using a pre-built request message. @see #callFuture(CmdInfo, byte[]) */
    default CompletableFuture<Response> callFuture(RequestMessage message) {
        return this.getCommunicationAggregation().callFuture(message);
    }

    /**
     * Call another logic server and return a CompletableFuture for the response.
     *
     * @param cmdInfo the target command route
     * @param data    the raw byte payload
     * @return a {@link CompletableFuture} that completes with the response
     */
    default CompletableFuture<Response> callFuture(CmdInfo cmdInfo, byte[] data) {
        var message = ofRequestMessage(cmdInfo, data);
        return callFuture(message);
    }

    /** Future-based call with no payload. @see #callFuture(CmdInfo, byte[]) */
    default CompletableFuture<Response> callFuture(CmdInfo cmdInfo) {
        return callFuture(cmdInfo, CommonConst.emptyBytes);
    }

    /** Future-based call with int payload. @see #callFuture(CmdInfo, byte[]) */
    default CompletableFuture<Response> callFuture(CmdInfo cmdInfo, int data) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encode(data));
    }

    /** Future-based call with long payload. @see #callFuture(CmdInfo, byte[]) */
    default CompletableFuture<Response> callFuture(CmdInfo cmdInfo, long data) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encode(data));
    }

    /** Future-based call with boolean payload. @see #callFuture(CmdInfo, byte[]) */
    default CompletableFuture<Response> callFuture(CmdInfo cmdInfo, boolean data) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encode(data));
    }

    /** Future-based call with String payload. @see #callFuture(CmdInfo, byte[]) */
    default CompletableFuture<Response> callFuture(CmdInfo cmdInfo, String data) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encode(data));
    }

    /** Future-based call with Object payload. @see #callFuture(CmdInfo, byte[]) */
    default CompletableFuture<Response> callFuture(CmdInfo cmdInfo, Object data) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encode(data));
    }

    /** Future-based call with List payload. @see #callFuture(CmdInfo, byte[]) */
    default CompletableFuture<Response> callFuture(CmdInfo cmdInfo, List<?> dataList) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encodeList(dataList));
    }

    /** Future-based call with List&lt;Integer&gt; payload. @see #callFuture(CmdInfo, byte[]) */
    default CompletableFuture<Response> callFutureListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encodeListInt(dataList));
    }

    /** Future-based call with List&lt;Long&gt; payload. @see #callFuture(CmdInfo, byte[]) */
    default CompletableFuture<Response> callFutureListLong(CmdInfo cmdInfo, List<Long> dataList) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encodeListLong(dataList));
    }

    /** Future-based call with List&lt;Boolean&gt; payload. @see #callFuture(CmdInfo, byte[]) */
    default CompletableFuture<Response> callFutureListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encodeListBool(dataList));
    }

    /** Future-based call with List&lt;String&gt; payload. @see #callFuture(CmdInfo, byte[]) */
    default CompletableFuture<Response> callFutureListString(CmdInfo cmdInfo, List<String> dataList) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encodeListString(dataList));
    }

    //*********************** callback ***********************//

    /** Async call using a pre-built request message with the current executor. @see #callAsync(CmdInfo, byte[], Consumer) */
    default void callAsync(RequestMessage message, Consumer<Response> action) {
        var executor = this.getCurrentExecutor();
        callAsync(message, action, executor);
    }

    /** Async call using a pre-built request message with a custom executor. @see #callAsync(CmdInfo, byte[], Consumer) */
    default void callAsync(RequestMessage message, Consumer<Response> action, Executor executor) {
        callFuture(message).thenAcceptAsync(action, executor);
    }

    /**
     * Call another logic server asynchronously, invoking the callback with the response.
     * <p>The callback is executed on the current flow context's executor.
     *
     * @param cmdInfo the target command route
     * @param data    the raw byte payload
     * @param action  the callback to invoke with the response
     */
    default void callAsync(CmdInfo cmdInfo, byte[] data, Consumer<Response> action) {
        var message = ofRequestMessage(cmdInfo, data);
        callAsync(message, action);
    }

    /** Async call with no payload. @see #callAsync(CmdInfo, byte[], Consumer) */
    default void callAsync(CmdInfo cmdInfo, Consumer<Response> action) {
        callAsync(cmdInfo, CommonConst.emptyBytes, action);
    }

    /** Async call with int payload. @see #callAsync(CmdInfo, byte[], Consumer) */
    default void callAsync(CmdInfo cmdInfo, int data, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encode(data), action);
    }

    /** Async call with long payload. @see #callAsync(CmdInfo, byte[], Consumer) */
    default void callAsync(CmdInfo cmdInfo, long data, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encode(data), action);
    }

    /** Async call with boolean payload. @see #callAsync(CmdInfo, byte[], Consumer) */
    default void callAsync(CmdInfo cmdInfo, boolean data, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encode(data), action);
    }

    /** Async call with String payload. @see #callAsync(CmdInfo, byte[], Consumer) */
    default void callAsync(CmdInfo cmdInfo, String data, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encode(data), action);
    }

    /** Async call with Object payload. @see #callAsync(CmdInfo, byte[], Consumer) */
    default void callAsync(CmdInfo cmdInfo, Object data, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encode(data), action);
    }

    /** Async call with List payload. @see #callAsync(CmdInfo, byte[], Consumer) */
    default void callAsync(CmdInfo cmdInfo, List<?> dataList, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encodeList(dataList), action);
    }

    /** Async call with List&lt;Integer&gt; payload. @see #callAsync(CmdInfo, byte[], Consumer) */
    default void callAsyncListInt(CmdInfo cmdInfo, List<Integer> dataList, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encodeListInt(dataList), action);
    }

    /** Async call with List&lt;Long&gt; payload. @see #callAsync(CmdInfo, byte[], Consumer) */
    default void callAsyncListLong(CmdInfo cmdInfo, List<Long> dataList, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encodeListLong(dataList), action);
    }

    /** Async call with List&lt;Boolean&gt; payload. @see #callAsync(CmdInfo, byte[], Consumer) */
    default void callAsyncListBool(CmdInfo cmdInfo, List<Boolean> dataList, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encodeListBool(dataList), action);
    }

    /** Async call with List&lt;String&gt; payload. @see #callAsync(CmdInfo, byte[], Consumer) */
    default void callAsyncListString(CmdInfo cmdInfo, List<String> dataList, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encodeListString(dataList), action);
    }
}
