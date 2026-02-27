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
import lombok.*;
import lombok.experimental.*;

/**
 * Event subscription sends {@link CommonEvent} to {@link RingBuffer}
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
@SuppressWarnings("unchecked")
@FieldDefaults(level = AccessLevel.PACKAGE)
public final class CommonEvent {

    /**
     * Object that does not implement the {@link DomainEventSource} interface
     */
    Object value;

    /**
     * Whether it is a wrapped domain event
     * <pre>
     * true Object that implements the {@link DomainEventSource} interface
     * false Object that does not implement the {@link DomainEventSource} interface
     * </pre>
     */
    public boolean eventSource = true;

    /**
     * Domain event
     */
    DomainEventSource domainEventSource;

    /**
     * Get the domain event source object
     *
     * @param <T> source
     * @return Event source object
     */
    public <T> T getDomainEventSource() {
        return (T) domainEventSource;
    }

    /**
     * Object that does not implement the {@link DomainEventSource} interface
     *
     * @param <T> T
     * @return value
     */
    public <T> T getValue() {
        return (T) value;
    }

    /**
     * Object that does not implement the {@link DomainEventSource} interface
     *
     * @param value value
     */
    public void setValue(Object value) {
        this.eventSource = false;
        this.value = value;
    }

    /**
     * Set the domain event source
     *
     * @param domainEventSource Domain event source
     */
    public void setDomainEventSource(DomainEventSource domainEventSource) {
        this.eventSource = true;
        this.domainEventSource = domainEventSource;
    }
}
