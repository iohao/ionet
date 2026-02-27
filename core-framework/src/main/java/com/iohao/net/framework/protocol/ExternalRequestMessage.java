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
package com.iohao.net.framework.protocol;

import lombok.*;

/**
 * Request message sent from an external (Netty) server to a logic server.
 * <p>
 * Carries user identity information, a trace id for distributed tracing,
 * and the external server / template context. Extends {@link ExternalCommonMessage}
 * with request-specific fields.
 *
 * @author 渔民小镇
 * @date 2025-09-10
 * @since 25.1
 */
@Getter
@Setter
public final class ExternalRequestMessage extends ExternalCommonMessage implements UserIdentity, ExternalPayloadSetting {
    /** User id associated with this request. */
    long userId;
    /** Whether the user's identity has been verified (authenticated). */
    boolean verifyIdentity;

    /** Template id of the external server that received the client connection. */
    int templateId;
    /** Distributed trace id for request tracking. */
    String traceId;
    /** Network id identifying the Aeron connection. */
    int netId;
    /** Identifier of the originating external server. */
    int externalServerId;

    /**
     * Create a shallow clone of this request message.
     *
     * @return a new {@link ExternalRequestMessage} with the same field values
     */
    public ExternalRequestMessage ofClone() {
        var message = new ExternalRequestMessage();
        message.futureId = this.futureId;
        message.payload = this.payload;

        message.userId = this.userId;
        message.verifyIdentity = this.verifyIdentity;
        message.templateId = this.templateId;
        message.externalServerId = this.externalServerId;
        message.netId = this.netId;
        message.traceId = this.traceId;

        return message;
    }
}
