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

import com.iohao.net.common.kit.concurrent.executor.ExecutorRegion;

/**
 * Subscriber thread executor selection strategy.
 * <p>
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
 * @date 2024-01-11
 * @see EventSubscribe
 * @since 21
 */
public enum ExecutorSelector {
    /**
     * [Thread Safe] Execute in the user thread executor
     * <pre>
     * This strategy will use the action's thread executor to ensure that the same user (userId),
     * when consuming events and actions, uses the same thread executor to avoid concurrency issues.
     *
     * Note: Do not perform time-consuming IO-related operations to avoid blocking the consumption of actions.
     * </pre>
     *
     * @see ExecutorRegion#getUserThreadExecutorRegion()
     */
    userExecutor,

    /**
     * Execute in the virtual thread executor
     * <pre>
     * Time-consuming operations can choose this strategy
     * </pre>
     *
     * @see ExecutorRegion#getUserVirtualThreadExecutorRegion()
     */
    userVirtualExecutor,

    /**
     * [Thread Safe] Execute in a thread executor
     * <pre>
     * This strategy will use the Subscriber.id to determine the thread executor, ensuring that the same subscriber method,
     * when consuming events, uses the same thread executor to avoid concurrency issues.
     *
     * Note: Do not perform time-consuming IO-related operations to avoid blocking the consumption of other subscribers.
     *
     * Other supplementary explanation:
     * Subscriber.id is allocated by the framework. This strategy is similar to userExecutor and simpleExecutor.
     * userExecutor and simpleExecutor use userId to determine the thread executor,
     * while methodExecutor uses the subscriber's own Subscriber.id to determine the thread executor (you can think of it as being partitioned by subscriber method).
     * </pre>
     *
     * @see ExecutorRegion#getSimpleThreadExecutorRegion()
     */
    methodExecutor,
    /**
     * [Thread Safe] Execute in a thread executor
     * <pre>
     * This strategy is similar to userExecutor, but uses an independent thread executor ({@link ExecutorRegion#getSimpleThreadExecutorRegion() }).
     * When using, the developer needs to set the value of {@link EventBusMessage#threadIndex} (this value needs to be > 0).
     * </pre>
     *
     * @see ExecutorRegion#getSimpleThreadExecutorRegion()
     */
    simpleExecutor,

    /**
     * Reserved for developers
     * <pre>
     * If the above strategies do not meet the business requirements, developers can implement the {@link SubscribeExecutorStrategy} interface for custom extension
     * </pre>
     * example
     * <pre>{@code
     * // The logic server adds EventBusRunner to handle EventBus related business
     * builder.addRunner(new AbstractEventBusRunner() {
     *     @Override
     *     public void registerEventBus(EventBus eventBus, BarSkeleton skeleton) {
     *         // Your thread executor selection strategy
     *         eventBus.setSubscribeExecutorStrategy(new YourSubscribeExecutorStrategy());
     *         }
     *     });
     * }
     * </pre>
     *
     * @see SubscribeExecutorStrategy
     */
    customExecutor
}