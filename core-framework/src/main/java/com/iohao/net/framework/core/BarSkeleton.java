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
package com.iohao.net.framework.core;

import com.iohao.net.framework.communication.CommunicationAggregation;
import com.iohao.net.framework.core.flow.*;
import com.iohao.net.framework.core.runner.Runners;
import com.iohao.net.framework.communication.eventbus.EventBus;
import com.iohao.net.framework.protocol.Server;
import com.iohao.net.common.kit.concurrent.executor.ExecutorRegion;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;


/**
 * Central execution engine of the ionet business framework.
 * <p>
 * Holds action commands, flow executor, interceptors, and communication aggregation.
 * Built via {@link BarSkeletonBuilder}.
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
@FieldDefaults(level = AccessLevel.PUBLIC)
@Builder(access = AccessLevel.PACKAGE, builderClassName = "InternalBuilder", builderMethodName = "internalBuilder", setterPrefix = "set")
public final class BarSkeleton {
    final Runners runners;
    final ActionAfter actionAfter;
    final FlowExecutor flowExecutor;
    final ActionMethodInOut[] inOuts;
    final ExecutorRegion executorRegion;
    final ActionCommand[][] actionCommands;
    final ActionMethodInvoke actionMethodInvoke;
    final FlowContextFactory flowContextFactory;
    final ActionCommandRegions actionCommandRegions;
    final ActionFactoryBean<Object> actionFactoryBean;
    final ActionMethodExceptionProcess actionMethodExceptionProcess;

    Server server;
    EventBus eventBus;
    CommunicationAggregation communicationAggregation;

    /**
     * Create a new {@link BarSkeletonBuilder} instance.
     *
     * @return a new builder
     */
    public static BarSkeletonBuilder builder() {
        return new BarSkeletonBuilder();
    }

    /**
     * Handle an incoming request by executing the flow pipeline.
     *
     * @param flowContext the per-request flow context
     */
    public void handle(final FlowContext flowContext) {
        flowContext.setCommunicationAggregation(communicationAggregation);
        flowContext.setBarSkeleton(this);

        this.flowExecutor.execute(flowContext, this);
    }
}
