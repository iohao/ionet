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

import lombok.experimental.*;

/**
 * Static facade for publishing domain events through the configured producer.
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
@UtilityClass
public class DomainEventPublish {
    /**
     * Publishes a typed {@link DomainEventSource}.
     *
     * @param domainSource domain event source
     */
    public void send(DomainEventSource domainSource) {
        DisruptorManager.commonEventProducer.onData(domainSource, domainSource.getTopic(), true);
    }

    /**
     * Publishes an arbitrary object as a domain event.
     *
     * @param domainSource domain object or {@link Topic}
     */
    public void send(Object domainSource) {
        if (domainSource instanceof DomainEventSource domainEventSource) {
            send(domainEventSource);
        } else {
            Class<?> topic = domainSource instanceof Topic ? ((Topic) domainSource).getTopic() : domainSource.getClass();
            DisruptorManager.commonEventProducer.onData(domainSource, topic, false);
        }
    }
}
