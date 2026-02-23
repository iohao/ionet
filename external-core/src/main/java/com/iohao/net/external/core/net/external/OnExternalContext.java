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
package com.iohao.net.external.core.net.external;

import com.iohao.net.framework.protocol.ExternalResponse;
import com.iohao.net.common.kit.ByteKit;
import com.iohao.net.external.core.session.UserSession;
import com.iohao.net.external.core.session.UserSessions;

/**
 * Execution context for an {@link OnExternal} template handler.
 *
 * @param userSessions user session manager for lookups and mutations
 * @param response mutable response to send back to the internal caller
 * @param userId business userId, or userChannelId when {@code verifyIdentity == false}
 * @param verifyIdentity whether the current user identity has been verified
 * @param payload raw payload bytes
 * @param payloadLength valid payload length
 * @author 渔民小镇
 * @date 2025-09-11
 * @since 25.1
 */
public record OnExternalContext(
        UserSessions<?, ?> userSessions,
        ExternalResponse response,
        long userId,
        boolean verifyIdentity,
        byte[] payload,
        int payloadLength
) {

    /**
     * Resolve the target user session using verified userId or channelId semantics.
     *
     * @return matched user session, or {@code null}
     */
    public UserSession getUserSession() {
        return verifyIdentity
                ? userSessions.getUserSession(userId)
                : userSessions.getUserSessionByUserChannelId(userId);
    }

    /**
     * Decode the payload as an {@code int}.
     *
     * @return decoded int value
     */
    public int getPayloadAsInt() {
        return ByteKit.getInt(this.payload());
    }

    /**
     * Decode the payload as a {@code long}.
     *
     * @return decoded long value
     */
    public long getPayloadAsLong() {
        return ByteKit.getLong(this.payload());
    }

    /**
     * Decode the payload as a {@code boolean}.
     *
     * @return decoded boolean value
     */
    public boolean getPayloadAsBool() {
        return ByteKit.getBoolean(this.payload());
    }

    /**
     * Decode the payload as a string.
     *
     * @return decoded string value
     */
    public String getPayloadAsString() {
        return ByteKit.getString(this.payload());
    }
}
