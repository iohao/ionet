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
 * Response message sent when a server successfully connects (joins) the cluster.
 * <p>
 * Created from a {@link ServerMessage} via the {@link #of(ServerMessage)} factory
 * method, copying all server metadata and adding the joining server's id and
 * a future correlation id.
 *
 * @author 渔民小镇
 * @date 2025-09-25
 * @since 25.1
 */
@Getter
@Setter
@ToString(callSuper = true)
public final class ConnectResponseMessage extends ServerMessage {
    /** Correlation id used to match this response to its originating connect request. */
    long futureId;
    /** Identifier of the server that has joined the cluster. */
    int joinServerId;

    /**
     * Create a {@link ConnectResponseMessage} by copying metadata from the given server message.
     *
     * @param message the source server message whose metadata is copied
     * @return a new {@link ConnectResponseMessage} populated with the source metadata
     */
    public static ConnectResponseMessage of(ServerMessage message) {
        var responseMessage = new ConnectResponseMessage();
        responseMessage.setId(message.getId());
        responseMessage.setName(message.getName());
        responseMessage.setTag(message.getTag());
        responseMessage.setServerType(message.getServerType());
        responseMessage.setNetId(message.getNetId());
        responseMessage.setIp(message.getIp());
        responseMessage.setCmdMerges(message.getCmdMerges());
        responseMessage.setPubName(message.getPubName());
        message.getPayloadMap().forEach(responseMessage::addPayload);

        return responseMessage;
    }
}
