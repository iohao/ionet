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

import java.util.*;

/**
 * Event Bus Management Domain
 * <pre>
 * 1. Manages subscriber information of other processes.
 * 2. If multiple logic services are started within one process, the subscribers of these logic services will be added here.
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-24
 * @since 21
 */
public interface EventBusRegion {
    /**
     * Get the EventBus instance for the given server ID.
     *
     * @param serverId the server ID
     * @return the EventBus instance associated with the server ID
     */
    EventBus getEventBus(int serverId);

    /**
     * Register a local EventBus instance in this region.
     *
     * @param eventBus the EventBus instance to register
     */
    void addLocal(EventBus eventBus);

    /**
     * Gets all subscribers in the current process based on the event message
     *
     * @param eventBusMessage The event message
     * @return All subscribers in the current process
     */
    List<Subscriber> listLocalSubscriber(EventBusMessage eventBusMessage);

    /**
     * Load event topics from a remote server.
     *
     * @param eventServerMessage the remote server message containing topic information
     */
    void loadRemoteEventTopic(EventServerMessage eventServerMessage);

    /**
     * Unload event topics from a disconnected remote server.
     *
     * @param eventServerMessage the remote server message to unload
     */
    void unloadRemoteTopic(EventServerMessage eventServerMessage);

    /**
     * Get remote server messages that have subscribers for the given event.
     *
     * @param eventBusMessage the event message to match against remote subscribers
     * @return set of remote server messages with matching subscribers
     */
    Set<EventServerMessage> listRemoteEventServerMessage(EventBusMessage eventBusMessage);
}
