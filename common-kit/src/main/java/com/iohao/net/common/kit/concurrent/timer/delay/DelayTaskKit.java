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
import java.util.*;
import java.util.function.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Lightweight controllable delayed task utility class
 *
 * @author 渔民小镇
 * @date 2024-09-01
 * @since 21.16
 */
@UtilityClass
public class DelayTaskKit {
    /** Lightweight controllable delayed task region interface */
    @Getter
    DelayTaskRegion delayTaskRegion = new SimpleDelayTaskRegion();

    /**
     * Set the lightweight controllable delayed task region
     *
     * @param delayTaskRegion delayTaskRegion
     */
    public void setDelayTaskRegion(DelayTaskRegion delayTaskRegion) {
        Objects.requireNonNull(delayTaskRegion);

        var delayTaskRegionOld = DelayTaskKit.delayTaskRegion;
        DelayTaskKit.delayTaskRegion = delayTaskRegion;

        if (delayTaskRegionOld instanceof DelayTaskRegionEnhance stop) {
            stop.stop();
        }
    }

    /**
     * Cancel the task by taskId
     *
     * @param taskId taskId
     */
    public void cancel(String taskId) {
        delayTaskRegion.cancel(taskId);
    }

    /**
     * get Optional DelayTask by taskId
     *
     * @param taskId taskId
     * @return Optional DelayTask
     */
    public Optional<DelayTask> optional(String taskId) {
        return delayTaskRegion.optional(taskId);
    }

    /**
     * If taskId is present, execute the given action
     *
     * @param taskId   taskId
     * @param consumer the given action
     */
    public void ifPresent(String taskId, Consumer<DelayTask> consumer) {
        DelayTaskKit.optional(taskId).ifPresent(consumer);
    }

    /**
     * Create a lightweight controllable delayed task
     *
     * @param taskListener task listener callback
     * @return lightweight controllable delayed task
     */
    public DelayTask of(TaskListener taskListener) {
        return delayTaskRegion.of(taskListener);
    }

    /**
     * Create a lightweight controllable delayed task
     *
     * @param taskId       taskId (If taskId is the same, it will overwrite the previous delayed task)
     * @param taskListener task listener callback
     * @return lightweight controllable delayed task
     */
    public DelayTask of(String taskId, TaskListener taskListener) {
        return delayTaskRegion.of(taskId, taskListener);
    }
}
