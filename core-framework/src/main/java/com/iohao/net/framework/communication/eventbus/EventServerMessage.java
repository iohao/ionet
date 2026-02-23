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

import com.iohao.net.framework.CoreGlobalConfig;
import com.iohao.net.framework.protocol.Server;
import lombok.Getter;

import java.util.Collection;
import java.util.Set;

/**
 * Holds event topic information and server metadata for a logic server participating in the EventBus system.
 * Used to track which topics a given server subscribes to, and whether it is local or remote.
 *
 * @author 渔民小镇
 * @date 2023-12-24
 * @since 21
 */
@Getter
public final class EventServerMessage {
    /** Whether this server is in a remote process. */
    final boolean remote;
    /** The server descriptor. */
    final Server server;
    /** The set of event topic names this server subscribes to. */
    final Set<String> topics;

    /**
     * Create an EventServerMessage for the given server and its subscribed topics.
     *
     * @param server the server descriptor
     * @param topics the set of event topic names the server subscribes to
     */
    public EventServerMessage(Server server, Set<String> topics) {
        this.server = server;
        this.remote = server.netId() != CoreGlobalConfig.getNetId();
        this.topics = topics;
    }

    /**
     * Get the event topic names this server subscribes to.
     *
     * @return collection of topic names
     */
    public Collection<String> getTopics() {
        return topics;
    }

    /**
     * Get the name of this server.
     *
     * @return the server name
     */
    public String getName() {
        return server.name();
    }

    /**
     * Get the server ID.
     *
     * @return the server ID
     */
    public int getServerId() {
        return server.id();
    }
}
