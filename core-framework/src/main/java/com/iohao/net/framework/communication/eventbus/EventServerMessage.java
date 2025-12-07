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
 * Event bus logic service related information
 *
 * @author 渔民小镇
 * @date 2023-12-24
 * @since 21
 */
@Getter
public final class EventServerMessage {
    final boolean remote;
    final Server server;
    final Set<String> topics;

    public EventServerMessage(Server server, Set<String> topics) {
        this.server = server;
        this.remote = server.netId() != CoreGlobalConfig.getNetId();
        this.topics = topics;
    }

    public Collection<String> getTopics() {
        return topics;
    }

    public String getName() {
        return server.name();
    }

    public int getServerId() {
        return server.id();
    }
}
