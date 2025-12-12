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
 * LogicCallCommunicationDecorator
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

    default Response call(RequestMessage message) {
        return this.getCommunicationAggregation().call(message);
    }

    default Response call(CmdInfo cmdInfo, byte[] data) {
        var message = ofRequestMessage(cmdInfo, data);
        return call(message);
    }

    default Response call(CmdInfo cmdInfo) {
        return call(cmdInfo, CommonConst.emptyBytes);
    }

    default Response call(CmdInfo cmdInfo, int data) {
        return call(ofRequestMessage(cmdInfo, data));
    }

    default Response call(CmdInfo cmdInfo, long data) {
        return call(ofRequestMessage(cmdInfo, data));
    }

    default Response call(CmdInfo cmdInfo, boolean data) {
        return call(ofRequestMessage(cmdInfo, data));
    }

    default Response call(CmdInfo cmdInfo, String data) {
        return call(ofRequestMessage(cmdInfo, data));
    }

    default Response call(CmdInfo cmdInfo, Object data) {
        return call(ofRequestMessage(cmdInfo, data));
    }

    default Response call(CmdInfo cmdInfo, List<?> dataList) {
        return call(ofRequestMessage(cmdInfo, dataList));
    }

    default Response callListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        return call(ofRequestMessageListInt(cmdInfo, dataList));
    }

    default Response callListLong(CmdInfo cmdInfo, List<Long> dataList) {
        return call(ofRequestMessageListLong(cmdInfo, dataList));

    }

    default Response callListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        return call(ofRequestMessageListBool(cmdInfo, dataList));
    }

    default Response callListString(CmdInfo cmdInfo, List<String> dataList) {
        return call(ofRequestMessageListString(cmdInfo, dataList));

    }

    //*********************** call future ***********************//
    default CompletableFuture<Response> callFuture(RequestMessage message) {
        return this.getCommunicationAggregation().callFuture(message);
    }

    default CompletableFuture<Response> callFuture(CmdInfo cmdInfo, byte[] data) {
        var message = ofRequestMessage(cmdInfo, data);
        return callFuture(message);
    }

    default CompletableFuture<Response> callFuture(CmdInfo cmdInfo) {
        return callFuture(cmdInfo, CommonConst.emptyBytes);
    }

    default CompletableFuture<Response> callFuture(CmdInfo cmdInfo, int data) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encode(data));
    }

    default CompletableFuture<Response> callFuture(CmdInfo cmdInfo, long data) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encode(data));
    }

    default CompletableFuture<Response> callFuture(CmdInfo cmdInfo, boolean data) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encode(data));
    }

    default CompletableFuture<Response> callFuture(CmdInfo cmdInfo, String data) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encode(data));
    }

    default CompletableFuture<Response> callFuture(CmdInfo cmdInfo, Object data) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encode(data));
    }

    default CompletableFuture<Response> callFuture(CmdInfo cmdInfo, List<?> dataList) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encodeList(dataList));
    }

    default CompletableFuture<Response> callFutureListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encodeListInt(dataList));
    }

    default CompletableFuture<Response> callFutureListLong(CmdInfo cmdInfo, List<Long> dataList) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encodeListLong(dataList));
    }

    default CompletableFuture<Response> callFutureListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encodeListBool(dataList));
    }

    default CompletableFuture<Response> callFutureListString(CmdInfo cmdInfo, List<String> dataList) {
        var codec = this.getInternalDataCodec();
        return callFuture(cmdInfo, codec.encodeListString(dataList));
    }

    //*********************** callback ***********************//

    default void callAsync(RequestMessage message, Consumer<Response> action) {
        var executor = this.getCurrentExecutor();
        callAsync(message, action, executor);
    }

    default void callAsync(RequestMessage message, Consumer<Response> action, Executor executor) {
        callFuture(message).thenAcceptAsync(action, executor);
    }

    default void callAsync(CmdInfo cmdInfo, byte[] data, Consumer<Response> action) {
        var message = ofRequestMessage(cmdInfo, data);
        callAsync(message, action);
    }

    default void callAsync(CmdInfo cmdInfo, Consumer<Response> action) {
        callAsync(cmdInfo, CommonConst.emptyBytes, action);
    }

    default void callAsync(CmdInfo cmdInfo, int data, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encode(data), action);
    }

    default void callAsync(CmdInfo cmdInfo, long data, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encode(data), action);
    }

    default void callAsync(CmdInfo cmdInfo, boolean data, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encode(data), action);
    }

    default void callAsync(CmdInfo cmdInfo, String data, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encode(data), action);
    }

    default void callAsync(CmdInfo cmdInfo, Object data, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encode(data), action);
    }

    default void callAsync(CmdInfo cmdInfo, List<?> dataList, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encodeList(dataList), action);
    }

    default void callAsyncListInt(CmdInfo cmdInfo, List<Integer> dataList, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encodeListInt(dataList), action);
    }

    default void callAsyncListLong(CmdInfo cmdInfo, List<Long> dataList, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encodeListLong(dataList), action);
    }

    default void callAsyncListBool(CmdInfo cmdInfo, List<Boolean> dataList, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encodeListBool(dataList), action);
    }

    default void callAsyncListString(CmdInfo cmdInfo, List<String> dataList, Consumer<Response> action) {
        var codec = this.getInternalDataCodec();
        callAsync(cmdInfo, codec.encodeListString(dataList), action);
    }
}
