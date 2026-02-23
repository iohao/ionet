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
package com.iohao.net.extension.client.kit;

import lombok.experimental.UtilityClass;

/**
 * Configuration flags for simulated client behavior and logging.
 *
 * @author 渔民小镇
 * @date 2023-07-15
 */
@UtilityClass
public class ClientUserConfigs {
    /**
     * Disables console input features.
     * <pre>
     *     Recommended for pressure testing (set this property to {@code true}).
     *
     *     When enabled, all console-input-related features become unavailable.
     * </pre>
     */
    public boolean closeScanner;

    /** Enables logs for broadcast-listen callbacks when true. */
    public boolean openLogListenBroadcast = true;

    /** Enables logs when the client sends requests to the server. */
    public boolean openLogRequestCommand = true;

    /** Enables logs for request callbacks. */
    public boolean openLogRequestCallback = true;

    /**
     * When true, duplicate simulated commands are not allowed.
     * <pre>
     *     Default is {@code false}, which disables duplicate checking.
     * </pre>
     */
    public boolean uniqueInputCommand;

    /** Enables idle/heartbeat callback logs. */
    public boolean openLogIdle = false;

    /**
     * Disables simulated-request related logs.
     */
    public void closeLog() {
        openLogListenBroadcast = false;
        openLogRequestCommand = false;
        openLogRequestCallback = false;
    }
}
