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
import java.util.concurrent.*;
import java.util.function.*;

/**
 * Decorator interface providing convenience methods for communicating with external (client-facing) servers.
 * <p>
 * Offers three invocation styles for each operation:
 * <ul>
 *   <li>Synchronous -- blocking call returning the response directly</li>
 *   <li>Future -- returning a {@link CompletableFuture} for non-blocking composition</li>
 *   <li>Async callback -- accepting a {@link Consumer} executed on the current or a supplied {@link Executor}</li>
 * </ul>
 * Also supports collect-style calls that aggregate responses from multiple external servers,
 * and logic-server binding.
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
public interface ExternalCommunicationDecorator extends CommonDecorator {

    /**
     * Create an {@link ExternalRequestMessage} with the given template ID and payload.
     *
     * @param templateId the external request template identifier
     * @param payload    the serialized request payload (may be {@code null})
     * @return a new external request message
     */
    ExternalRequestMessage ofExternalRequestMessage(int templateId, byte[] payload);

    /**
     * Create an {@link ExternalRequestMessage} with the given template ID and no payload.
     *
     * @param templateId the external request template identifier
     * @return a new external request message with {@code null} payload
     */
    default ExternalRequestMessage ofExternalRequestMessage(int templateId) {
        return ofExternalRequestMessage(templateId, null);
    }

    //*********************** callExternal ***********************//

    /**
     * Synchronously call a single external server.
     *
     * @param message the external request message
     * @return the external server response
     */
    default ExternalResponse callExternal(ExternalRequestMessage message) {
        return this.getCommunicationAggregation().callExternal(message);
    }

    /**
     * Synchronously call a single external server with the given template ID and payload.
     *
     * @param templateId the external request template identifier
     * @param payload    the serialized request payload
     * @return the external server response
     */
    default ExternalResponse callExternal(int templateId, byte[] payload) {
        return callExternal(ofExternalRequestMessage(templateId, payload));
    }

    /**
     * Synchronously call a single external server with the given template ID and no payload.
     *
     * @param templateId the external request template identifier
     * @return the external server response
     */
    default ExternalResponse callExternal(int templateId) {
        return callExternal(ofExternalRequestMessage(templateId));
    }

    //*********************** callExternal future ***********************//

    /**
     * Asynchronously call a single external server, returning a future.
     *
     * @param message the external request message
     * @return a future that completes with the external server response
     */
    default CompletableFuture<ExternalResponse> callExternalFuture(ExternalRequestMessage message) {
        return this.getCommunicationAggregation().callExternalFuture(message);
    }

    /**
     * Asynchronously call a single external server with the given template ID and payload.
     *
     * @param templateId the external request template identifier
     * @param payload    the serialized request payload
     * @return a future that completes with the external server response
     */
    default CompletableFuture<ExternalResponse> callExternalFuture(int templateId, byte[] payload) {
        return callExternalFuture(ofExternalRequestMessage(templateId, payload));
    }

    /**
     * Asynchronously call a single external server with the given template ID and no payload.
     *
     * @param templateId the external request template identifier
     * @return a future that completes with the external server response
     */
    default CompletableFuture<ExternalResponse> callExternalFuture(int templateId) {
        return callExternalFuture(ofExternalRequestMessage(templateId));
    }

    //*********************** callExternal callback ***********************//

    /**
     * Call a single external server asynchronously and invoke the callback on the current executor.
     *
     * @param message the external request message
     * @param action  the callback to invoke with the response
     */
    default void callExternalAsync(ExternalRequestMessage message, Consumer<ExternalResponse> action) {
        callExternalAsync(message, action, this.getCurrentExecutor());
    }

    /**
     * Call a single external server asynchronously and invoke the callback on the supplied executor.
     *
     * @param message  the external request message
     * @param action   the callback to invoke with the response
     * @param executor the executor on which to run the callback
     */
    default void callExternalAsync(ExternalRequestMessage message, Consumer<ExternalResponse> action, Executor executor) {
        callExternalFuture(message).thenAcceptAsync(action, executor);
    }

    /**
     * Call a single external server asynchronously with the given template ID and payload.
     *
     * @param templateId the external request template identifier
     * @param payload    the serialized request payload
     * @param action     the callback to invoke with the response
     */
    default void callExternalAsync(int templateId, byte[] payload, Consumer<ExternalResponse> action) {
        var message = ofExternalRequestMessage(templateId, payload);
        callExternalAsync(message, action);
    }

    /**
     * Call a single external server asynchronously with the given template ID and no payload.
     *
     * @param templateId the external request template identifier
     * @param action     the callback to invoke with the response
     */
    default void callExternalAsync(int templateId, Consumer<ExternalResponse> action) {
        var message = ofExternalRequestMessage(templateId);
        callExternalAsync(message, action);
    }

    //*********************** CollectExternal call ***********************//

    /**
     * Synchronously call all external servers and collect their aggregated responses.
     *
     * @param message the external request message
     * @return the aggregated response from all external servers
     */
    default ResponseCollectExternal callExternalCollect(ExternalRequestMessage message) {
        return this.getCommunicationAggregation().callCollectExternal(message);
    }

    /**
     * Synchronously call all external servers with the given template ID and payload.
     *
     * @param templateId the external request template identifier
     * @param payload    the serialized request payload
     * @return the aggregated response from all external servers
     */
    default ResponseCollectExternal callExternalCollect(int templateId, byte[] payload) {
        return callExternalCollect(ofExternalRequestMessage(templateId, payload));
    }

    /**
     * Synchronously call all external servers with the given template ID and no payload.
     *
     * @param templateId the external request template identifier
     * @return the aggregated response from all external servers
     */
    default ResponseCollectExternal callExternalCollect(int templateId) {
        return callExternalCollect(ofExternalRequestMessage(templateId));
    }

    //*********************** CollectExternal future ***********************//

    /**
     * Asynchronously call all external servers and collect their aggregated responses.
     *
     * @param message the external request message
     * @return a future that completes with the aggregated response
     */
    default CompletableFuture<ResponseCollectExternal> callExternalCollectFuture(ExternalRequestMessage message) {
        return this.getCommunicationAggregation().callCollectExternalFuture(message);
    }

    /**
     * Asynchronously call all external servers with the given template ID and payload.
     *
     * @param templateId the external request template identifier
     * @param payload    the serialized request payload
     * @return a future that completes with the aggregated response
     */
    default CompletableFuture<ResponseCollectExternal> callExternalCollectFuture(int templateId, byte[] payload) {
        var message = ofExternalRequestMessage(templateId, payload);
        return callExternalCollectFuture(message);
    }

    /**
     * Asynchronously call all external servers with the given template ID and no payload.
     *
     * @param templateId the external request template identifier
     * @return a future that completes with the aggregated response
     */
    default CompletableFuture<ResponseCollectExternal> callExternalCollectFuture(int templateId) {
        var message = ofExternalRequestMessage(templateId);
        return callExternalCollectFuture(message);
    }

    //*********************** CollectExternal callback ***********************//

    /**
     * Call all external servers asynchronously and invoke the callback on the current executor.
     *
     * @param message the external request message
     * @param action  the callback to invoke with the aggregated response
     */
    default void callExternalCollectAsync(ExternalRequestMessage message, Consumer<ResponseCollectExternal> action) {
        Executor executor = this.getCurrentExecutor();
        callExternalCollectAsync(message, action, executor);
    }

    /**
     * Call all external servers asynchronously and invoke the callback on the supplied executor.
     *
     * @param message  the external request message
     * @param action   the callback to invoke with the aggregated response
     * @param executor the executor on which to run the callback
     */
    default void callExternalCollectAsync(ExternalRequestMessage message, Consumer<ResponseCollectExternal> action, Executor executor) {
        callExternalCollectFuture(message).thenAcceptAsync(action, executor);
    }

    /**
     * Call all external servers asynchronously with the given template ID and payload.
     *
     * @param templateId the external request template identifier
     * @param payload    the serialized request payload
     * @param action     the callback to invoke with the aggregated response
     */
    default void callExternalCollectAsync(int templateId, byte[] payload, Consumer<ResponseCollectExternal> action) {
        var message = ofExternalRequestMessage(templateId, payload);
        callExternalCollectAsync(message, action);
    }

    /**
     * Call all external servers asynchronously with the given template ID and no payload.
     *
     * @param templateId the external request template identifier
     * @param action     the callback to invoke with the aggregated response
     */
    default void callExternalCollectAsync(int templateId, Consumer<ResponseCollectExternal> action) {
        var message = ofExternalRequestMessage(templateId);
        callExternalCollectAsync(message, action);
    }

    //*********************** bindingLogicServer ***********************//

    /**
     * Bind a user session to a specific logic server.
     *
     * @param message the binding request message
     * @return the common response indicating success or failure
     */
    default CommonResponse bindingLogicServer(BindingLogicServerMessage message) {
        return this.getCommunicationAggregation().bindingLogicServer(message);
    }
}
