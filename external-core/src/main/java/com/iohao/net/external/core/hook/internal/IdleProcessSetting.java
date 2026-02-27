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
package com.iohao.net.external.core.hook.internal;

import com.iohao.net.external.core.hook.*;
import java.util.concurrent.*;

/**
 * Immutable heartbeat/idle processing configuration for an external server.
 *
 * @param pong whether to respond to heartbeat requests with a pong message
 * @param readerIdleTime reader-idle timeout
 * @param writerIdleTime writer-idle timeout
 * @param allIdleTime all-idle timeout
 * @param timeUnit time unit used by idle timeout values
 * @param idleHook heartbeat callback hook
 * @author 渔民小镇
 * @date 2025-10-16
 * @since 25.1
 */
public record IdleProcessSetting(
        boolean pong
        , long readerIdleTime
        , long writerIdleTime
        , long allIdleTime
        , TimeUnit timeUnit
        , IdleHook<?> idleHook
) {
    /**
     * Create a mutable builder with framework defaults.
     *
     * @return idle process setting builder
     */
    public static IdleProcessSettingBuilder builder() {
        return new IdleProcessSettingBuilder();
    }
}
