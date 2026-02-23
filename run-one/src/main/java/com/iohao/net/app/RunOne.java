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
package com.iohao.net.app;

import com.iohao.net.framework.core.kit.ActionCommandRegionGlobalCheckKit;
import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.toy.IonetBanner;
import com.iohao.net.center.CenterServerBuilder;
import com.iohao.net.common.NetCommonGlobalConfig;
import com.iohao.net.common.kit.IonetLogName;
import com.iohao.net.common.kit.SafeKit;
import com.iohao.net.external.core.ExternalServer;
import com.iohao.net.common.Publisher;
import com.iohao.net.server.LogicServer;
import com.iohao.net.server.NetServerBuilder;
import com.iohao.net.server.connection.ServerShutdownHook;
import com.iohao.net.server.logic.LogicServerApplication;
import io.aeron.Aeron;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * Coordinates startup for a combined deployment that can host the external server, multiple logic
 * servers, and an optional center server in one process.
 *
 * @author 渔民小镇
 * @date 2025-09-04
 * @since 25.1
 */
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
@Slf4j(topic = IonetLogName.CommonStdout)
public class RunOne {
    Aeron aeron;
    Publisher publisher;

    NetServerBuilder netServerBuilder;
    CenterServerBuilder centerServerBuilder;

    ExternalServer externalServer;
    List<LogicServer> logicServerList;

    boolean enableCenterServer;
    List<ServerShutdownHook> serverShutdownHookList;

    public NetServerBuilder getNetServerBuilder() {
        if (netServerBuilder == null) {
            netServerBuilder = new NetServerBuilder();
        }

        return netServerBuilder;
    }

    public void startup() {
        Objects.requireNonNull(aeron);
        defaultSetting();

        var netServer = netServerBuilder.build();

        banner(this.externalServer == null ? 0 : 1, SafeKit.size(this.logicServerList));

        if (this.externalServer != null) {
            externalServer.startup(netServer);
        }

        if (Objects.nonNull(this.logicServerList)) {
            for (var logicServer : this.logicServerList) {
                var server = LogicServerApplication.startup(logicServer);
                netServer.addServer(server);
            }
        }

        netServer.onStart();
        ActionCommandRegionGlobalCheckKit.detectGlobalDuplicateRoutes();
    }

    protected void defaultSetting() {

        if (publisher == null) {
            publisher = NetCommonGlobalConfig.getPublisher();
        }

        if (publisher != null) {
            publisher.startup();
        }

        if (netServerBuilder == null) {
            netServerBuilder = new NetServerBuilder();
        }

        if (netServerBuilder.getAeron() == null) {
            netServerBuilder.setAeron(aeron);
        }

        if (netServerBuilder.getPublisher() == null) {
            netServerBuilder.setPublisher(publisher);
        }

        netServerBuilder.addServerShutdownHook(this.serverShutdownHookList);

        if (enableCenterServer) {
            if (centerServerBuilder == null) {
                centerServerBuilder = new CenterServerBuilder()
                        .setAeron(aeron)
                        .setPublisher(publisher);
            }

            centerServerBuilder.builder().onStart();
        }
    }

    public static void banner(int externalServerSize, int logicServerSize) {
        int num = 0;
        num += logicServerSize;
        num += externalServerSize;

        IonetBanner.initCountDownLatch(num);

        IonetBanner.render();
    }

    public RunOne enableCenterServer() {
        enableCenterServer = true;
        return this;
    }

    static {
        // Trigger initialization early so command metadata is available before startup wiring.
        CmdInfo.of(0);
    }
}
