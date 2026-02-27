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
package com.iohao.net.external.core.netty.micro.join;

import com.iohao.net.external.core.*;
import com.iohao.net.external.core.config.*;
import com.iohao.net.external.core.micro.*;
import com.iohao.net.external.core.netty.micro.*;

/**
 * Transport selector that configures WebSocket defaults for Netty external servers.
 *
 * @author 渔民小镇
 * @date 2023-05-29
 */
public final class WebSocketExternalJoinSelector implements ExternalJoinSelector {
    @Override
    public ExternalJoinEnum getExternalJoinEnum() {
        return ExternalJoinEnum.WEBSOCKET;
    }

    @Override
    public void defaultSetting(ExternalServerBuilderSetting builderSetting) {
        ExternalJoinSelectorKit.defaultSetting(builderSetting);

        var microBootstrapFlow = builderSetting.getMicroBootstrapFlow();
        if (microBootstrapFlow == null) {
            builderSetting.setMicroBootstrapFlow(new WebSocketMicroBootstrapFlow());
        }
    }
}
