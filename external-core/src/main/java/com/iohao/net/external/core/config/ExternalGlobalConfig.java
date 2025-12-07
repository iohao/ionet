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
package com.iohao.net.external.core.config;

import com.iohao.net.external.core.hook.AccessAuthenticationHook;
import com.iohao.net.external.core.hook.cache.ExternalCmdCache;
import com.iohao.net.external.core.hook.internal.DefaultAccessAuthenticationHook;
import lombok.experimental.UtilityClass;

/**
 * ExternalGlobalConfig
 *
 * @author 渔民小镇
 * @date 2023-02-19
 */
@UtilityClass
public class ExternalGlobalConfig {
    /** Default startup port for the external server */
    public int externalPort = 10100;

    /** true means enabling simple log printing for netty handler. see SimpleLoggerHandler */
    public boolean enableLoggerHandler = true;

    /** http upgrade websocket protocol path */
    public String websocketPath = "/websocket";

    /** Default packet size */
    public int maxFramePayloadLength = 1024 * 1024;

    /** Access authentication hook interface */
    public AccessAuthenticationHook accessAuthenticationHook = new DefaultAccessAuthenticationHook();

    /** External server route cache */
    public ExternalCmdCache externalCmdCache;

    public UserRequestEnhance userRequestEnhance = _ -> {
    };
}
