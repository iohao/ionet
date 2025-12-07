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
package com.iohao.net.framework.core.flow;

import com.iohao.net.framework.core.BarSkeleton;
import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.communication.CommonDecorator;
import com.iohao.net.framework.communication.CommunicationType;
import com.iohao.net.framework.protocol.Request;
import com.iohao.net.framework.protocol.Server;
import com.iohao.net.common.kit.concurrent.executor.ExecutorRegion;
import com.iohao.net.common.kit.concurrent.executor.ThreadExecutor;

import java.util.concurrent.Executor;

/**
 * FlowCommon
 *
 * @author 渔民小镇
 * @date 2025-10-09
 * @since 25.1
 */
public interface FlowCommon extends CommonDecorator {

    long getUserId();

    void setUserId(long userId);

    Request getRequest();

    BarSkeleton getBarSkeleton();

    CmdInfo getCmdInfo();

    default Server getServer() {
        return this.getBarSkeleton().server;
    }

    default int getServerId() {
        return this.getServer().id();
    }

    default String getTraceId() {
        return this.getRequest().getTraceId();
    }

    default long getThreadIndex() {
        return this.getUserId();
    }

    @Override
    default Executor getCurrentExecutor() {
        return getCurrentThreadExecutor().executor();
    }

    ThreadExecutor getCurrentThreadExecutor();

    default ExecutorRegion getExecutorRegion() {
        return this.getBarSkeleton().executorRegion;
    }

    default ThreadExecutor getUserThreadExecutor() {
        return this.getExecutorRegion().getUserThreadExecutor(this.getThreadIndex());
    }

    default ThreadExecutor getVirtualThreadExecutor() {
        return this.getExecutorRegion().getUserVirtualThreadExecutor(this.getThreadIndex());
    }

    default void execute(Runnable command) {
        this.getCurrentThreadExecutor().executeTry(command);
    }

    default void executeUser(Runnable command) {
        this.getUserThreadExecutor().executeTry(command);
    }

    default void executeVirtual(Runnable command) {
        this.getVirtualThreadExecutor().execute(command);
    }

    void setMethodResult(Object data);

    CommunicationType getCommunicationType();
}
