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

import java.lang.invoke.MethodHandle;

/**
 * Represents a single event subscriber method bound to an {@link EventBusSubscriber} instance.
 *
 * @author 渔民小镇
 * @date 2023-12-24
 * @see EventSubscribe
 * @since 21
 */
public final class Subscriber {
   /** Unique subscriber ID assigned by the framework. */
   public final int id;
   /** The subscriber object that contains the annotated method. */
   public Object eventBusSubscriber;
   /** The method handle for reflective invocation of the subscriber method. */
   public MethodHandle methodHandle;
    /** Execution order (priority) */
   public int order;
    /** Executor selection strategy */
   public ExecutorSelector executorSelect;
    /** Subscriber invocation */
   public SubscriberInvoke subscriberInvoke;
    /** EventBus */
   public EventBus eventBus;

    /**
     * Create a subscriber with the given unique ID.
     *
     * @param id the unique subscriber ID
     */
    public Subscriber(int id) {
        this.id = id;
    }
}
