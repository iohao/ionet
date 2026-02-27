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
import lombok.*;
import lombok.experimental.*;

/**
 * Builder for {@link IdleProcessSetting} heartbeat/idle configuration.
 *
 * @author 渔民小镇
 * @date 2023-02-18
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class IdleProcessSettingBuilder {
    /** Overall heartbeat time */
    long idleTime = 300;
    /** Read - heartbeat time */
    long readerIdleTime = idleTime;
    /** Write - heartbeat time */
    long writerIdleTime = idleTime;
    /** All - heartbeat time */
    long allIdleTime = idleTime;
    /** Heartbeat time unit - defaults to seconds */
    TimeUnit timeUnit = TimeUnit.SECONDS;
    /** true : respond to the client with a heartbeat (pong) */
    boolean pong = true;
    /** Heartbeat hook */
    IdleHook<?> idleHook;

    /**
     * Overall heartbeat time setting, includes: readerIdleTime, writerIdleTime, and allIdleTime
     *
     * @param idleTime Overall time
     * @return this
     */
    public IdleProcessSettingBuilder setIdleTime(long idleTime) {
        this.idleTime = idleTime;
        this.readerIdleTime = idleTime;
        this.writerIdleTime = idleTime;
        this.allIdleTime = idleTime;
        return this;
    }

    /**
     * Build an immutable idle-processing setting snapshot.
     *
     * @return idle process setting
     */
    public IdleProcessSetting ofIdleProcessSetting() {
        return new IdleProcessSetting(pong
                , readerIdleTime
                , writerIdleTime
                , allIdleTime
                , timeUnit
                , idleHook
        );
    }
}
