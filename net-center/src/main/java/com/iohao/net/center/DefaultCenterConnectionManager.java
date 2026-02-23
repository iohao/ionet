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
package com.iohao.net.center;

import com.iohao.net.framework.protocol.ServerMessage;
import com.iohao.net.center.creator.CenterConnectionManagerCreatorParameter;
import com.iohao.net.common.kit.CollKit;
import com.iohao.net.common.AeronConst;
import com.iohao.net.common.Publisher;
import io.aeron.Aeron;
import io.aeron.Publication;
import io.aeron.Subscription;
import io.aeron.logbuffer.FragmentHandler;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Default center-server connection manager backed by Aeron publications and subscriptions.
 *
 * @author 渔民小镇
 * @date 2025-09-27
 * @since 25.1
 */
final class DefaultCenterConnectionManager implements CenterConnectionManager {
    final Map<Integer, CenterClientConnection> connectionMap = CollKit.ofConcurrentHashMap();
    final Map<Integer, Publication> publicationMap = CollKit.ofConcurrentHashMap();

    final Aeron aeron;
    Subscription subscription;
    Publisher publisher;

    public DefaultCenterConnectionManager(CenterConnectionManagerCreatorParameter parameter) {
        this.aeron = parameter.aeron();
        this.publisher = parameter.publisher();
        this.init();
    }

    @Override
    public Stream<ServerMessage> streamServerMessage() {
        return connectionMap.values().stream().map(CenterClientConnection::getMessage);
    }

    @Override
    public boolean containsNetId(int netId) {
        return publicationMap.containsKey(netId);
    }

    @Override
    public Publication getPublicationByNetId(int netId) {
        return this.publicationMap.get(netId);
    }

    @Override
    public void addConnection(CenterClientConnection connection) {
        this.publicationMap.put(connection.getNetId(), connection.getPublication());
        this.connectionMap.put(connection.getServerId(), connection);
        this.publisher.addPublication(connection.getPubName(), connection.getPublication());
    }

    @Override
    public void publishMessage(String pubName, Object message) {
        publisher.publishMessage(pubName, message);
    }

    @Override
    public int poll(FragmentHandler fragmentHandler) {
        return this.subscription.poll(fragmentHandler, 1);
    }

    private void init() {
        var channel = AeronConst.udpChannel.formatted("0.0.0.0", AeronConst.centerPort);
        this.subscription = this.aeron.addSubscription(channel, AeronConst.centerStreamId);
//        this.subscription = this.aeron.addSubscription(channel, AeronConst.centerId, image -> {
//
//            log.info("""
//                            A new publisher has connected
//                              channel: {}
//                              streamId: {}
//                              sessionId: {}
//                            """,
//                    image.subscription().channel(),
//                    image.subscription().streamId(),
//                    image.sessionId()
//            );
//        }, image -> {
//            log.info("""
//                            Publisher disconnected
//                              channel: {}
//                              streamId: {}
//                              sessionId: {}
//                            """,
//                    image.subscription().channel(),
//                    image.subscription().streamId(),
//                    image.sessionId()
//            );
//        });
    }
}
