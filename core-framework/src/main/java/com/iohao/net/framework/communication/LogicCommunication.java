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

import com.iohao.net.framework.protocol.RequestMessage;
import com.iohao.net.framework.protocol.SendMessage;
import com.iohao.net.framework.protocol.Response;

import java.util.concurrent.CompletableFuture;

/**
 * Interface for inter-logic-server communication.
 * <p>
 * Extends {@link PublisherCommunication} and provides fire-and-forget sending,
 * asynchronous call, and blocking call methods for logic-to-logic messaging.
 *
 * @author 渔民小镇
 * @date 2025-09-04
 * @since 25.1
 */
public interface LogicCommunication extends PublisherCommunication {
    /**
     * Send a message to another logic server without expecting a response (fire-and-forget).
     *
     * @param message the send message
     */
    void send(SendMessage message);

    /**
     * Call another logic server asynchronously and return a future for the response.
     *
     * @param message the request message
     * @return a {@link CompletableFuture} that completes with the response
     */
    CompletableFuture<Response> callFuture(RequestMessage message);

    /**
     * Call another logic server and block until a response is received.
     *
     * @param message the request message
     * @return the response from the target logic server
     */
    Response call(RequestMessage message);
}
