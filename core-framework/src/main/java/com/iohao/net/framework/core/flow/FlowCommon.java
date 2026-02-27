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

import com.iohao.net.common.kit.concurrent.executor.*;
import com.iohao.net.framework.communication.*;
import com.iohao.net.framework.core.*;
import com.iohao.net.framework.protocol.*;
import java.util.concurrent.*;

/**
 * Base interface for flow communication, providing access to the server, request, executor,
 * and communication infrastructure.
 *
 * @author 渔民小镇
 * @date 2025-10-09
 * @since 25.1
 */
public interface FlowCommon extends CommonDecorator {

    /**
     * Get the user ID associated with this flow.
     *
     * @return the user ID
     */
    long getUserId();

    /**
     * Set the user ID associated with this flow.
     *
     * @param userId the user ID
     */
    void setUserId(long userId);

    /**
     * Get the current request being processed.
     *
     * @return the request
     */
    Request getRequest();

    /**
     * Get the {@link BarSkeleton} execution engine for this flow.
     *
     * @return the bar skeleton
     */
    BarSkeleton getBarSkeleton();

    /**
     * Get the command info (cmd + subCmd) for the current request.
     *
     * @return the command info
     */
    CmdInfo getCmdInfo();

    /**
     * Get the server descriptor from the bar skeleton.
     *
     * @return the server
     */
    default Server getServer() {
        return this.getBarSkeleton().server;
    }

    /**
     * Get the server ID.
     *
     * @return the server ID
     */
    default int getServerId() {
        return this.getServer().id();
    }

    /**
     * Get the trace ID from the current request.
     *
     * @return the trace ID, or {@code null} if not set
     */
    default String getTraceId() {
        return this.getRequest().getTraceId();
    }

    /**
     * Get the thread index used for executor selection. Defaults to the user ID, ensuring
     * that requests from the same user are dispatched to the same thread.
     *
     * @return the thread index
     */
    default long getThreadIndex() {
        return this.getUserId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Executor getCurrentExecutor() {
        return getCurrentThreadExecutor().executor();
    }

    /**
     * Get the current thread executor for this flow context.
     *
     * @return the current thread executor
     */
    ThreadExecutor getCurrentThreadExecutor();

    /**
     * Get the executor region from the bar skeleton.
     *
     * @return the executor region
     */
    default ExecutorRegion getExecutorRegion() {
        return this.getBarSkeleton().executorRegion;
    }

    /**
     * Get the user-bound thread executor, selected by the thread index.
     *
     * @return the user thread executor
     */
    default ThreadExecutor getUserThreadExecutor() {
        return this.getExecutorRegion().getUserThreadExecutor(this.getThreadIndex());
    }

    /**
     * Get the virtual-thread executor bound to the current user's thread index.
     *
     * @return the virtual thread executor
     */
    default ThreadExecutor getVirtualThreadExecutor() {
        return this.getExecutorRegion().getUserVirtualThreadExecutor(this.getThreadIndex());
    }

    /**
     * Execute a task on the current thread executor.
     *
     * @param command the task to execute
     */
    default void execute(Runnable command) {
        this.getCurrentThreadExecutor().executeTry(command);
    }

    /**
     * Execute a task on the user-bound thread executor.
     *
     * @param command the task to execute
     */
    default void executeUser(Runnable command) {
        this.getUserThreadExecutor().executeTry(command);
    }

    /**
     * Execute a task on the virtual-thread executor.
     *
     * @param command the task to execute
     */
    default void executeVirtual(Runnable command) {
        this.getVirtualThreadExecutor().execute(command);
    }

    /**
     * Set the result data produced by the action method.
     *
     * @param data the method result
     */
    void setMethodResult(Object data);

    /**
     * Get the communication type of the current flow (e.g., user request, internal call).
     *
     * @return the communication type
     */
    CommunicationType getCommunicationType();
}
