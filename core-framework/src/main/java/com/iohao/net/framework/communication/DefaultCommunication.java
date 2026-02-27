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

import com.iohao.net.framework.*;
import com.iohao.net.framework.core.*;
import com.iohao.net.framework.core.flow.*;
import com.iohao.net.framework.core.kit.*;
import com.iohao.net.framework.protocol.*;
import lombok.extern.slf4j.*;

/**
 * Default implementation of {@link Communication} that delegates to the global {@link CommunicationAggregation}.
 * <p>
 * Creates protocol messages ({@link ExternalRequestMessage} and {@link RequestMessage}) by populating
 * them with trace IDs, network IDs, and source server metadata from the current {@link FlowContext}.
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
@Slf4j
public class DefaultCommunication implements Communication {
    /**
     * Create an {@link ExternalRequestMessage} for communicating with external servers.
     * <p>
     * Populates the message with the given template ID, payload, current trace ID,
     * and the global network ID from {@link CoreGlobalConfig}.
     *
     * @param templateId the external request template identifier
     * @param payload    the serialized request payload (may be {@code null})
     * @return a fully populated external request message
     */
    @Override
    public ExternalRequestMessage ofExternalRequestMessage(int templateId, byte[] payload) {
        var message = new ExternalRequestMessage();
        message.setPayload(payload);
        message.setTemplateId(templateId);

        message.setTraceId(this.getTraceId());
        message.setNetId(CoreGlobalConfig.getNetId());

        return message;
    }

    /**
     * Create a {@link RequestMessage} for inter-logic-server communication.
     * <p>
     * Derives the current {@link FlowContext} via {@link FlowContextKeys}, copies routing
     * metadata from the original request, and sets the network ID and source server ID.
     *
     * @param cmdInfo   the command routing information (cmd + subCmd)
     * @param dataBytes the serialized request data
     * @return a fully populated request message ready for dispatch
     */
    @Override
    public RequestMessage ofRequestMessage(CmdInfo cmdInfo, byte[] dataBytes) {
        FlowContext flowContext = FlowContextKeys.getFlowContext();
        Request request = flowContext.getRequest();

        var message = RequestMessage.of(cmdInfo, dataBytes);
        BarMessageKit.employ(request, message);

        var server = flowContext.getServer();
        message.setNetId(server.netId());
        message.setSourceServerId(server.id());

        return message;
    }
}
