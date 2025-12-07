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

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.experimental.PackagePrivate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * DomainEventContextParam
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
public class DomainEventSetting {
    /** Domain event consumption */
    @PackagePrivate
    final Set<DomainEventHandler<?>> domainEventHandlerSet = new HashSet<>();

    /// Wait strategy
    ///
    /// | Strategy | Applicable Scenario | Name |
    /// | :--- | :--- | :--- |
    /// | **Locking** | Scenario where CPU resources are scarce, and throughput and latency are not critical | `BlockingWaitStrategy` |
    /// | **Spinning** | Reduces latency by continuously retrying, minimizing system calls caused by thread switching. Recommended for scenarios where threads are bound to a fixed CPU | `BusySpinWaitStrategy` |
    /// | **Spinning + yield + custom strategy** | Scenario where CPU resources are scarce, and throughput and latency are not critical | `PhasedBackoffWaitStrategy` |
    /// | **Spinning + yield + sleep** | A good trade-off between performance and CPU resources. Latency is non-uniform | `SleepingWaitStrategy` |
    /// | **Locking, with timeout limit** | Scenario where CPU resources are scarce, and throughput and latency are not critical | `TimeoutBlockingWaitStrategy` |
    /// | **Spinning + yield + spinning** | A good trade-off between performance and CPU resources. Latency is relatively uniform | `YieldingWaitStrategy` |
    public WaitStrategy waitStrategy = new LiteBlockingWaitStrategy();
    public ProducerType producerType = ProducerType.MULTI;

    public int ringBufferSize = 1024;
    /** Exception handling */
    public ExceptionHandler<Object> exceptionHandler = new DefaultDomainEventExceptionHandler();

    /** Create disruptor */
    public DisruptorCreator disruptorCreator = new DefaultDisruptorCreator();

    public CommonEventProducer commonEventProducer = new DefaultCommonEventProducer();

    @PackagePrivate
    int topicCount;

    /**
     * Add domain event handler, topic defaults to the T type of the interface implementation
     *
     * @param domainEventHandler domain event handler
     */
    public void addEventHandler(DomainEventHandler<?> domainEventHandler) {
        if (Objects.nonNull(domainEventHandler)) {
            domainEventHandlerSet.add(domainEventHandler);
        }
    }
}
