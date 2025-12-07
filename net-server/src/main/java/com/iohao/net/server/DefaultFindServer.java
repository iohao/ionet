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

import com.iohao.net.framework.core.exception.ActionErrorEnum;
import com.iohao.net.framework.protocol.Request;
import com.iohao.net.framework.protocol.Server;
import com.iohao.net.server.balanced.LogicServerLoadBalanced;
import com.iohao.net.server.cmd.CmdRegions;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * DefaultFindServer
 *
 * @author 渔民小镇
 * @date 2025-10-11
 * @since 25.1
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
final class DefaultFindServer implements FindServer {
    CmdRegions cmdRegions;
    LogicServerLoadBalanced logicServerLoadBalanced;

    @Override
    public void setNetServerSetting(NetServerSetting setting) {
        this.logicServerLoadBalanced = setting.balancedManager().getLogicBalanced();
        this.cmdRegions = setting.cmdRegions();
    }

    @Override
    public Server getServer(Request message) {
        int cmdMerge = message.getCmdMerge();
        Server server = this.logicServerLoadBalanced.getServerByCmdMerge(cmdMerge);
        if (server == null) {
            message.setOutputError(ActionErrorEnum.cmdInfoErrorCode);
        } else {
            message.setLogicServerId(server.id());
        }

        return server;
    }
}
