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

import com.iohao.net.framework.CoreGlobalConfig;
import com.iohao.net.framework.core.kit.BarMessageKit;
import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.core.FlowContextKeys;
import com.iohao.net.framework.core.flow.FlowContext;
import com.iohao.net.framework.protocol.ExternalRequestMessage;
import com.iohao.net.framework.protocol.RequestMessage;
import com.iohao.net.framework.protocol.Request;
import lombok.extern.slf4j.Slf4j;

/**
 * DefaultCommunication
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
@Slf4j
public class DefaultCommunication implements Communication {
    @Override
    public ExternalRequestMessage ofExternalRequestMessage(int templateId, byte[] payload) {
        var message = new ExternalRequestMessage();
        message.setPayload(payload);
        message.setTemplateId(templateId);

        message.setTraceId(this.getTraceId());
        message.setNetId(CoreGlobalConfig.getNetId());

        return message;
    }

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
