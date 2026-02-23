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

import java.util.Collection;

/**
 * Event message carrying the event source data, topic, routing info, and fire type flags
 * for the EventBus system.
 *
 * @author 渔民小镇
 * @date 2023-12-24
 * @since 21
 */
public final class EventBusMessage {
    /** The target server ID this message is destined for. */
    public int serverId;
    /** Thread index used for executor selection in thread-safe strategies. */
    public long threadIndex;
    /** Trace ID for distributed tracing and debugging. */
    public String traceId;
    /** The event topic, typically the fully qualified class name of the event source. */
    public String topic;
    /** Serialized event source data for cross-process transmission. */
    public byte[] data;

    /** The server ID of the EventBus that originally published this event. */
    public transient int sourceServerId;

    /** The deserialized event source object (local only, not serialized). */
    public transient Object eventSource;
    /** Remote server messages from other processes that have matching subscribers. */
    public transient Collection<EventServerMessage> eventServerMessages;

    /** Bitmask of fire types that have been applied to this message. */
    public transient int fireType;

    /**
     * Set the event source and auto-derive the topic from its class name.
     *
     * @param eventSource the event source object
     */
    public void setEventSource(Object eventSource) {
        this.eventSource = eventSource;
        if (this.topic == null) {
            this.topic = eventSource.getClass().getName();
        }
    }

    /**
     * Checks if the type of subscriber that has already been triggered exists
     *
     * @param fireType {@link EventBusFireType}
     * @return true if it exists
     * @see EventBusFireType
     */
    public boolean containsFireType(int fireType) {
        return (this.fireType & fireType) == fireType;
    }

    /**
     * Adds the type of subscriber that has already been triggered
     *
     * @param fireType {@link EventBusFireType}
     * @see EventBusFireType
     */
    public void addFireType(int fireType) {
        this.fireType |= fireType;
    }

    /**
     * Check if no fire type flags have been set.
     *
     * @return true if no fire type has been applied
     */
    public boolean emptyFireType() {
        return fireType == 0;
    }

    /**
     * Create a shallow clone of this message targeting a different server ID.
     *
     * @param serverId the target server ID for the cloned message
     * @return a new EventBusMessage with the same data but a different server ID
     */
    public EventBusMessage ofClone(int serverId) {
        var message = new EventBusMessage();
        message.serverId = serverId;
        message.threadIndex = this.threadIndex;
        message.traceId = this.traceId;
        message.topic = this.topic;
        message.data = this.data;

        return message;
    }
}
