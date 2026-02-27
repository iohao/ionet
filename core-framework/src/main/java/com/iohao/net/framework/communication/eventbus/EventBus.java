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

import com.iohao.net.common.kit.concurrent.executor.*;
import com.iohao.net.framework.communication.*;
import com.iohao.net.framework.core.flow.*;
import java.util.*;

/**
 * EventBus, the relationship between EventBus, the business framework, and the logic server is 1:1:1.
 * <p>
 * When publishing events:
 * <pre>
 * 1. If the relevant subscribers are within the same process, synchronous and asynchronous sending can be controlled.
 * 2. If the relevant subscribers are not in the same process but are distributed across different processes, only asynchronous sending is possible (even if a synchronous method is used to publish the event).
 *
 * 【Synchronous】 here means: when publishing an event, the main logic will only continue to proceed after the relevant subscribers have completed their execution.
 * 【Asynchronous】 here means: when publishing an event, the main logic will not be blocked, and the relevant subscribers will execute in other threads.
 *
 * Whether synchronous or asynchronous, when the relevant subscribers execute the logic server, it is thread-safe by default; this is because the subscriber {@link EventSubscribe} uses the user thread executor by default.
 * </pre>
 * <p>
 * Related examples for obtaining EventBus
 * <p>
 * for example 1 - Get the corresponding eventBus through FlowContext
 * <pre>{@code
 * EventBus eventBus = flowContext.getEventBus();
 * eventBus.fire(userLoginEventMessage);
 * }
 * </pre>
 * <p>
 * The fire series provides multiple types of event publishing mechanisms:
 * <pre>
 * 1. fire sends events to subscribers, which include:
 *     a. Sending event messages to subscribers of all logic servers in the current process.
 *     b. Sending event messages to subscribers in other processes.
 * 2. fireLocal sends event messages to subscribers of all logic servers in the current process.
 * 3. fireMe sends event messages only to the subscribers of the current EventBus.
 * 4. fireAny sends events to subscribers, which include:
 *     a. Sending event messages to subscribers of all logic servers in the current process.
 *     b. Sending event messages to subscribers in other processes.
 *     c. When there are multiple logic server instances of the same type, the event will only be sent to one of the logic servers of that type.
 *
 * The fire series provides multiple types of event publishing mechanisms. The above methods are asynchronous by default, and the corresponding synchronous methods are named as fireXXXSync.
 * </pre>
 * Convenient usage - {@link FlowContext}
 * <pre>
 * In addition to publishing events via EventBus, the framework also provides EventBus-related methods in {@link FlowContext}.
 * FlowContext internally uses EventBus to publish events.
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-24
 * @see FlowContext#getEventBus()
 * @see EventBusRegion
 * @see FlowContext
 * @since 21
 */
public interface EventBus {
    /**
     * Get the unique ID of this EventBus instance.
     *
     * @return the EventBus instance ID
     */
    int getId();

    /**
     * Register a subscriber
     *
     * @param eventBusSubscriber The subscriber
     */
    void register(Object eventBusSubscriber);

    /**
     * Subscribers corresponding to the event message
     *
     * @param eventBusMessage The event message
     * @return The corresponding subscribers
     */
    Collection<Subscriber> listSubscriber(EventBusMessage eventBusMessage);

    /**
     * All event source topics subscribed by the current eventBus
     *
     * @return All event source topics subscribed by the current eventBus
     */
    Set<String> listTopic();

    void setStatus(EventBusStatus status);

    /**
     * [Asynchronous] Send an event to all subscribers
     * <pre>
     * 1. Send event messages to subscribers of all logic servers in the current process.
     * 2. Send event messages to subscribers in other processes.
     * </pre>
     *
     * @param eventBusMessage The event message
     */
    void fire(EventBusMessage eventBusMessage);

    /**
     * [Synchronous] Send an event to all subscribers
     * <pre>
     * 1. [Synchronous] Send event messages to subscribers of all logic servers in the current process.
     * 2. [Asynchronous] Send event messages to subscribers in other processes.
     *
     * Note that the synchronization here only refers to the synchronization of subscribers within the current process, and is invalid for subscribers in other processes (asynchronous is used for handling remote subscribers).
     * </pre>
     *
     * @param eventBusMessage The event message
     */
    void fireSync(EventBusMessage eventBusMessage);

    /**
     * [Asynchronous] Send an event to all subscribers
     * <pre>
     * 1. Send event messages to subscribers of all logic servers in the current process.
     * 2. Send event messages to subscribers in other processes.
     * </pre>
     *
     * @param eventSource The event source
     */
    default void fire(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fire(eventBusMessage);
    }

    /**
     * [Synchronous] Send an event to all subscribers
     * <pre>
     * 1. [Synchronous] Send event messages to subscribers of all logic servers in the current process.
     * 2. [Asynchronous] Send event messages to subscribers in other processes.
     *
     * Note that the synchronization here only refers to the synchronization of subscribers within the current process, and is invalid for subscribers in other processes (asynchronous is used for handling remote subscribers).
     * </pre>
     *
     * @param eventSource The event source
     */
    default void fireSync(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireSync(eventBusMessage);
    }

    /**
     * [Asynchronous] Send event messages to subscribers of all logic servers in the current process
     *
     * @param eventSource The event source
     */
    default void fireLocal(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireLocal(eventBusMessage);
    }

    /**
     * [Asynchronous] Send event messages to subscribers of all logic servers in the current process
     *
     * @param eventBusMessage The event message
     */
    void fireLocal(EventBusMessage eventBusMessage);

    /**
     * [Synchronous] Send event messages to subscribers of all logic servers in the current process
     *
     * @param eventSource The event source
     */
    default void fireLocalSync(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireLocalSync(eventBusMessage);
    }

    /**
     * [Synchronous] Send event messages to subscribers of all logic servers in the current process
     *
     * @param eventBusMessage The event message
     */
    void fireLocalSync(EventBusMessage eventBusMessage);

    /**
     * [Asynchronous] Send event messages only to the subscribers of the current EventBus
     * <pre>
     * Subscribers that have been registered with {@link EventBus#register(Object)}
     * </pre>
     *
     * @param eventSource The event source
     */
    default void fireMe(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireMe(eventBusMessage);
    }

    /**
     * [Asynchronous] Send event messages only to the subscribers of the current EventBus
     * <pre>
     * Subscribers that have been registered with {@link EventBus#register(Object)}
     * </pre>
     *
     * @param eventBusMessage The event message
     */
    void fireMe(EventBusMessage eventBusMessage);

    /**
     * [Synchronous] Send event messages only to the subscribers of the current EventBus
     * <pre>
     * Subscribers that have been registered with {@link EventBus#register(Object)}
     * </pre>
     *
     * @param eventSource The event source
     */
    default void fireMeSync(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireMeSync(eventBusMessage);
    }

    /**
     * [Synchronous] Send event messages only to the subscribers of the current EventBus
     * <pre>
     * Subscribers that have been registered with {@link EventBus#register(Object)}
     * </pre>
     *
     * @param eventBusMessage The event message
     */
    void fireMeSync(EventBusMessage eventBusMessage);

    /**
     * [Asynchronous] Send event messages to subscribers in the current process and subscribers in remote processes. If multiple logic server instances of the same type exist, the event will only be sent to one of the instances.
     * <pre>
     * Suppose there is a mail logic server for distributing rewards, and we have started two (or more) mail logic server instances to handle the business.
     * When we use the fireAny method to send an event, the event will only be sent to one of the instances.
     * </pre>
     *
     * @param eventSource The event source
     */
    default void fireAny(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireAny(eventBusMessage);
    }

    /**
     * [Asynchronous] Send event messages to subscribers in the current process and subscribers in remote processes. If multiple logic server instances of the same type exist, the event will only be sent to one of the instances.
     * <pre>
     * Suppose there is a mail logic server for distributing rewards, and we have started two (or more) mail logic server instances to handle the business.
     * When we use the fireAny method to send an event, the event will only be sent to one of the instances.
     * </pre>
     *
     * @param eventBusMessage The event message
     */
    void fireAny(EventBusMessage eventBusMessage);

    /**
     * [Synchronous] Send event messages to subscribers in the current process and subscribers in remote processes. If multiple logic server instances of the same type exist, the event will only be sent to one of the instances.
     * <pre>
     * Suppose there is a mail logic server for distributing rewards, and we have started two (or more) mail logic server instances to handle the business.
     * When we use the fireAny method to send an event, the event will only be sent to one of the instances.
     * </pre>
     *
     * @param eventSource The event source
     */
    default void fireAnySync(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireAnySync(eventBusMessage);
    }

    /**
     * [Synchronous] Send event messages to subscribers in the current process and subscribers in remote processes. If multiple logic server instances of the same type exist, the event will only be sent to one of the instances.
     * <pre>
     * Suppose there is a mail logic server for distributing rewards, and we have started two (or more) mail logic server instances to handle the business.
     * When we use the fireAny method to send an event, the event will only be sent to one of the instances.
     * </pre>
     *
     * @param eventBusMessage The event message
     */
    void fireAnySync(EventBusMessage eventBusMessage);

    /**
     * Set the subscriber thread executor selection strategy
     *
     * @param subscribeExecutorStrategy The subscriber thread executor selection strategy
     */
    void setSubscribeExecutorStrategy(SubscribeExecutorStrategy subscribeExecutorStrategy);

    /**
     * Get the subscriber thread executor selection strategy
     *
     * @return The subscriber thread executor selection strategy
     */
    SubscribeExecutorStrategy getSubscribeExecutorStrategy();

    /**
     * Set SubscriberInvokeCreator
     *
     * @param subscriberInvokeCreator SubscriberInvokeCreator
     */
    void setSubscriberInvokeCreator(SubscriberInvokeCreator subscriberInvokeCreator);

    /**
     * Set the event message creator, EventBusMessage creator
     *
     * @param eventBusMessageCreator EventBusMessageCreator
     */
    void setEventBusMessageCreator(EventBusMessageCreator eventBusMessageCreator);

    /**
     * Set the event listener
     *
     * @param eventBusListener The event listener
     */
    void setEventBusListener(EventBusListener eventBusListener);

    /**
     * Get the event listener
     *
     * @return The event listener
     */
    EventBusListener getEventBusListener();

    /**
     * Set the event bus logic server related information
     *
     * @param eventServerMessage The event bus logic server related information
     */
    void setEventServerMessage(EventServerMessage eventServerMessage);

    void setCommunication(EventBusMessageCommunication communication);

    /**
     * Set the executor region
     *
     * @param executorRegion The executor region
     */
    void setExecutorRegion(ExecutorRegion executorRegion);

    /**
     * Get the executor region
     *
     * @return The executor region
     */
    ExecutorRegion getExecutorRegion();

    /**
     * Get the event message creator
     *
     * @return The event message creator
     */
    EventBusMessageCreator getEventBusMessageCreator();

    /**
     * Get the event bus logic server related information
     *
     * @return The event bus logic server related information
     */
    EventServerMessage getEventServerMessage();

    /**
     * Get the event source class for the given topic name.
     *
     * @param topic the topic name
     * @return the event source class associated with the topic
     */
    Class<?> getTopicClass(String topic);

    /**
     * Create an event message
     *
     * @param eventSource The event source
     * @return The event message
     */
    default EventBusMessage createEventBusMessage(Object eventSource) {
        return this.getEventBusMessageCreator().create(eventSource);
    }
}