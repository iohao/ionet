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
package com.iohao.net.external.core.message;

import com.iohao.net.framework.protocol.CommunicationMessage;
import com.iohao.net.external.core.session.UserSessions;
import lombok.experimental.UtilityClass;

/**
 *
 * @author 渔民小镇
 * @date 2025-11-12
 * @since 25.1
 */
@UtilityClass
public final class ExternalWriteKit {
    public void writeAndFlush(CommunicationMessage message, UserSessions<?, ?> userSessions) {
        if (userSessions == null) {
            return;
        }

        var userId = message.getUserId();
        var userSession = message.isVerifyIdentity()
                ? userSessions.getUserSession(userId)
                : userSessions.getUserSessionByUserChannelId(userId);

        if (userSession != null) {
            userSession.writeAndFlush(message);
        }
    }
}
