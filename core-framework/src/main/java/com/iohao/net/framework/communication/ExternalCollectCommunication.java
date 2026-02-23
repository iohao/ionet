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
import com.iohao.net.framework.protocol.*;

import java.util.concurrent.CompletableFuture;

/**
 * Communication interface for collecting aggregated responses from multiple external (client-facing) servers.
 * <p>
 * Provides both synchronous and asynchronous (future-based) methods for broadcasting requests
 * to all external servers and gathering their responses, as well as logic-server binding
 * and direct message writing to user sessions.
 *
 * @author 渔民小镇
 * @date 2025-09-07
 * @since 25.1
 */
@Enterprise
public interface ExternalCollectCommunication {
    /**
     * Asynchronously call all external servers and collect their responses.
     *
     * @param message the external request message to broadcast
     * @return a future that completes with the aggregated response from all external servers
     */
    CompletableFuture<ResponseCollectExternal> callCollectExternalFuture(ExternalRequestMessage message);

    /**
     * Synchronously call all external servers and collect their responses.
     *
     * @param message the external request message to broadcast
     * @return the aggregated response from all external servers
     */
    ResponseCollectExternal callCollectExternal(ExternalRequestMessage message);

    /**
     * Asynchronously bind a user session to a specific logic server.
     *
     * @param message the binding request message
     * @return a future that completes with the common response
     */
    CompletableFuture<CommonResponse> bindingLogicServerFuture(BindingLogicServerMessage message);

    /**
     * Synchronously bind a user session to a specific logic server.
     *
     * @param message the binding request message
     * @return the common response indicating success or failure
     */
    CommonResponse bindingLogicServer(BindingLogicServerMessage message);

    /**
     * Write a response message directly to a user session on the external server.
     *
     * @param message the user response message to deliver
     */
    void writeMessage(UserResponseMessage message);
}
