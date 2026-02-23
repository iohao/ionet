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
import lombok.Setter;
import lombok.ToString;

/**
 * Broadcast message targeting a single user (unicast).
 * <p>
 * Extends {@link BroadcastMessage} with {@link UserIdentity} for user binding and
 * {@link CommonResponse} for error propagation. Used to push data to one specific
 * connected user identified by {@code userId}, routed through the external server
 * identified by {@code externalServerId}.
 *
 * @author 渔民小镇
 * @date 2025-09-03
 * @since 25.1
 */
@Getter
@Setter
@ToString(callSuper = true)
public final class BroadcastUserMessage extends BroadcastMessage implements UserIdentity, CommonResponse {
    long userId;
    boolean verifyIdentity;
    int externalServerId;

    int errorCode;
    String errorMessage;
}
