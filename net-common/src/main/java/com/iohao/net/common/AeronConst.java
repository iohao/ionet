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
package com.iohao.net.common;


/**
 * Shared Aeron channel names, stream ids, and default ports used by net-common.
 *
 * @author 渔民小镇
 * @date 2025-08-28
 * @since 25.1
 */
public interface AeronConst {
    /** UDP channel pattern used when an endpoint host/port is required. */
    String udpChannel = "aeron:udp?endpoint=%s:%d";
    /** In-process Aeron IPC channel. */
    String ipcChannel = "aeron:ipc";

    /** Publication name for the center service channel. */
    String centerPublicationName = "centerPublicationName";
    /** Default center service port. */
    int centerPort = 30050;
    /** Stream id used by the center service publication. */
    int centerStreamId = 1;

    /** Default port used by common Aeron components. */
    int port = 30055;
}
