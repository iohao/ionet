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
 * Provides domain-event infrastructure based on Disruptor for implementing asynchronous event-driven
 * workflows, similar in spirit to Guava EventBus or Spring `ApplicationEvent`.
 *
 * <p>See <a href="https://iohao.github.io/ionet/docs/extension_module/domain_event">domain-event</a>
 * for module usage and integration guidance.
 * Disruptor is an open-source concurrent framework. It is a high-performance queue developed by the British foreign exchange trading company LMAX, which greatly simplifies the difficulty of concurrent program development and won the 2011 Duke’s Program Framework Innovation Award. Disruptor can be understood as a single-machine version of MQ (the lightest and fastest single-machine MQ -- disruptor).
 * <p>
 * Advantages of Event Source Domain Events
 * <pre>
 * 1. Domain-Driven Design, based on LMAX architecture.
 * 2. Single Responsibility Principle, which can achieve the ultimate in system extensibility, high scalability, and low coupling.
 * 3. Asynchronous high concurrency and thread-safe, using disruptor ring buffer to consume business.
 * 4. Writing code using event consumption methods, even if the business is complex, the code will not become messy, and the cost of code maintenance is lower.
 * 5. Flexible customization of business thread models
 * 6. Event domain provided in plugin form, achieving plug-and-play, as interesting as playing Lego bricks.
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
package com.iohao.net.extension.domain;
