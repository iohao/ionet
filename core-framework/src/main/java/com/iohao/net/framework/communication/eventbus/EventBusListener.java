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
package com.iohao.net.framework.communication.eventbus;

/**
 * Event listener, triggered conditions: 1. when a subscriber throws an uncaught exception, 2. when an event message has no corresponding subscriber.
 *
 * @author 渔民小镇
 * @date 2023-12-24
 * @since 21
 */
public interface EventBusListener {
    /**
     * Subscriber exception handling
     *
     * @param e               e
     * @param eventSource     Event source
     * @param eventBusMessage Event message
     */
    void invokeException(Throwable e, Object eventSource, EventBusMessage eventBusMessage);

    /**
     * Listener callback triggered when the event message has no corresponding subscriber
     * <pre>
     * Note: By default, detection only occurs when calling the {@link EventBus#fire} series of methods.
     * </pre>
     *
     * @param eventBusMessage Event message
     * @param eventBus        eventBus
     */
    void emptySubscribe(EventBusMessage eventBusMessage, EventBus eventBus);
}