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

import java.util.List;
import java.util.Set;

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
    EventBus getEventBus(int serverId);

    void addLocal(EventBus eventBus);

    /**
     * Gets all subscribers in the current process based on the event message
     *
     * @param eventBusMessage The event message
     * @return All subscribers in the current process
     */
    List<Subscriber> listLocalSubscriber(EventBusMessage eventBusMessage);

    void loadRemoteEventTopic(EventServerMessage eventServerMessage);

    void unloadRemoteTopic(EventServerMessage eventServerMessage);

    Set<EventServerMessage> listRemoteEventServerMessage(EventBusMessage eventBusMessage);
}
