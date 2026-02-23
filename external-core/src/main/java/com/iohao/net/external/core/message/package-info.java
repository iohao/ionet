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
/**
 * Provides the external-server unified interaction protocol, related codecs, and customization
 * points for client-facing message encoding/decoding.
 *
 * <p>See <a href="https://iohao.github.io/ionet/docs/manual/external_message">External Server
 * Protocol Description</a> for protocol details.
 * <p>
 * ExternalMessage
 * <pre>
 * ExternalMessage is the unified interaction protocol, also known as the external server protocol. Its main purpose is to serve as the unified protocol for interaction with the game client, and it includes:
 * 1. Request command type: 0 for heartbeat, 1 for business.
 * 2. Business route (high 16 bits for main, low 16 bits for sub).
 * 3. Response code: 0 for success, others for error.
 * 4. Verification information (error message, exception message).
 * 5. Business request data.
 * 6. Message marker number. Set by the front-end when requesting and carried by the server upon response (similar to a transparent transmission parameter).
 * </pre>
 * <p>
 * Custom Unified Interaction Protocol
 * <pre>
 * ExternalMessage is the unified external protocol for user interaction with the server. By default, users (clients) interact via ExternalMessage when initiating a request.
 *
 * If there are no special circumstances, it is recommended to use the ExternalMessage provided by the framework by default. Because fields not used will be compressed by Protocol Buffer, using only the space needed.
 *
 * ExternalMessage is a unified protocol provided by the framework for external interaction and is the default recommended method.
 * Note that this is the default recommended method, not the only method. Developers can customize this part of the content.
 * That is, you can choose not to use ExternalMessage for external communication but instead use a custom unified protocol.
 *
 * For example, if you are planning to develop an IoT-related or another project and want to simplify the unified external protocol, where only the route and business object are sufficient for the protocol content.
 * In this case, you can implement a custom protocol by overriding the {@link com.iohao.net.external.core.message.CommunicationMessageCodec} interface.
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-09-13
 */
package com.iohao.net.external.core.message;
