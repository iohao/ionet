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


import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Disruptor event handler adapter that delegates to {@link DomainEventHandler}.
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
record CommonEventHandler(DomainEventHandler<?> eventHandler) implements EventHandler<CommonEvent> {
    @Override
    public void onEvent(CommonEvent event, long sequence, boolean endOfBatch) {
        if (event.eventSource) {
            eventHandler.onEvent(event.getDomainEventSource(), sequence, endOfBatch);
        } else {
            eventHandler.onEvent(event.getValue(), sequence, endOfBatch);
        }
    }
}

/**
 * Default disruptor-backed implementation of {@link CommonEventProducer}.
 *
 * @author 渔民小镇
 * @date 2025-10-20
 * @since 25.1
 */
final class DefaultCommonEventProducer implements CommonEventProducer {

    private static final EventTranslatorOneArg<CommonEvent, DomainEventSource> TRANSLATOR_SOURCE = (commonEvent, _, domainSource) -> {
        commonEvent.setDomainEventSource(domainSource);
    };

    private static final EventTranslatorOneArg<CommonEvent, Object> TRANSLATOR_VALUE = (commonEvent, _, value) -> {
        commonEvent.setValue(value);
    };

    @Override
    public void onData(Object domainSource, Class<?> topic, boolean eventSource) {
        var disruptor = DisruptorManager.getDisruptor(topic);

        if (Objects.isNull(disruptor)) {
            throw new NullPointerException("No processor configured for domain event: " + topic + ".");
        }

        if (eventSource) {
            disruptor.publishEvent(TRANSLATOR_SOURCE, (DomainEventSource) domainSource);
        } else {
            disruptor.publishEvent(TRANSLATOR_VALUE, domainSource);
        }
    }
}

/**
 * Default {@link DisruptorCreator} that creates daemon threads per topic pipeline.
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
@Slf4j
class DefaultDisruptorCreator implements DisruptorCreator {
    private static final AtomicInteger THREAD_INIT_NUMBER = new AtomicInteger(1);

    @Override
    public Disruptor<CommonEvent> ofDisruptor(Class<?> topic, DomainEventSetting setting) {
        
        return new Disruptor<>(CommonEvent::new, setting.ringBufferSize, r -> {
            var thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName(String.format("%s-%s-%s",
                    topic.getSimpleName()
                    , setting.topicCount
                    , THREAD_INIT_NUMBER.getAndIncrement()
            ));

            return thread;
        }, setting.producerType, setting.waitStrategy);
    }
}

/**
 * Default disruptor exception handler for domain-event pipelines.
 *
 * @author 渔民小镇
 * @date 2022-01-14
 */
@Slf4j
class DefaultDomainEventExceptionHandler implements ExceptionHandler<Object> {
    @Override
    public void handleEventException(Throwable ex, long sequence, Object event) {
        log.error(ex.getMessage(), ex);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        log.error(ex.getMessage(), ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        log.error(ex.getMessage(), ex);
    }
}
