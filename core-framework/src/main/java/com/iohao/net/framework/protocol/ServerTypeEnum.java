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
package com.iohao.net.framework.protocol;

import lombok.Getter;

/**
 * Enumeration of server types within the ionet cluster.
 * <p>
 * Each server is classified as either a {@link #LOGIC} server (handles business
 * logic) or an {@link #EXTERNAL} server (handles client-facing connections).
 *
 * @author 渔民小镇
 * @date 2025-08-25
 * @since 25.1
 */
public enum ServerTypeEnum {
    /** Logic server that processes business actions. */
    LOGIC((byte) 0),
    /** External (Netty) server that manages client connections. */
    EXTERNAL((byte) 1);

    @Getter
    final byte value;

    ServerTypeEnum(byte value) {
        this.value = value;
    }

    /**
     * Resolve a {@link ServerTypeEnum} from its byte value.
     *
     * @param value the byte value (0 for LOGIC, 1 for EXTERNAL)
     * @return the corresponding enum constant
     */
    public static ServerTypeEnum valueOf(byte value) {
        return value == 0 ? LOGIC : EXTERNAL;
    }
}
