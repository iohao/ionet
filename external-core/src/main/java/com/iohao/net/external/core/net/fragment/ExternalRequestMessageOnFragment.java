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
package com.iohao.net.external.core.net.fragment;

import com.iohao.net.common.*;
import com.iohao.net.common.kit.*;
import com.iohao.net.common.kit.concurrent.*;
import com.iohao.net.external.core.message.*;
import com.iohao.net.external.core.net.external.*;
import com.iohao.net.external.core.session.*;
import com.iohao.net.framework.core.exception.*;
import com.iohao.net.framework.protocol.*;
import com.iohao.net.sbe.*;
import com.iohao.net.server.*;
import com.iohao.net.server.connection.*;
import io.aeron.logbuffer.*;
import lombok.extern.slf4j.*;
import org.agrona.*;

/**
 * Aeron fragment consumer that dispatches internal external-operation requests by template id.
 *
 * @author 渔民小镇
 * @date 2025-09-11
 * @since 25.1
 */
@Slf4j
public class ExternalRequestMessageOnFragment implements OnFragment, NetServerSettingAware {
    protected final ExternalRequestMessageDecoder decoder = new ExternalRequestMessageDecoder();
    protected ConnectionManager connectionManager;

    @Override
    public void setNetServerSetting(NetServerSetting setting) {
        connectionManager = setting.connectionManager();
    }

    @Override
    public void process(DirectBuffer buffer, int offset, int actingBlockLength, int actingVersion, Header header) {
        decoder.wrap(buffer, offset, actingBlockLength, actingVersion);

        var userSessions = this.getUserSessions(decoder.externalServerId());
        if (userSessions == null) {
            var responseMessage = new ExternalResponseMessage();
            responseMessage.setFutureId(decoder.futureId());
            responseMessage.setError(ActionErrorEnum.internalCommunicationError);
            connectionManager.publishMessageByNetId(decoder.netId(), responseMessage);
            return;
        }

        var userIdentity = decoder.userIdentity();
        var userId = userIdentity.userId();
        var verifyIdentity = userIdentity.verifyIdentity() == 1;

        var payloadLength = decoder.payloadLength();
        var payload = ByteKit.ofBytes(payloadLength);
        decoder.getPayload(payload, 0, payloadLength);

        var responseMessage = new ExternalResponseMessage();
        responseMessage.setFutureId(decoder.futureId());
        responseMessage.setExternalServerId(decoder.externalServerId());
        var context = new OnExternalContext(userSessions, responseMessage, userId, verifyIdentity, payload, payloadLength);
        processOnExternal(context);
    }

    /**
     * Resolve the user session container for the target external server id.
     *
     * @param externalServerId external server id
     * @return user session container
     */
    protected UserSessions<?, ?> getUserSessions(int externalServerId) {
        return ExternalServerSingle.userSessions;
    }

    /**
     * Execute the template handler asynchronously and always publish the response back to the requester netId.
     *
     * @param context external template execution context
     */
    protected void processOnExternal(OnExternalContext context) {
        var templateId = decoder.templateId();
        var netId = decoder.netId();

        TaskKit.getNetVirtualExecutor().execute(() -> {
            try {
                var onExternals = OnExternalManager.getOnExternals(templateId);
                var on = onExternals[Math.abs(templateId)];
                on.process(context.payload(), context.payloadLength(), context);
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
                if (e instanceof MessageException messageException) {
                    context.response().setError(messageException.getErrorInformation());
                } else {
                    context.response().setError(ActionErrorEnum.systemOtherErrCode);
                }
            } finally {
                connectionManager.publishMessageByNetId(netId, context.response());
            }
        });
    }

    @Override
    public int getTemplateId() {
        return ExternalRequestMessageDecoder.TEMPLATE_ID;
    }
}
