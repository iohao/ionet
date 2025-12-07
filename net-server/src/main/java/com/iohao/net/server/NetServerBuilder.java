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
package com.iohao.net.server;

import com.iohao.net.framework.CoreGlobalConfig;
import com.iohao.net.framework.core.DefaultSkeletonThreadPipeline;
import com.iohao.net.framework.core.SkeletonThreadPipeline;
import com.iohao.net.framework.communication.CommunicationKit;
import com.iohao.net.framework.communication.DefaultFutureManager;
import com.iohao.net.framework.communication.FutureManager;
import com.iohao.net.common.NetCommonGlobalConfig;
import com.iohao.net.common.Publisher;
import com.iohao.net.common.kit.NetworkKit;
import com.iohao.net.server.balanced.*;
import com.iohao.net.server.cmd.CmdRegions;
import com.iohao.net.server.cmd.DefaultCmdRegions;
import com.iohao.net.server.connection.*;
import com.iohao.net.server.connection.ConnectionManagerParameter;
import com.iohao.net.server.creator.CommunicationAggregationCreator;
import com.iohao.net.server.creator.ConnectionManagerCreator;
import com.iohao.net.server.creator.FindServerCreator;
import com.iohao.net.server.creator.NetServerCreator;
import com.iohao.net.server.listener.CmdRegionServerListener;
import com.iohao.net.server.listener.DebugServerListener;
import com.iohao.net.server.listener.ServerListener;
import io.aeron.Aeron;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 *
 * @author 渔民小镇
 * @date 2025-09-08
 * @since 25.1
 */
@Slf4j
@Setter
@Accessors(chain = true)
public final class NetServerBuilder {
    final List<ServerListener> listenerList = new ArrayList<>();
    final List<ServerShutdownHook> serverShutdownHookList = new ArrayList<>();

    CommunicationAggregationCreator communicationAggregationCreator = DefaultCommunicationAggregation::new;
    FindServerCreator findServerCreator = DefaultFindServer::new;
    ConnectionManagerCreator connectionManagerCreator = DefaultConnectionManager::new;
    NetServerCreator netServerCreator = DefaultNetServer::new;

    @Getter
    Aeron aeron;
    int netId;
    @Getter
    Publisher publisher;

    BalancedManager balancedManager;
    SkeletonThreadPipeline skeletonThreadPipeline;
    CmdRegions cmdRegions;
    FutureManager futureManager;
    String centerIp;

    NetServerSettingHook serverSettingHook = _ -> {
    };

    public NetServer build() {
        defaultSetting();

        var communicationAggregation = communicationAggregationCreator.of();
        CommunicationKit.setCommunicationAggregation(communicationAggregation);

        var connectionManager = connectionManagerCreator.of(ConnectionManagerParameter.builder()
                .setAeron(aeron)
                .setPublisher(publisher)
                .setNetId(netId)
                .setCenterIp(centerIp)
                .setFutureManager(futureManager)
                .build()
        );

        var findServer = findServerCreator.of();
        var setting = NetServerSetting.builder()
                .setNetId(netId)
                .setAeron(aeron)
                .setCmdRegions(cmdRegions)
                .setConnectionManager(connectionManager)
                .setSkeletonThreadPipeline(skeletonThreadPipeline)
                .setCommunicationAggregation(communicationAggregation)
                .setFindServer(findServer)
                .setBalancedManager(balancedManager)
                .setFutureManager(futureManager)
                .setListenerList(listenerList)
                .setServerShutdownHookList(serverShutdownHookList)
                .setPublisher(publisher)
                .setConvenientCommunication(new ConvenientCommunication(findServer, publisher))
                .build();

        findServer.setNetServerSetting(setting);

        serverSettingHook.buildComplete(setting);

        return netServerCreator.of(setting);
    }

    public void addServerShutdownHook(ServerShutdownHook hook) {
        Objects.requireNonNull(hook);
        this.serverShutdownHookList.add(hook);
    }

    public void addServerShutdownHook(List<ServerShutdownHook> hookList) {
        if (hookList == null) {
            return;
        }

        for (ServerShutdownHook hook : hookList) {
            this.addServerShutdownHook(hook);
        }
    }

    public void addServerListener(ServerListener serverListener) {
        Objects.requireNonNull(serverListener);
        this.listenerList.add(serverListener);
    }

    private void defaultSetting() {
        Objects.requireNonNull(this.aeron);
        Objects.requireNonNull(communicationAggregationCreator);
        Objects.requireNonNull(findServerCreator);
        Objects.requireNonNull(netServerCreator);
        Objects.requireNonNull(connectionManagerCreator);

        if (this.netId == 0) {
            this.netId = CoreGlobalConfig.getNetId();
        }

        if (this.publisher == null) {
            this.publisher = NetCommonGlobalConfig.getPublisher();
        }

        if (this.skeletonThreadPipeline == null) {
            this.skeletonThreadPipeline = new DefaultSkeletonThreadPipeline();
        }

        if (this.balancedManager == null) {
            this.balancedManager = new DefaultBalancedManager();
        }

        if (this.cmdRegions == null) {
            cmdRegions = new DefaultCmdRegions();
        }

        if (this.futureManager == null) {
            this.futureManager = new DefaultFutureManager();
        }

        if (centerIp == null) {
            centerIp = NetworkKit.LOCAL_IP;
        }

        listenerList.addFirst(new CmdRegionServerListener());

        if (CoreGlobalConfig.devMode) {
            listenerList.addFirst(new DebugServerListener());
        }

        serverShutdownHookList.addLast(new ServerOfflineMessageShutdownHook());
    }
}
