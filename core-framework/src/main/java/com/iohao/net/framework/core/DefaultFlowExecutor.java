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

import com.iohao.net.framework.core.flow.*;
import com.iohao.net.framework.core.kit.*;
import lombok.extern.slf4j.*;

/**
 * Default {@link FlowExecutor} implementation that executes the flow pipeline
 * within a {@link ScopedValue} context for thread-scoped FlowContext propagation.
 *
 * @author 渔民小镇
 * @date 2021-12-17
 */
@Slf4j
final class DefaultFlowExecutor implements FlowExecutor {
    @Override
    public void execute(final FlowContext flowContext, final BarSkeleton barSkeleton) {
        ScopedValue.where(FlowContextKeys.FLOW_CONTEXT, flowContext).run(() -> {
            try {
                FlowExecutorKit.execute(flowContext, barSkeleton);
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        });
    }
}
