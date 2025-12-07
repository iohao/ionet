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
package com.iohao.net.external.core.micro;

import java.util.Objects;

/**
 * Pipeline Context
 *
 * @author 渔民小镇
 * @date 2023-02-19
 */
public interface PipelineContext {
    /**
     * Adds the handler to the first position.
     *
     * @param handler The handler
     */
    default void addFirst(Object handler) {
        Objects.requireNonNull(handler);
        String simpleName = handler.getClass().getSimpleName();
        this.addFirst(simpleName, handler);
    }

    /**
     * Adds the handler to the first position.
     *
     * @param name    The name of the handler
     * @param handler The handler
     */
    void addFirst(String name, Object handler);

    /**
     * Adds the handler to the last position.
     *
     * @param handler The handler
     */
    default void addLast(Object handler) {
        Objects.requireNonNull(handler);
        String simpleName = handler.getClass().getSimpleName();
        this.addLast(simpleName, handler);
    }

    /**
     * Adds the handler to the last position.
     *
     * @param name    The name of the handler
     * @param handler The handler
     */
    void addLast(String name, Object handler);

    /**
     * Removes the specified handler.
     *
     * @param name The name of the handler
     */
    void remove(String name);
}
