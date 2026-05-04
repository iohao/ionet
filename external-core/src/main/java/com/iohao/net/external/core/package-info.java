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
 * Provides the core abstractions and runtime infrastructure for external servers, including
 * protocol handling, session management, hooks, and internal communication integration.
 *
 * <p>See <a href="https://iohao.github.io/ionet/docs/manual/external_intro">External Server</a> for
 * the architecture overview.
 * <p>
 * Responsibilities of the External Server
 * <pre>
 * 1. Maintain long connections with users (players)
 * 2. Help developers abstract away communication and connection details
 * 3. Built-in connection methods: WebSocket, TCP. Custom selectors can add other transports.
 * 4. Forward user (player) requests to the Gateway
 * 5. Dynamic scaling (adding or removing) of server instances
 * 6. Feature extensions, such as: route existence detection, route permissions, UserSession management, heartbeats,
 * and future features like circuit breakers, rate limiting, load shedding (or graceful degradation), and user traffic statistics.
 * </pre>
 * <p>
 * Extension Scenario
 * <pre>
 * The External Server is primarily responsible for connections with users (players).
 * Suppose a single piece of hardware supports a maximum of 5000 user connections.
 * When the number of users reaches 7000, we can add another External Server to manage traffic and reduce load.
 *
 * Due to the scalability and flexibility of the External Server, it can support concurrent online players ranging
 * from a few thousand to tens of millions.
 * This is because by increasing the number of External Servers, connection load balancing and traffic control
 * can be effectively managed, enabling the system to better withstand high concurrency pressure.
 * </pre>
 * <p>
 * Switching, Support, and Extension of Connection Methods
 * <pre>
 * The External Server already provides support for TCP and WebSocket connection methods, and offers flexible ways
 * to switch between them.
 * TCP and WebSocket connection methods can be seamlessly integrated with the business code.
 * Developers can use a single set of business code, without any modification, to support multiple communication protocols.
 *
 * Built-in TCP and WebSocket transports can be switched by changing the corresponding enumeration.
 * Multiple external servers can also coexist in one application to expose different transports.
 *
 * Connection methods are extensible. Custom transports such as UDP or KCP can be integrated by registering a matching
 * ExternalJoinSelector and transport bootstrap.
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-04-28
 */
package com.iohao.net.external.core;
