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

import com.iohao.net.external.core.ExternalServerBuilderSetting;
import com.iohao.net.external.core.netty.SettingOption;
import com.iohao.net.external.core.netty.handler.UserRequestHandler;
import com.iohao.net.external.core.netty.handler.SocketCmdAccessAuthHandler;
import com.iohao.net.external.core.netty.handler.SocketIdleHandler;
import com.iohao.net.external.core.netty.handler.SocketUserSessionHandler;
import com.iohao.net.external.core.netty.hook.DefaultSocketIdleHook;
import com.iohao.net.external.core.netty.micro.SocketMicroBootstrap;
import com.iohao.net.external.core.netty.session.SocketUserSessions;
import lombok.experimental.UtilityClass;

/**
 * ExternalJoinSelectorKit
 *
 * @author 渔民小镇
 * @date 2025-10-16
 * @since 25.1
 */
@UtilityClass
final class ExternalJoinSelectorKit {
    public void defaultSetting(ExternalServerBuilderSetting setting) {
        if (setting.getMicroBootstrap() == null) {
            setting.setMicroBootstrap(new SocketMicroBootstrap());
        }

        if (setting.getUserSessions() == null) {
            setting.setUserSessions(new SocketUserSessions());
        }

        var idleProcessSettingBuilder = setting.getIdleProcessSettingBuilder();
        if (idleProcessSettingBuilder != null) {
            if (idleProcessSettingBuilder.getIdleHook() == null) {
                idleProcessSettingBuilder.setIdleHook(new DefaultSocketIdleHook());
            }

            setting.ifNull(SettingOption.socketIdleHandler, SocketIdleHandler::new);
        }

        // pipelineCustom Handler
        setting.ifNull(SettingOption.socketUserSessionHandler, SocketUserSessionHandler::new);
        setting.ifNull(SettingOption.socketCmdAccessAuthHandler, SocketCmdAccessAuthHandler::new);
        setting.ifNull(SettingOption.userRequestHandler, UserRequestHandler::new);
    }
}
