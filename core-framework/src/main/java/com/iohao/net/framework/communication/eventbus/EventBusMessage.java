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
 * EventBusMessage
 *
 * @author 渔民小镇
 * @date 2023-12-24
 * @since 21
 */
public final class EventBusMessage {
    public int serverId;
    public long threadIndex;
    public String traceId;
    public String topic;
    public byte[] data;

    public transient int sourceServerId;

    public transient Object eventSource;
    /** Information from other processes */
    public transient Collection<EventServerMessage> eventServerMessages;

    public transient int fireType;

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

    public boolean emptyFireType() {
        return fireType == 0;
    }

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
