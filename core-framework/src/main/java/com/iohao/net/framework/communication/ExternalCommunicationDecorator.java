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

import com.iohao.net.framework.protocol.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * ExternalCommunicationDecorator
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
public interface ExternalCommunicationDecorator extends CommonDecorator {

    ExternalRequestMessage ofExternalRequestMessage(int templateId, byte[] payload);

    default ExternalRequestMessage ofExternalRequestMessage(int templateId) {
        return ofExternalRequestMessage(templateId, null);
    }

    //*********************** callExternal ***********************//

    default ExternalResponse callExternal(ExternalRequestMessage message) {
        return this.getCommunicationAggregation().callExternal(message);
    }

    default ExternalResponse callExternal(int templateId, byte[] payload) {
        return callExternal(ofExternalRequestMessage(templateId, payload));
    }

    default ExternalResponse callExternal(int templateId) {
        return callExternal(ofExternalRequestMessage(templateId));
    }

    //*********************** callExternal future ***********************//
    default CompletableFuture<ExternalResponse> callExternalFuture(ExternalRequestMessage message) {
        return this.getCommunicationAggregation().callExternalFuture(message);
    }

    default CompletableFuture<ExternalResponse> callExternalFuture(int templateId, byte[] payload) {
        return callExternalFuture(ofExternalRequestMessage(templateId, payload));
    }

    default CompletableFuture<ExternalResponse> callExternalFuture(int templateId) {
        return callExternalFuture(ofExternalRequestMessage(templateId));
    }

    //*********************** callExternal callback ***********************//
    default void callExternalAsync(ExternalRequestMessage message, Consumer<ExternalResponse> action) {
        callExternalAsync(message, action, this.getCurrentExecutor());
    }

    default void callExternalAsync(ExternalRequestMessage message, Consumer<ExternalResponse> action, Executor executor) {
        callExternalFuture(message).thenAcceptAsync(action, executor);
    }

    default void callExternalAsync(int templateId, byte[] payload, Consumer<ExternalResponse> action) {
        var message = ofExternalRequestMessage(templateId, payload);
        callExternalAsync(message, action);
    }

    default void callExternalAsync(int templateId, Consumer<ExternalResponse> action) {
        var message = ofExternalRequestMessage(templateId);
        callExternalAsync(message, action);
    }

    //*********************** CollectExternal call ***********************//

    default ResponseCollectExternal callExternalCollect(ExternalRequestMessage message) {
        return this.getCommunicationAggregation().callCollectExternal(message);
    }

    default ResponseCollectExternal callExternalCollect(int templateId, byte[] payload) {
        return callExternalCollect(ofExternalRequestMessage(templateId, payload));
    }

    default ResponseCollectExternal callExternalCollect(int templateId) {
        return callExternalCollect(ofExternalRequestMessage(templateId));
    }

    //*********************** CollectExternal future ***********************//
    default CompletableFuture<ResponseCollectExternal> callExternalCollectFuture(ExternalRequestMessage message) {
        return this.getCommunicationAggregation().callCollectExternalFuture(message);
    }

    default CompletableFuture<ResponseCollectExternal> callExternalCollectFuture(int templateId, byte[] payload) {
        var message = ofExternalRequestMessage(templateId, payload);
        return callExternalCollectFuture(message);
    }

    default CompletableFuture<ResponseCollectExternal> callExternalCollectFuture(int templateId) {
        var message = ofExternalRequestMessage(templateId);
        return callExternalCollectFuture(message);
    }

    //*********************** CollectExternal callback ***********************//
    default void callExternalCollectAsync(ExternalRequestMessage message, Consumer<ResponseCollectExternal> action) {
        Executor executor = this.getCurrentExecutor();
        callExternalCollectAsync(message, action, executor);
    }

    default void callExternalCollectAsync(ExternalRequestMessage message, Consumer<ResponseCollectExternal> action, Executor executor) {
        callExternalCollectFuture(message).thenAcceptAsync(action, executor);
    }

    default void callExternalCollectAsync(int templateId, byte[] payload, Consumer<ResponseCollectExternal> action) {
        var message = ofExternalRequestMessage(templateId, payload);
        callExternalCollectAsync(message, action);
    }

    default void callExternalCollectAsync(int templateId, Consumer<ResponseCollectExternal> action) {
        var message = ofExternalRequestMessage(templateId);
        callExternalCollectAsync(message, action);
    }

    //*********************** bindingLogicServer ***********************//
    default CommonResponse bindingLogicServer(BindingLogicServerMessage message) {
        return this.getCommunicationAggregation().bindingLogicServer(message);
    }
}
