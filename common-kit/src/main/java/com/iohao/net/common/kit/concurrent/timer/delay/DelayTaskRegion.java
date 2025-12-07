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
package com.iohao.net.common.kit.concurrent.timer.delay;

import com.iohao.net.common.kit.concurrent.TaskListener;

import java.util.Optional;

/**
 * Lightweight controllable delayed task region interface, responsible for operations such as creation, retrieval, cancellation, and counting of lightweight controllable delayed tasks.
 *
 * @author 渔民小镇
 * @date 2024-09-01
 * @since 21.16
 */
public interface DelayTaskRegion {

    /**
     * Get an Optional controllable delayed task by taskId
     *
     * @param taskId taskId
     * @return DelayTask Optional
     */
    Optional<DelayTask> optional(String taskId);

    /**
     * Cancel the execution of the controllable delayed task based on taskId.
     *
     * @param taskId taskId
     */
    void cancel(String taskId);

    /**
     * Count the number of current delayed tasks
     *
     * @return the number of current delayed tasks
     */
    int count();

    /**
     * Create a controllable delayed task and set the task listener callback.
     * <pre>{@code
     * DelayTask delayTask = of(taskListener);
     * // Start the delayed task
     * delayTask.task();
     * }
     * </pre>
     *
     * @param taskListener task listener callback
     * @return the controllable delayed task
     */
    DelayTask of(TaskListener taskListener);

    /**
     * Create a controllable delayed task, and set the taskId and task listener callback
     * <pre>{@code
     * DelayTask delayTask = of(taskId, taskListener);
     * // Start the delayed task
     * delayTask.task();
     * }
     * </pre>
     *
     * @param taskId       taskId
     * @param taskListener task listener callback
     * @return the controllable delayed task
     */
    DelayTask of(String taskId, TaskListener taskListener);
}
