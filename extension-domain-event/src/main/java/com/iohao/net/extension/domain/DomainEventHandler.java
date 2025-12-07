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

/**
 * Interface for domain event consumption, receives a domain event
 *
 * @param <T> T domain entity
 * @author 渔民小镇
 * @date 2021-12-26
 */
@FunctionalInterface
public interface DomainEventHandler<T> {

    /**
     * Event handling
     *
     * @param event      domain entity
     * @param endOfBatch endOfBatch
     */
    void onEvent(final T event, final boolean endOfBatch);

    /**
     * Event handling
     *
     * @param event      domain entity
     * @param sequence   sequence
     * @param endOfBatch endOfBatch
     */
    default void onEvent(final T event, final long sequence, final boolean endOfBatch) {
        this.onEvent(event, endOfBatch);
    }

    /**
     * Get the domain event name
     *
     * @return domain event name
     */
    default String getName() {
        return this.getClass().getSimpleName();
    }
}
