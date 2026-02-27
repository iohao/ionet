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

import com.iohao.net.common.kit.*;
import com.iohao.net.framework.core.*;
import com.iohao.net.framework.core.codec.*;
import com.iohao.net.framework.protocol.*;
import java.util.*;

/**
 * Decorator providing unicast broadcast methods to send messages to a specific user by ID.
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
public interface BroadcastUserCommunicationDecorator extends CommonDecorator {
    /** Broadcast a pre-built user message. @see #broadcastUser(long, CmdInfo, byte[]) */
    default void broadcast(BroadcastUserMessage message) {
        this.getCommunicationAggregation().broadcast(message);
    }

    /**
     * Broadcast a message to a specific user.
     *
     * @param userId       the target user ID
     * @param cmdInfo      the command route identifying the message type
     * @param data         the raw byte payload
     * @param originalData the original pre-encoded data object (retained for hooks/interceptors)
     */
    private void broadcastUser(long userId, CmdInfo cmdInfo, byte[] data, Object originalData) {
        var message = new BroadcastUserMessage();
        message.setCmdMerge(cmdInfo.cmdMerge());
        message.setOriginalData(originalData);
        message.setData(data);

        message.setUserId(userId);
        message.setVerifyIdentity(true);

        this.broadcast(message);
    }

    /** Broadcast raw bytes to a specific user. @see #broadcastUser(long, CmdInfo, byte[], Object) */
    default void broadcastUser(long userId, CmdInfo cmdInfo, byte[] data) {
        broadcastUser(userId, cmdInfo, data, null);
    }

    /** Broadcast with no payload to a specific user. @see #broadcastUser(long, CmdInfo, byte[], Object) */
    default void broadcastUser(long userId, CmdInfo cmdInfo) {
        broadcastUser(userId, cmdInfo, CommonConst.emptyBytes, null);
    }

    /** Broadcast with int payload to a specific user. @see #broadcastUser(long, CmdInfo, byte[], Object) */
    default void broadcastUser(long userId, CmdInfo cmdInfo, int data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastUser(userId, cmdInfo, codec.encode(data), data);
    }

    /** Broadcast with long payload to a specific user. @see #broadcastUser(long, CmdInfo, byte[], Object) */
    default void broadcastUser(long userId, CmdInfo cmdInfo, long data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastUser(userId, cmdInfo, codec.encode(data), data);
    }

    /** Broadcast with boolean payload to a specific user. @see #broadcastUser(long, CmdInfo, byte[], Object) */
    default void broadcastUser(long userId, CmdInfo cmdInfo, boolean data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastUser(userId, cmdInfo, codec.encode(data), data);
    }

    /** Broadcast with String payload to a specific user. @see #broadcastUser(long, CmdInfo, byte[], Object) */
    default void broadcastUser(long userId, CmdInfo cmdInfo, String data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastUser(userId, cmdInfo, codec.encode(data), data);
    }

    /** Broadcast with Object payload to a specific user. @see #broadcastUser(long, CmdInfo, byte[], Object) */
    default void broadcastUser(long userId, CmdInfo cmdInfo, Object data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastUser(userId, cmdInfo, codec.encode(data), data);
    }

    /** Broadcast with List payload to a specific user. @see #broadcastUser(long, CmdInfo, byte[], Object) */
    default void broadcastUser(long userId, CmdInfo cmdInfo, List<?> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastUser(userId, cmdInfo, codec.encodeList(dataList), dataList);
    }

    /** Broadcast with List&lt;Integer&gt; payload to a specific user. @see #broadcastUser(long, CmdInfo, byte[], Object) */
    default void broadcastUserListInt(long userId, CmdInfo cmdInfo, List<Integer> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastUser(userId, cmdInfo, codec.encodeListInt(dataList), dataList);
    }

    /** Broadcast with List&lt;Long&gt; payload to a specific user. @see #broadcastUser(long, CmdInfo, byte[], Object) */
    default void broadcastUserListLong(long userId, CmdInfo cmdInfo, List<Long> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastUser(userId, cmdInfo, codec.encodeListLong(dataList), dataList);
    }

    /** Broadcast with List&lt;Boolean&gt; payload to a specific user. @see #broadcastUser(long, CmdInfo, byte[], Object) */
    default void broadcastUserListBool(long userId, CmdInfo cmdInfo, List<Boolean> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastUser(userId, cmdInfo, codec.encodeListBool(dataList), dataList);
    }

    /** Broadcast with List&lt;String&gt; payload to a specific user. @see #broadcastUser(long, CmdInfo, byte[], Object) */
    default void broadcastUserListString(long userId, CmdInfo cmdInfo, List<String> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastUser(userId, cmdInfo, codec.encodeListString(dataList), dataList);
    }
}
