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
package com.iohao.net.common.kit.concurrent;

import java.util.concurrent.*;

/**
 * Task listener callback
 *
 * @author 渔民小镇
 * @date 2024-05-31
 * @since 21.9
 */
interface CommonTaskListener {
    /**
     * Whether to trigger the onUpdate listener callback method
     *
     * @return true to execute the onUpdate method
     */
    default boolean triggerUpdate() {
        return true;
    }

    /**
     * Timer listener callback
     */
    void onUpdate();

    /**
     * Exception callback
     * <pre>
     * When the triggerUpdate or onUpdate method throws an exception, it will be passed here.
     * </pre>
     *
     * @param e e
     */
    default void onException(Throwable e) {
        System.err.println(e.getMessage());
    }

    /**
     * The executor for executing onUpdate
     * <pre>
     * If null is returned, it will be executed within HashedWheelTimer.
     *
     * If there are time-consuming tasks, such as those involving IO operations, it is recommended to specify an executor to run the current callback (onUpdate method) to avoid blocking other tasks.
     * </pre>
     * Example
     * <pre>{@code
     * default Executor getExecutor() {
     * // Time-consuming task, specify an executor to consume the current onUpdate
     * return TaskKit.getCacheExecutor();
     * }
     * }
     * </pre>
     *
     * @return If the return value is null, execution will use the current thread (default HashedWheelTimer), otherwise, the specified executor will be used.
     */
    default Executor getExecutor() {
        return null;
    }
}