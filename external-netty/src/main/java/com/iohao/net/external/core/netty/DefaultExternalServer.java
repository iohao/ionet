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
package com.iohao.net.external.core.netty;

import com.iohao.net.common.kit.concurrent.TaskKit;
import com.iohao.net.external.core.*;
import com.iohao.net.external.core.message.ExternalServerSingle;
import com.iohao.net.external.core.micro.MicroBootstrap;
import com.iohao.net.external.core.micro.MicroBootstrapFlow;
import com.iohao.net.server.NetServer;

import java.util.Set;

/**
 * DefaultExternalServer
 *
 * @author 渔民小镇
 * @date 2023-02-19
 */
public class DefaultExternalServer implements ExternalServer {
    ExternalSetting setting;
    MicroBootstrap microBootstrap;
    MicroBootstrapFlow<?> microBootstrapFlow;
    Set<Object> injectSet;

    public DefaultExternalServer(ExternalServerCreatorParameter parameter) {
        this.setting = parameter.setting();
        this.microBootstrap = parameter.microBootstrap();
        this.microBootstrapFlow = parameter.microBootstrapFlow();
        this.injectSet = parameter.injectSet();

        ExternalServerSingle.userSessions = setting.userSessions();
    }

    @Override
    public void startup(NetServer netServer) {

        var server = this.setting.server();
        netServer.addServer(server);

        this.setting.option(ExternalSetting.netServerSetting, netServer.getNetServerSetting());
        this.injectSet.forEach(setting::inject);
        this.injectSet = null;

        TaskKit.executeVirtual(() -> microBootstrap.startup(setting.port(), microBootstrapFlow));
    }
}