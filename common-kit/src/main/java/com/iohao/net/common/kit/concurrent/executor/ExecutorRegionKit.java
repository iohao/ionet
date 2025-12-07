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

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

/**
 * The ExecutorRegion utility class functions like a proxy.
 *
 * @author 渔民小镇
 * @date 2024-01-11
 */
@UtilityClass
public class ExecutorRegionKit {
    @Setter
    @Getter
    ExecutorRegion executorRegion = new ExecutorRegion() {
        final ThreadExecutorRegion userThreadExecutorRegion = new UserThreadExecutorRegion();
        final ThreadExecutorRegion simpleThreadExecutorRegion = new SimpleThreadExecutorRegion();
        final ThreadExecutorRegion userVirtualThreadExecutorRegion = new UserVirtualThreadExecutorRegion();

        @Override
        public ThreadExecutorRegion getUserThreadExecutorRegion() {
            return this.userThreadExecutorRegion;
        }

        @Override
        public ThreadExecutorRegion getUserVirtualThreadExecutorRegion() {
            return this.userVirtualThreadExecutorRegion;
        }

        @Override
        public ThreadExecutorRegion getSimpleThreadExecutorRegion() {
            return simpleThreadExecutorRegion;
        }
    };

    public ThreadExecutor getUserThreadExecutor(long index) {
        return executorRegion.getUserThreadExecutor(index);
    }

    public ThreadExecutor getUserVirtualThreadExecutor(long index) {
        return executorRegion.getUserVirtualThreadExecutor(index);
    }

    public ThreadExecutor getSimpleThreadExecutor(long index) {
        return executorRegion.getSimpleThreadExecutor(index);
    }
}
