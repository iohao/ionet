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
package com.iohao.net.external.core.hook;

import com.iohao.net.external.core.session.*;
import com.iohao.net.framework.protocol.*;

/**
 * Heartbeat/idle event callback hook for external user sessions.
 *
 * @author 渔民小镇
 * @date 2023-02-18
 */
public interface IdleHook<IdleEvent> {
    /**
     * Heartbeat event callback.
     * <p>
     * You only need to handle your business logic here, such as notifying other players in the room that the user has gone offline.
     *
     * @param userSession target user session
     * @param event       idle event payload from the transport implementation
     * @return true to disconnect the player
     */
    boolean callback(UserSession userSession, IdleEvent event);

    /**
     * Callback before heartbeat response (pong).
     * Developers can add some extra information to the heartbeat message, such as the current time.
     *
     * @param idleMessage heartbeat response message that will be sent to the client
     */
    default void pongBefore(CommunicationMessage idleMessage) {
    }
}
