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
import lombok.extern.slf4j.*;

/**
 * Default {@link SkeletonThreadPipeline} that dispatches a {@link FlowContext} to the
 * appropriate thread executor based on the request's hop count.
 * <p>
 * Hop-count routing:
 * <ul>
 *   <li>0 -- user thread executor (direct client request)</li>
 *   <li>1 -- simple thread executor (single-hop forwarded request)</li>
 *   <li>2+ -- virtual thread executor (multi-hop request)</li>
 * </ul>
 *
 * @author 渔民小镇
 * @date 2025-09-02
 * @since 25.1
 */
@Slf4j
public final class DefaultSkeletonThreadPipeline implements SkeletonThreadPipeline {
    @Override
    public void execute(BarSkeleton barSkeleton, FlowContext flowContext) {
        var threadIndex = flowContext.getThreadIndex();
        var executorRegion = barSkeleton.executorRegion;
        var request = flowContext.getRequest();

        var threadExecutor = switch (request.getHopCount()) {
            case 0 -> executorRegion.getUserThreadExecutor(threadIndex);
            case 1 -> executorRegion.getSimpleThreadExecutor(threadIndex);
            default -> executorRegion.getUserVirtualThreadExecutor(threadIndex);
        };

        flowContext.setCurrentThreadExecutor(threadExecutor);
        threadExecutor.execute(() -> barSkeleton.handle(flowContext));
    }
}
