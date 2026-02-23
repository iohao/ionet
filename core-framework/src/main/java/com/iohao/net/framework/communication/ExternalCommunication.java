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

import com.iohao.net.framework.protocol.ExternalRequestMessage;
import com.iohao.net.framework.protocol.ExternalResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Interface for communicating with external (client-facing) servers.
 * <p>
 * Provides both asynchronous and blocking methods to send a request to an
 * external server and receive a response.
 *
 * @author 渔民小镇
 * @date 2025-09-07
 * @since 25.1
 */
public interface ExternalCommunication {
    /**
     * Send a request to an external server asynchronously.
     *
     * @param message the external request message
     * @return a {@link CompletableFuture} that completes with the external response
     */
    CompletableFuture<ExternalResponse> callExternalFuture(ExternalRequestMessage message);

    /**
     * Send a request to an external server and block until a response is received.
     *
     * @param message the external request message
     * @return the external response
     */
    ExternalResponse callExternal(ExternalRequestMessage message);
}
