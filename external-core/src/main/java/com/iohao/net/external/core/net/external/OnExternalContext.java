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
 * OnExternalContext
 *
 * @param userId         When verifyIdentity is False, this value represents the userChannelId.
 * @param verifyIdentity If verifyIdentity is false, it means the user is not logged in.
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

    public UserSession getUserSession() {
        return verifyIdentity
                ? userSessions.getUserSession(userId)
                : userSessions.getUserSessionByUserChannelId(userId);
    }

    public int getPayloadAsInt() {
        return ByteKit.getInt(this.payload());
    }

    public long getPayloadAsLong() {
        return ByteKit.getLong(this.payload());
    }

    public boolean getPayloadAsBool() {
        return ByteKit.getBoolean(this.payload());
    }

    public String getPayloadAsString() {
        return ByteKit.getString(this.payload());
    }
}
