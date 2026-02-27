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
import com.iohao.net.external.core.message.*;
import com.iohao.net.framework.protocol.*;
import com.iohao.net.sbe.*;
import io.aeron.logbuffer.*;
import org.agrona.*;

/**
 * Aeron fragment consumer that broadcasts a decoded message to a specified user list.
 *
 * @author 渔民小镇
 * @date 2025-09-03
 * @since 25.1
 */
public class BroadcastUserListMessageOnFragment implements OnFragment {
    final BroadcastUserListMessageDecoder decoder = new BroadcastUserListMessageDecoder();

    @Override
    public void process(DirectBuffer buffer, int offset, int actingBlockLength, int actingVersion, Header header) {
        decoder.wrap(buffer, offset, actingBlockLength, actingVersion);

        var message = CommunicationMessageKit.createCommunicationMessage();
        message.setCmdMerge(decoder.cmdMerge());

        var _decoder = decoder.userIds();
        if (_decoder.count() == 0) {
            return;
        }

        var userIds = new long[_decoder.count()];
        int index = 0;
        for (var groupElement : _decoder) {
            userIds[index++] = groupElement.value();
        }

        var dataLength = decoder.dataLength();
        var dataBytes = ByteKit.ofBytes(dataLength);
        decoder.getData(dataBytes, 0, dataLength);
        message.setData(dataBytes);

        this.writeAndFlush(message, userIds);
    }

    /**
     * Write the decoded message to each resolved user session in the provided list.
     *
     * @param message decoded outbound message
     * @param userIds target user ids
     */
    protected void writeAndFlush(CommunicationMessage message, long[] userIds) {
        var userSessions = ExternalServerSingle.userSessions;
        for (long userId : userIds) {
            var userSession = userSessions.getUserSession(userId);
            if (userSession != null) {
                userSession.writeAndFlush(message);
            }
        }
    }

    @Override
    public int getTemplateId() {
        return BroadcastUserListMessageDecoder.TEMPLATE_ID;
    }
}
