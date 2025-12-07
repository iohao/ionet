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
package com.iohao.net.framework.communication;

import com.iohao.net.framework.core.FlowContextKeys;
import com.iohao.net.framework.core.flow.FlowContext;
import com.iohao.net.common.kit.trace.TraceKit;

import java.util.concurrent.Executor;

/**
 * CommonDecorator
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
public interface CommonDecorator {
    default CommunicationAggregation getCommunicationAggregation() {
        return CommunicationKit.communicationAggregation;
    }

    default String getTraceId() {
        return TraceKit.getTraceId();
    }

    default Executor getCurrentExecutor() {
        FlowContext flowContext = FlowContextKeys.getFlowContext();
        return flowContext.getCurrentThreadExecutor().executor();
    }
}
