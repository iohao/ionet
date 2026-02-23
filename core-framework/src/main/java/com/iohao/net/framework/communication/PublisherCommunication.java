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
package com.iohao.net.framework.communication;

/**
 * Interface for publishing messages to other servers in the cluster.
 * <p>
 * Supports targeting servers by net ID, server ID, or logical name.
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
public interface PublisherCommunication {
    /**
     * Publish a message to a server identified by its net ID.
     *
     * @param netId   the net ID of the target server
     * @param message the message to publish
     */
    void publishMessageByNetId(int netId, Object message);

    /**
     * Publish a message to a server identified by its server ID.
     *
     * @param serverId the server ID of the target server
     * @param message  the message to publish
     */
    void publishMessage(int serverId, Object message);

    /**
     * Publish a message to a server identified by its logical name.
     *
     * @param name    the logical name of the target server
     * @param message the message to publish
     */
    void publishMessage(String name, Object message);
}
