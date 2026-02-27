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
import com.iohao.net.framework.core.kit.*;
import com.iohao.net.framework.protocol.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Default implementation of {@link FlowContext} with Lombok-generated getters and setters.
 *
 * @author 渔民小镇
 * @date 2025-10-09
 * @since 25.1
 */
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultFlowContext implements FlowContext {
    /** When verifyIdentity is False, this value represents the userChannelId. */
    long userId;
    BarSkeleton barSkeleton;
    ActionCommand actionCommand;
    Object actionController;
    Request request;
    Object methodResult;
    Object originalMethodResult;
    CommunicationType communicationType;

    int errorCode;
    String errorMessage;
    CommunicationAggregation communicationAggregation;
    long nanoTime;
    Object dataParam;
    CmdInfo cmdInfo;
    ThreadExecutor currentThreadExecutor;

    public long getNanoTime() {
        if (this.nanoTime != 0) {
            return this.nanoTime;
        }

        this.nanoTime = System.nanoTime();
        return this.nanoTime;
    }

    /**
     * Create a new {@link RequestMessage} for cross-logic-server calls,
     * copying routing fields from the current request.
     *
     * @param cmdInfo   the target command routing info
     * @param dataBytes the serialized request payload
     * @return a new request message with source server and net ID populated
     */
    @Override
    public RequestMessage ofRequestMessage(final CmdInfo cmdInfo, final byte[] dataBytes) {
        var message = RequestMessage.of(cmdInfo, dataBytes);
        BarMessageKit.employ(this.getRequest(), message);

        var server = this.getServer();
        message.setSourceServerId(server.id());
        message.setNetId(server.netId());

        return message;
    }
}
