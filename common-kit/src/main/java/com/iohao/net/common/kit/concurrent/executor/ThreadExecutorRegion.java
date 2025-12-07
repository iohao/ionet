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
package com.iohao.net.common.kit.concurrent.executor;

/**
 * ThreadExecutorRegion
 *
 * @author 渔民小镇
 * @date 2023-12-01
 */
public interface ThreadExecutorRegion {
    /**
     * Get the Executor based on the index.
     *
     * @param index index index
     * @return Executor Executor
     */
    ThreadExecutor getThreadExecutor(long index);

    /**
     * Get the Executor by index to run the task.
     *
     * @param runnable runnable
     * @param index    index
     */
    default void execute(Runnable runnable, long index) {
        this.getThreadExecutor(index).execute(runnable);
    }
}
