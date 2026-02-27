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
 * User-facing request message received from the external Netty pipeline.
 * <p>
 * Extends {@link BarMessage} and implements {@link Request} to carry the full set of
 * request fields: sticky-routing hint, hop count, logic-server bindings, and per-request
 * attachment data. This is the concrete message type created by the external server
 * handlers and forwarded to logic servers via Aeron for action dispatch.
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
@Setter
@Getter
@ToString(callSuper = true)
public final class UserRequestMessage extends BarMessage implements Request {
    int stick;
    int hopCount;
    int[] bindingLogicServerIds;
    byte[] attachment;
}
