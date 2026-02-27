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


import com.iohao.net.framework.core.*;

/**
 * Fire-and-forget request message for one-way logic-to-logic server communication.
 * <p>
 * Extends {@link RequestMessage} without expecting a response. Used when a logic server
 * needs to notify another logic server of an event or trigger an action without waiting
 * for a result.
 *
 * @author 渔民小镇
 * @date 2025-09-02
 * @since 25.1
 */
public final class SendMessage extends RequestMessage {
    /**
     * Create a new {@link SendMessage} with the given command route and payload.
     *
     * @param cmdInfo the command route descriptor
     * @param data    the serialized payload
     * @return a new send message
     */
    public static SendMessage of(CmdInfo cmdInfo, byte[] data) {
        SendMessage message = new SendMessage();
        message.setCmdInfo(cmdInfo);
        message.setData(data);
        return message;
    }
}
