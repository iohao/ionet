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
package com.iohao.net.extension.domain;

import com.lmax.disruptor.dsl.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

/**
 * Boots and manages the lifecycle of domain-event disruptor pipelines.
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
public class DomainEventApplication {
    final AtomicBoolean init = new AtomicBoolean();

    /**
     * Initializes disruptors for all registered handlers and starts them once.
     *
     * @param setting domain-event configuration
     */
    public void startup(DomainEventSetting setting) {
        if (!init.compareAndSet(false, true)) {
            return;
        }
        Set<String> topicSet = new HashSet<>();
        var domainEventHandlerSet = setting.domainEventHandlerSet;

        domainEventHandlerSet.stream().collect(Collectors.groupingBy(o -> {
            var parameterizedType = (ParameterizedType) o.getClass().getGenericInterfaces()[0];
            var type = parameterizedType.getActualTypeArguments()[0];
            String typeName = type.getTypeName();
            topicSet.add(typeName);

            try {
                return Class.forName(typeName, true, Thread.currentThread().getContextClassLoader());
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        })).forEach((Class<?> topic, List<DomainEventHandler<?>> eventHandlers) -> {
            setting.topicCount = topicSet.size();
            var disruptor = setting.disruptorCreator.ofDisruptor(topic, setting);
            DisruptorManager.put(topic, disruptor);

            if (Objects.nonNull(setting.exceptionHandler)) {
                disruptor.setDefaultExceptionHandler(setting.exceptionHandler);
            }

            var handlers = eventHandlers.stream().map(CommonEventHandler::new).toList().toArray(new CommonEventHandler[0]);
            disruptor.handleEventsWith(handlers);
        });

        DisruptorManager.commonEventProducer = setting.commonEventProducer;
        DisruptorManager.forEach(Disruptor::start);
        domainEventHandlerSet.clear();
    }

    /**
     * Stops all running disruptors managed by the domain-event extension.
     */
    public void stop() {
        DisruptorManager.listDisruptor().removeIf(disruptor -> {
            disruptor.shutdown();
            return true;
        });
    }
}
