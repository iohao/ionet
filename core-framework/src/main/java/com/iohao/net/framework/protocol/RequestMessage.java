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
import lombok.*;

/**
 * Internal request message used for logic-to-logic server communication via Aeron.
 * <p>
 * Extends {@link CommonMessage} with {@link Request} fields including user identity,
 * hop tracking, logic-server binding, and per-request attachment. Provides factory
 * methods for creation and shallow cloning to support request forwarding across
 * logic server boundaries.
 *
 * @author 渔民小镇
 * @date 2025-09-02
 */
@Setter
@Getter
public class RequestMessage extends CommonMessage implements Request {
    long userId;
    boolean verifyIdentity;

    int hopCount;
    int[] bindingLogicServerIds;
    byte[] attachment;

    /**
     * Create a new {@link RequestMessage} with the given command route and payload.
     *
     * @param cmdInfo the command route descriptor
     * @param data    the serialized request payload
     * @return a new request message
     */
    public static RequestMessage of(CmdInfo cmdInfo, byte[] data) {
        var message = new RequestMessage();
        message.setCmdInfo(cmdInfo);
        message.setData(data);
        return message;
    }

    /**
     * Create a shallow clone of this request message, copying all routing and identity fields.
     * <p>
     * Useful when forwarding a request to another logic server while preserving the
     * original context.
     *
     * @return a new {@link RequestMessage} with the same field values
     */
    public RequestMessage ofClone() {
        var message = new RequestMessage();
        message.userId = this.userId;
        message.verifyIdentity = this.verifyIdentity;
        message.cmdMerge = this.cmdMerge;
        message.traceId = this.traceId;

        message.externalServerId = this.externalServerId;
        message.logicServerId = this.logicServerId;
        message.sourceServerId = this.sourceServerId;

        message.netId = this.netId;
        message.data = this.data;

        message.hopCount = this.hopCount;
        message.bindingLogicServerIds = this.bindingLogicServerIds;
        message.attachment = this.attachment;

        return message;
    }
}
