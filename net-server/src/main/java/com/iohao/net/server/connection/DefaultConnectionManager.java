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
package com.iohao.net.server.connection;

import com.iohao.net.common.*;
import com.iohao.net.common.kit.*;
import com.iohao.net.framework.*;
import io.aeron.*;
import io.aeron.logbuffer.*;
import java.util.*;
import lombok.extern.slf4j.*;

/**
 * Default Aeron-based {@link ConnectionManager} implementation for net-server peers.
 *
 * @author 渔民小镇
 * @date 2025-09-06
 * @since 25.1
 */
@Slf4j
public final class DefaultConnectionManager implements ConnectionManager {
    /** key: serverId */
    final Map<Integer, ConnectionItem> connectionMap = CollKit.ofConcurrentHashMap();
    final Map<Integer, Publication> publicationMap = CollKit.ofConcurrentHashMap();
    final Map<Integer, String> netIdNameMap = CollKit.ofConcurrentHashMap();
    final int fragmentLimit;

    final Aeron aeron;
    final Subscription subscription;

    final Publication publicationCenter;
    final Publisher publisher;


    public DefaultConnectionManager(ConnectionManagerParameter parameter) {
        this.fragmentLimit = CoreGlobalConfig.fragmentLimit;
        this.aeron = parameter.aeron();
        this.publisher = parameter.publisher();

        // Subscription self
        this.subscription = this.aeron.addSubscription(AeronConst.ipcChannel, parameter.netId());

        // center
        String centerChannel = AeronConst.udpChannel.formatted(parameter.centerIp(), AeronConst.centerPort);
        this.publicationCenter = this.aeron.addExclusivePublication(centerChannel, AeronConst.centerStreamId);
        this.publisher.addPublication(AeronConst.centerPublicationName, this.publicationCenter);
    }

    DefaultConnectionManager(Publisher publisher) {
        this.fragmentLimit = CoreGlobalConfig.fragmentLimit;
        this.aeron = null;
        this.subscription = null;
        this.publicationCenter = null;
        this.publisher = publisher;
    }

    @Override
    public boolean containsNetId(int netId) {
        return publicationMap.containsKey(netId);
    }

    @Override
    public void awaitConnect() {
        while (!this.publicationCenter.isConnected()) {
            this.aeron.context().idleStrategy().idle();
        }
    }

    @Override
    public Publication getPublicationByNetId(int netId) {
        return this.publicationMap.get(netId);
    }

    @Override
    public void publishMessageToCenter(Object message) {
        publisher.publishMessage(AeronConst.centerPublicationName, message);
    }

    @Override
    public void publishMessage(int serverId, Object message) {
        var connection = connectionMap.get(serverId);
        if (connection == null) {
            log.warn("Drop message because server connection is missing. serverId: {}, messageType: {}",
                    serverId,
                    messageType(message));
            return;
        }

        publishMessage(connection.getPubName(), message);
    }

    @Override
    public void publishMessage(String pubName, Object message) {
        if (pubName == null) {
            log.warn("Drop message because publication name is missing. messageType: {}", messageType(message));
            return;
        }

        publisher.publishMessage(pubName, message);
    }

    @Override
    public void publishMessageByNetId(int netId, Object message) {
        String pubName = netIdNameMap.get(netId);
        if (pubName == null) {
            log.warn("Drop message because net id publication is missing. netId: {}, messageType: {}",
                    netId,
                    messageType(message));
            return;
        }

        publishMessage(pubName, message);
    }

    @Override
    public void addConnection(ConnectionItem connection) {
        connectionMap.put(connection.getServerId(), connection);
        publisher.addPublication(connection.getPubName(), connection.getPublication());

        int netId = connection.getNetId();

        netIdNameMap.put(netId, connection.getPubName());
        if (connection.getPublication() != null) {
            publicationMap.put(netId, connection.getPublication());
        }
    }


    @Override
    public int poll(FragmentHandler fragmentHandler) {
        return this.subscription.poll(fragmentHandler, fragmentLimit);
    }

    private String messageType(Object message) {
        return message == null ? "null" : message.getClass().getSimpleName();
    }
}
