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
package com.iohao.net.extension.room.operation;

import com.iohao.net.framework.core.flow.FlowContext;
import com.iohao.net.extension.domain.DomainEventHandler;
import com.iohao.net.extension.room.RoomKit;
import lombok.extern.slf4j.Slf4j;

/**
 * OperationContextEventHandler
 *
 * @author 渔民小镇
 * @date 2024-05-12
 * @since 21.8
 */
@Slf4j
public final class OperationContextEventHandler implements DomainEventHandler<OperationContext> {
    @Override
    public void onEvent(OperationContext operationContext, boolean endOfBatch) {
        try {
            operationContext.execute();
        } catch (Throwable e) {
            FlowContext flowContext = operationContext.getFlowContext();
            if (flowContext == null) {
                log.error(e.getMessage(), e);
                return;
            }

            RoomKit.onException(e, flowContext);
        }
    }
}
