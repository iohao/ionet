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

import com.iohao.net.common.kit.concurrent.*;
import java.time.*;

/**
 * <a href="https://iohao.github.io/ionet/docs/kit/delay_task">Lightweight controllable delayed task</a>. The task will be executed after a specified time, can be cancelled, and its delay time can be increased.
 *
 * @author 渔民小镇
 * @date 2024-09-01
 * @since 21.16
 */
public interface DelayTask {

    /**
     * get taskId
     *
     * @return taskId
     */
    String getTaskId();

    /**
     * Get the task listener object
     *
     * @param <T> t
     * @return task listener
     */
    <T extends TaskListener> T getTaskListener();

    /**
     * Whether the task is active
     *
     * @return true if active
     */
    boolean isActive();

    /**
     * Cancel the task
     */
    void cancel();

    /**
     * Remaining delay time in milliseconds
     *
     * @return remaining delay time in milliseconds
     */
    long getMillis();

    /**
     * Increase delay time
     *
     * @param duration duration
     * @return DelayTask
     */
    default DelayTask plusTime(Duration duration) {
        return this.plusTimeMillis(duration.toMillis());
    }

    /**
     * Increase delay time
     * <p>
     * for example
     * <pre>{@code
     * DelayTask delayTask = ...;
     * delayTask.plusTimeMillis(500);  // Increase delay by 0.5 seconds
     * delayTask.plusTimeMillis(-500); // Decrease delay by 0.5 seconds
     * }</pre>
     *
     * @param millis millis (when negative, means decreasing delay time)
     * @return DelayTask
     */
    DelayTask plusTimeMillis(long millis);

    /**
     * Decrease delay time
     * <p>
     * for example
     * <pre>{@code
     * DelayTask delayTask = ...;
     * delayTask.minusTimeMillis(500);  // Decrease delay by 0.5 seconds
     * delayTask.minusTimeMillis(-500); // Increase delay by 0.5 seconds
     * }</pre>
     *
     * @param millis millis (when negative, means increasing delay time)
     * @return DelayTask
     */
    default DelayTask minusTimeMillis(long millis) {
        return this.plusTimeMillis(-millis);
    }

    /**
     * Decrease delay time
     *
     * @param duration duration
     * @return DelayTask
     */
    default DelayTask minusTime(Duration duration) {
        return this.minusTimeMillis(duration.toMillis());
    }

    /**
     * Start the delayed task
     *
     * @return DelayTask
     */
    DelayTask task();
}
