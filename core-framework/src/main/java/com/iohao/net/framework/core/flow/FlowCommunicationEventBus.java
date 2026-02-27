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
package com.iohao.net.framework.core.flow;

import com.iohao.net.common.kit.exception.*;
import com.iohao.net.framework.annotations.*;
import com.iohao.net.framework.communication.eventbus.*;

/**
 * FlowCommunicationEventBus
 *
 * @author 渔民小镇
 * @date 2025-10-09
 * @since 25.1
 */
@Enterprise
public interface FlowCommunicationEventBus extends FlowCommon {

    default EventBus getEventBus() {
        throw new EnterpriseSupportException();
    }

    default EventBusMessage ofEventBusMessage(Object eventSource) {
        throw new EnterpriseSupportException();
    }

    /**
     * [Async] Sends an event to all subscribers.
     *
     * @param eventSource The event source
     */
    default void fire(Object eventSource) {
        throw new EnterpriseSupportException();
    }

    /**
     * [Sync] Sends an event to all subscribers.
     *
     * @param eventSource The event source
     */
    default void fireSync(Object eventSource) {
        throw new EnterpriseSupportException();
    }

    /**
     * [Async] Sends an event message to subscribers in the current process and remote processes. If multiple logic servers of the same type exist, the event will only be sent to one instance.
     *
     * @param eventSource The event source
     */
    default void fireAny(Object eventSource) {
        throw new EnterpriseSupportException();
    }

    /**
     * Sends an event message to subscribers in the current process and remote processes. If multiple logic servers of the same type exist, the event will only be sent to one instance.
     *
     * @param eventSource The event source
     */
    default void fireAnySync(Object eventSource) {
        throw new EnterpriseSupportException();
    }

    /**
     * [Async] Sends an event message to subscribers of all logic servers in the current process.
     *
     * @param eventSource The event source
     */
    default void fireLocal(Object eventSource) {
        throw new EnterpriseSupportException();
    }

    /**
     * [Sync] Sends an event message to subscribers of all logic servers in the current process.
     *
     * @param eventSource The event source
     */
    default void fireLocalSync(Object eventSource) {
        throw new EnterpriseSupportException();
    }

    /**
     * [Async] Sends an event message only to subscribers of the current EventBus.
     *
     * @param eventSource The event source
     */
    default void fireMe(Object eventSource) {
        throw new EnterpriseSupportException();
    }

    /**
     * [Sync] Sends an event message only to subscribers of the current EventBus.
     *
     * @param eventSource The event source
     */
    default void fireMeSync(Object eventSource) {
        throw new EnterpriseSupportException();
    }
}
