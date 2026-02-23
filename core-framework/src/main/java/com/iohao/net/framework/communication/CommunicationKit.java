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
import com.iohao.net.framework.protocol.ExternalRequestMessage;
import com.iohao.net.framework.protocol.OnExternalTemplateId;
import com.iohao.net.common.kit.trace.TraceKit;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

/**
 * Utility class providing static access to the global communication infrastructure.
 * <p>
 * Holds the singleton {@link CommunicationAggregation} and {@link Communication} instances,
 * and offers convenience methods for common cross-server operations such as forced user
 * offline and user existence checks.
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
@UtilityClass
public final class CommunicationKit {
    @Getter
    CommunicationAggregation communicationAggregation;
    @Getter
    Communication communication;

    @Setter
    Supplier<Communication> communicationSupplier = DefaultCommunication::new;

    /**
     * Set the global communication aggregation and initialize the communication instance.
     *
     * @param communicationAggregation the aggregation implementation to use
     */
    public void setCommunicationAggregation(CommunicationAggregation communicationAggregation) {
        CommunicationKit.communicationAggregation = communicationAggregation;
        CommunicationKit.communication = communicationSupplier.get();
    }

    /**
     * Force a user offline by broadcasting a forced-offline request to all external servers.
     *
     * @param userId the ID of the user to disconnect
     */
    public void forcedOffline(long userId) {
        var message = ofExternalRequestMessage(userId, OnExternalTemplateId.forcedOffline);
        communicationAggregation.callCollectExternal(message);
    }

    /**
     * Check whether a user is currently connected to any external server.
     *
     * @param userId the ID of the user to check
     * @return {@code true} if the user exists on at least one external server
     */
    public boolean existUser(long userId) {
        var message = ofExternalRequestMessage(userId, OnExternalTemplateId.existUser);
        var responseCollectExternal = communicationAggregation.callCollectExternal(message);
        return responseCollectExternal.anySuccess();
    }

    /**
     * Create an external request message for the given user and template.
     *
     * @param userId     the target user ID
     * @param templateId the external template operation ID
     * @return a new {@link ExternalRequestMessage} populated with user, template, net ID, and trace ID
     */
    private ExternalRequestMessage ofExternalRequestMessage(long userId, int templateId) {
        var message = new ExternalRequestMessage();
        message.setPayload(userId);
        message.setTemplateId(templateId);
        message.setNetId(CoreGlobalConfig.getNetId());

        String traceId = TraceKit.getTraceId();
        if (traceId != null) {
            message.setTraceId(traceId);
        }

        return message;
    }
}
