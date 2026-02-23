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
package com.iohao.net.server.codec;

import com.iohao.net.framework.protocol.ServerOfflineMessage;
import com.iohao.net.common.MessageSbe;
import com.iohao.net.sbe.MessageHeaderEncoder;
import com.iohao.net.sbe.ServerOfflineMessageEncoder;
import org.agrona.MutableDirectBuffer;

/**
 * SBE encoder for server-offline notifications.
 *
 * @author 渔民小镇
 * @date 2025-09-26
 * @since 25.1
 */
@SuppressWarnings("all")
public final class ServerOfflineMessageSbe implements MessageSbe<ServerOfflineMessage> {
    final ServerOfflineMessageEncoder encoder = new ServerOfflineMessageEncoder();

    @Override
    public void encoder(ServerOfflineMessage message, MessageHeaderEncoder headerEncoder, MutableDirectBuffer buffer) {
        encoder.wrapAndApplyHeader(buffer, 0, headerEncoder);

        encoder.serverId(message.serverId());
    }

    @Override
    public int limit() {
        return encoder.limit();
    }
}
