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

import com.iohao.net.framework.annotations.Enterprise;
import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.common.kit.exception.EnterpriseSupportException;
import com.iohao.net.framework.protocol.RequestMessage;
import com.iohao.net.framework.protocol.ResponseCollect;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * LogicCallCollectCommunicationDecorator
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
@Enterprise
public interface LogicCallCollectCommunicationDecorator extends CommonDecorator, LogicCommunicationDecorator {

    //*********************** callCollect ***********************//
    default ResponseCollect callCollect(RequestMessage message) {
        return this.getCommunicationAggregation().callCollect(message);
    }

    default ResponseCollect callCollect(CmdInfo cmdInfo, byte[] data) {
        throw new EnterpriseSupportException();
    }

    default ResponseCollect callCollect(CmdInfo cmdInfo) {
        throw new EnterpriseSupportException();
    }

    default ResponseCollect callCollect(CmdInfo cmdInfo, int data) {
        throw new EnterpriseSupportException();
    }

    default ResponseCollect callCollect(CmdInfo cmdInfo, long data) {
        throw new EnterpriseSupportException();
    }

    default ResponseCollect callCollect(CmdInfo cmdInfo, boolean data) {
        throw new EnterpriseSupportException();
    }

    default ResponseCollect callCollect(CmdInfo cmdInfo, String data) {
        throw new EnterpriseSupportException();
    }

    default ResponseCollect callCollect(CmdInfo cmdInfo, Object data) {
        throw new EnterpriseSupportException();
    }

    default ResponseCollect callCollect(CmdInfo cmdInfo, List<?> dataList) {
        throw new EnterpriseSupportException();
    }

    default ResponseCollect callCollectListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        throw new EnterpriseSupportException();
    }

    default ResponseCollect callCollectListLong(CmdInfo cmdInfo, List<Long> dataList) {
        throw new EnterpriseSupportException();
    }

    default ResponseCollect callCollectListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        throw new EnterpriseSupportException();
    }

    default ResponseCollect callCollectListString(CmdInfo cmdInfo, List<String> dataList) {
        throw new EnterpriseSupportException();
    }

    //*********************** callCollect future ***********************//

    default CompletableFuture<ResponseCollect> callCollectFuture(RequestMessage message) {
        throw new EnterpriseSupportException();
    }

    default CompletableFuture<ResponseCollect> callCollectFuture(CmdInfo cmdInfo, byte[] data) {
        throw new EnterpriseSupportException();
    }

    default CompletableFuture<ResponseCollect> callCollectFuture(CmdInfo cmdInfo) {
        throw new EnterpriseSupportException();
    }

    default CompletableFuture<ResponseCollect> callCollectFuture(CmdInfo cmdInfo, int data) {
        throw new EnterpriseSupportException();
    }

    default CompletableFuture<ResponseCollect> callCollectFuture(CmdInfo cmdInfo, long data) {
        throw new EnterpriseSupportException();
    }

    default CompletableFuture<ResponseCollect> callCollectFuture(CmdInfo cmdInfo, boolean data) {
        throw new EnterpriseSupportException();
    }

    default CompletableFuture<ResponseCollect> callCollectFuture(CmdInfo cmdInfo, String data) {
        throw new EnterpriseSupportException();
    }

    default CompletableFuture<ResponseCollect> callCollectFuture(CmdInfo cmdInfo, Object data) {
        throw new EnterpriseSupportException();
    }

    default CompletableFuture<ResponseCollect> callCollectFuture(CmdInfo cmdInfo, List<?> dataList) {
        throw new EnterpriseSupportException();
    }

    default CompletableFuture<ResponseCollect> callCollectFutureListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        throw new EnterpriseSupportException();
    }

    default CompletableFuture<ResponseCollect> callCollectFutureListLong(CmdInfo cmdInfo, List<Long> dataList) {
        throw new EnterpriseSupportException();
    }

    default CompletableFuture<ResponseCollect> callCollectFutureListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        throw new EnterpriseSupportException();
    }

    default CompletableFuture<ResponseCollect> callCollectFutureListString(CmdInfo cmdInfo, List<String> dataList) {
        throw new EnterpriseSupportException();
    }

    //*********************** callCollect callback ***********************//

    default void callCollectAsync(RequestMessage message, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    default void callCollectAsync(RequestMessage message, Consumer<ResponseCollect> action, Executor executor) {
        throw new EnterpriseSupportException();
    }

    default void callCollectAsync(CmdInfo cmdInfo, byte[] data, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    default void callCollectAsync(CmdInfo cmdInfo, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    default void callCollectAsync(CmdInfo cmdInfo, int data, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    default void callCollectAsync(CmdInfo cmdInfo, long data, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    default void callCollectAsync(CmdInfo cmdInfo, boolean data, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    default void callCollectAsync(CmdInfo cmdInfo, String data, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    default void callCollectAsync(CmdInfo cmdInfo, Object data, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    default void callCollectAsync(CmdInfo cmdInfo, List<?> dataList, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    default void callCollectAsyncListInt(CmdInfo cmdInfo, List<Integer> dataList, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    default void callCollectAsyncListLong(CmdInfo cmdInfo, List<Long> dataList, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    default void callCollectAsyncListBool(CmdInfo cmdInfo, List<Boolean> dataList, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }

    default void callCollectAsyncListString(CmdInfo cmdInfo, List<String> dataList, Consumer<ResponseCollect> action) {
        throw new EnterpriseSupportException();
    }
}
