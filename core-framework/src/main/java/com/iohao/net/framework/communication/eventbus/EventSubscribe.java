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
package com.iohao.net.framework.communication.eventbus;

import java.lang.annotation.*;

/**
 * Subscriber annotation, marks a method as an event subscriber (receives events, processes events),
 * configurable with thread executor strategy and execution priority. It is thread-safe by default.
 * <pre>
 * The subscriber must have one and only one parameter, used to receive the event source.
 * It is thread-safe by default, using the user thread executor.
 * </pre>
 * for example
 * <pre>{@code
 * public class YourEventBusSubscriber implements EventBusSubscriber {
 *     // Specify the thread executor to execute the subscriber's logic
 *     @EventSubscribe(ExecutorSelector.userExecutor)
 *     public void userLogin(YourEventMessage message) {
 *         log.info("event - User [{}] logged in", message.getUserId());
 *     }
 * }
 *
 * @Data
 * public class YourEventMessage {
 *     final long userId;
 *     public YourEventMessage(long userId) {
 *         this.userId = userId;
 *     }
 * }
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-24
 * @since 21
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventSubscribe {
    /**
     * Executor strategy selection
     */
    ExecutorSelector value() default ExecutorSelector.userExecutor;

    /**
     * The execution order (priority) of the subscriber. A higher value means higher execution priority.
     * <pre>
     * To ensure sequential execution, subscribers need to use the same thread executor.
     * For example, it can be used with strategies like userExecutor, simpleExecutor, etc.
     * These strategies determine the thread executor used via {@link EventBusMessage#threadIndex}.
     * </pre>
     *
     * @return Execution order
     * @see ExecutorSelector
     */
    int order() default 0;
}