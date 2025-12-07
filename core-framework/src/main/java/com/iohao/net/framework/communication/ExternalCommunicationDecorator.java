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
    default void callbackExternal(ExternalRequestMessage message, Consumer<ExternalResponse> action) {
        callbackExternal(message, action, this.getCurrentExecutor());
    }

    default void callbackExternal(ExternalRequestMessage message, Consumer<ExternalResponse> action, Executor executor) {
        callExternalFuture(message).thenAcceptAsync(action, executor);
    }

    default void callbackExternal(int templateId, byte[] payload, Consumer<ExternalResponse> action) {
        var message = ofExternalRequestMessage(templateId, payload);
        callbackExternal(message, action);
    }

    default void callbackExternal(int templateId, Consumer<ExternalResponse> action) {
        var message = ofExternalRequestMessage(templateId);
        callbackExternal(message, action);
    }

    //*********************** CollectExternal call ***********************//

    default ResponseCollectExternal callCollectExternal(ExternalRequestMessage message) {
        return this.getCommunicationAggregation().callCollectExternal(message);
    }

    default ResponseCollectExternal callCollectExternal(int templateId, byte[] payload) {
        return callCollectExternal(ofExternalRequestMessage(templateId, payload));
    }

    default ResponseCollectExternal callCollectExternal(int templateId) {
        return callCollectExternal(ofExternalRequestMessage(templateId));
    }

    //*********************** CollectExternal future ***********************//
    default CompletableFuture<ResponseCollectExternal> callCollectExternalFuture(ExternalRequestMessage message) {
        return this.getCommunicationAggregation().callCollectExternalFuture(message);
    }

    default CompletableFuture<ResponseCollectExternal> callCollectExternalFuture(int templateId, byte[] payload) {
        var message = ofExternalRequestMessage(templateId, payload);
        return callCollectExternalFuture(message);
    }

    default CompletableFuture<ResponseCollectExternal> callCollectExternalFuture(int templateId) {
        var message = ofExternalRequestMessage(templateId);
        return callCollectExternalFuture(message);
    }

    //*********************** CollectExternal callback ***********************//
    default void callbackCollectExternal(ExternalRequestMessage message, Consumer<ResponseCollectExternal> action) {
        Executor executor = this.getCurrentExecutor();
        callbackCollectExternal(message, action, executor);
    }

    default void callbackCollectExternal(ExternalRequestMessage message, Consumer<ResponseCollectExternal> action, Executor executor) {
        callCollectExternalFuture(message).thenAcceptAsync(action, executor);
    }

    default void callbackCollectExternal(int templateId, byte[] payload, Consumer<ResponseCollectExternal> action) {
        var message = ofExternalRequestMessage(templateId, payload);
        callbackCollectExternal(message, action);
    }

    default void callbackCollectExternal(int templateId, Consumer<ResponseCollectExternal> action) {
        var message = ofExternalRequestMessage(templateId);
        callbackCollectExternal(message, action);
    }

    //*********************** bindingLogicServer ***********************//
    default CommonResponse bindingLogicServer(BindingLogicServerMessage message) {
        return this.getCommunicationAggregation().bindingLogicServer(message);
    }
}
