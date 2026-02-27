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

import lombok.*;

/**
 * Request message sent from one server to another within the cluster.
 * <p>
 * Extends {@link ServerMessage} with a future correlation id so that the
 * caller can match the asynchronous response back to this request.
 *
 * @author 渔民小镇
 * @date 2025-09-05
 * @since 25.1
 */
@Getter
@Setter
@ToString(callSuper = true)
public final class ServerRequestMessage extends ServerMessage {
    /** Correlation id used to match the asynchronous response to this request. */
    long futureId;
}
