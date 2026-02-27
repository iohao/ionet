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

import com.iohao.net.framework.communication.*;
import com.iohao.net.framework.protocol.*;

/**
 * Flow-level communication for sending requests to external servers.
 *
 * @author 渔民小镇
 * @date 2025-10-09
 * @since 25.1
 */
public interface FlowExternalCommunication extends FlowCommon, ExternalCommunicationDecorator {
    /**
     * Create an {@link ExternalRequestMessage} targeting the specified external server template ID,
     * with the given payload. Routing fields (user identity, trace ID, net ID, external server ID)
     * are copied from the current request and server.
     *
     * @param templateId the external server template ID
     * @param payload    the encoded byte payload (may be {@code null})
     * @return a new ExternalRequestMessage with routing fields populated
     */
    default ExternalRequestMessage ofExternalRequestMessage(int templateId, byte[] payload) {
        var message = new ExternalRequestMessage();
        message.setPayload(payload);
        message.setTemplateId(templateId);

        var request = this.getRequest();
        message.setUserIdentity(request);
        message.setTraceId(request.getTraceId());
        message.setNetId(this.getServer().netId());
        message.setExternalServerId(request.getExternalServerId());

        return message;
    }
}
