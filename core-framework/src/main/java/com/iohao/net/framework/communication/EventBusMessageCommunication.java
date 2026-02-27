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
package com.iohao.net.framework.communication;

import com.iohao.net.framework.annotations.*;
import com.iohao.net.framework.communication.eventbus.*;

/**
 * Communication interface for dispatching {@link EventBusMessage} events to remote logic servers.
 * <p>
 * Implementations are responsible for serializing and transmitting event bus messages
 * across the network so that subscribers on other logic servers can process them.
 *
 * @author 渔民小镇
 * @date 2025-09-20
 * @since 25.1
 */
@Enterprise
public interface EventBusMessageCommunication {
    /**
     * Fire an event bus message to remote logic servers.
     *
     * @param message the event bus message to dispatch remotely
     */
    void fireRemote(EventBusMessage message);
}
