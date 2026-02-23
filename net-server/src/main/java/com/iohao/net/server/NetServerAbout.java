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
import com.iohao.net.framework.communication.FutureManager;
import com.iohao.net.framework.protocol.Server;
import com.iohao.net.framework.protocol.ServerRequestMessage;
import com.iohao.net.framework.toy.IonetBanner;
import com.iohao.net.common.OnFragmentManager;
import com.iohao.net.common.kit.concurrent.TaskKit;
import com.iohao.net.common.kit.exception.ThrowKit;
import com.iohao.net.server.connection.ConnectionManager;
import io.aeron.Aeron;
import io.aeron.FragmentAssembler;
import io.aeron.logbuffer.FragmentHandler;
import lombok.extern.slf4j.Slf4j;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.AgentRunner;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingMillisIdleStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
final class DefaultNetServer implements NetServer {
    final ConnectionManager connectionManager;
    final NetServerAgent netServerAgent;
    final NetServerSetting setting;
    Aeron aeron;

    DefaultNetServer(NetServerSetting setting) {
        this.setting = setting;
        this.aeron = setting.aeron();
        this.connectionManager = setting.connectionManager();
        this.netServerAgent = new NetServerAgent(setting);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Execute configured hooks so peer servers can be notified during JVM shutdown.
            setting.serverShutdownHookList().forEach(hook -> hook.shutdownHook(setting));
        }));
    }

    public void addServer(Server server) {
        if (start) {
            ThrowKit.ofIllegalArgumentException("Please operate before starting");
            return;
        }

        netServerAgent.addServer(server);
    }

    @Override
    public NetServerSetting getNetServerSetting() {
        return this.setting;
    }

    boolean start = false;

    public void onStart() {
        start = true;

        final IdleStrategy idleStrategyClient = new SleepingMillisIdleStrategy();
        final AgentRunner clientAgentRunner = new AgentRunner(idleStrategyClient
                , Throwable::printStackTrace
                , null
                , netServerAgent);

        AgentRunner.startOnThread(clientAgentRunner);
    }
}

@Slf4j
final class NetServerAgent implements Agent {
    final int netId;
    final Aeron aeron;
    final NetServerSetting setting;
    final FutureManager futureManager;
    final FragmentHandler fragmentHandler;
    final List<Server> serverList = new ArrayList<>();
    ConnectionManager connectionManager;
    State state = State.AWAITING_INBOUND_CONNECT;

    NetServerAgent(NetServerSetting setting) {
        this.futureManager = setting.futureManager();
        this.setting = setting;
        this.aeron = setting.aeron();
        this.connectionManager = setting.connectionManager();
        this.netId = setting.netId();

        var adapter = new NetServerAdapter();
        if (CoreGlobalConfig.enableFragmentAssembler) {
            this.fragmentHandler = new FragmentAssembler(adapter);
        } else {
            this.fragmentHandler = adapter;
        }
    }

    @Override
    public void onStart() {
        injectOnFragment();
        this.state = State.CONNECTED;
        this.connectionManager.awaitConnect();

        this.registerServerToCenter();
    }

    @Override
    public int doWork() {
        return this.connectionManager.poll(fragmentHandler);
    }

    @Override
    public String roleName() {
        return "Net";
    }

    public void addServer(Server server) {
        serverList.add(server);
    }

    private void registerServerToCenter() {
        FutureManager futureManager = setting.futureManager();

        for (Server server : this.serverList) {
            var futureId = futureManager.nextFutureId();

            var message = new ServerRequestMessage();
            message.setFutureId(futureId);
            message.setId(server.id());
            message.setName(server.name());
            message.setTag(server.tag());
            message.setServerType(server.serverType());
            message.setNetId(server.netId());
            message.setIp(server.ip());
            message.setCmdMerges(server.cmdMerges());

            var barSkeleton = server.barSkeleton();
            if (barSkeleton != null) {
                barSkeleton.communicationAggregation = setting.communicationAggregation();
                barSkeleton.runners.onStart();
            }

            setting.listenerList().forEach(serverListener -> serverListener.connectBefore(server, setting));
            server.payloadMap().forEach(message::addPayload);

            ServerManager.addServer(server);

            // Register to center after local state/listeners are prepared.
            this.connectionManager.publishMessageToCenter(message);

            CompletableFuture<Integer> future = futureManager.ofFuture(futureId);
            future.thenAcceptAsync(_ -> {
                IonetBanner.addTag(server.tag());
                IonetBanner.countDown();

                if (barSkeleton != null) {
                    barSkeleton.runners.onStartAfter();
                }

                var logicServer = LogicServerManager.remove(server.id());
                if (logicServer != null) {
                    inject(logicServer);
                    logicServer.startupSuccess(barSkeleton);

                }

            }, TaskKit.getNetVirtualExecutor());
        }
    }

    private void injectOnFragment() {
        inject(setting.communicationAggregation());

        for (var fragment : OnFragmentManager.onFragments) {
            inject(fragment);
        }
    }

    private void inject(Object object) {
        if (object instanceof NetServerSettingAware aware) {
            aware.setNetServerSetting(this.setting);
        }
    }

    enum State {
        AWAITING_OUTBOUND_CONNECT,
        CONNECTED,
        READY,
        AWAITING_RESULT,
        AWAITING_INBOUND_CONNECT
    }
}
