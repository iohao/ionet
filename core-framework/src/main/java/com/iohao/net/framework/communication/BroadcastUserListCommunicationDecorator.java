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

import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.core.codec.DataCodecManager;
import com.iohao.net.framework.protocol.BroadcastUserListMessage;
import com.iohao.net.common.kit.ArrayKit;
import com.iohao.net.common.kit.CommonConst;

import java.util.Collection;
import java.util.List;

/**
 * Decorator providing broadcast methods to send messages to a list of users by their IDs.
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
public interface BroadcastUserListCommunicationDecorator extends CommonDecorator {
    /** Broadcast a pre-built user-list message. @see #broadcastUsers(Collection, CmdInfo, byte[]) */
    default void broadcast(BroadcastUserListMessage message) {
        this.getCommunicationAggregation().broadcast(message);
    }

    /**
     * Broadcast a message to a list of users.
     *
     * @param userIdList   the collection of target user IDs
     * @param cmdInfo      the command route identifying the message type
     * @param data         the raw byte payload
     * @param originalData the original pre-encoded data object (retained for hooks/interceptors)
     */
    private void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo, byte[] data, Object originalData) {
        var userIds = ArrayKit.toArrayLong(userIdList);

        var message = new BroadcastUserListMessage();
        message.setUserIds(userIds);
        message.setCmdMerge(cmdInfo.cmdMerge());
        message.setOriginalData(originalData);
        message.setData(data);

        this.broadcast(message);
    }

    /** Broadcast raw bytes to a list of users. @see #broadcastUsers(Collection, CmdInfo, byte[], Object) */
    default void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo, byte[] data) {
        broadcastUsers(userIdList, cmdInfo, data, null);
    }

    /** Broadcast with no payload to a list of users. @see #broadcastUsers(Collection, CmdInfo, byte[], Object) */
    default void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo) {
        broadcastUsers(userIdList, cmdInfo, CommonConst.emptyBytes, null);
    }

    /** Broadcast with int payload to a list of users. @see #broadcastUsers(Collection, CmdInfo, byte[], Object) */
    default void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo, int data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastUsers(userIdList, cmdInfo, codec.encode(data), data);
    }

    /** Broadcast with long payload to a list of users. @see #broadcastUsers(Collection, CmdInfo, byte[], Object) */
    default void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo, long data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastUsers(userIdList, cmdInfo, codec.encode(data), data);
    }

    /** Broadcast with boolean payload to a list of users. @see #broadcastUsers(Collection, CmdInfo, byte[], Object) */
    default void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo, boolean data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastUsers(userIdList, cmdInfo, codec.encode(data), data);
    }

    /** Broadcast with String payload to a list of users. @see #broadcastUsers(Collection, CmdInfo, byte[], Object) */
    default void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo, String data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastUsers(userIdList, cmdInfo, codec.encode(data), data);
    }

    /** Broadcast with Object payload to a list of users. @see #broadcastUsers(Collection, CmdInfo, byte[], Object) */
    default void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo, Object data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastUsers(userIdList, cmdInfo, codec.encode(data), data);
    }

    /** Broadcast with List payload to a list of users. @see #broadcastUsers(Collection, CmdInfo, byte[], Object) */
    default void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo, List<?> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastUsers(userIdList, cmdInfo, codec.encodeList(dataList), dataList);
    }

    /** Broadcast with List&lt;Integer&gt; payload to a list of users. @see #broadcastUsers(Collection, CmdInfo, byte[], Object) */
    default void broadcastUsersListInt(Collection<Long> userIdList, CmdInfo cmdInfo, List<Integer> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastUsers(userIdList, cmdInfo, codec.encodeListInt(dataList), dataList);
    }

    /** Broadcast with List&lt;Long&gt; payload to a list of users. @see #broadcastUsers(Collection, CmdInfo, byte[], Object) */
    default void broadcastUsersListLong(Collection<Long> userIdList, CmdInfo cmdInfo, List<Long> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastUsers(userIdList, cmdInfo, codec.encodeListLong(dataList), dataList);
    }

    /** Broadcast with List&lt;Boolean&gt; payload to a list of users. @see #broadcastUsers(Collection, CmdInfo, byte[], Object) */
    default void broadcastUsersListBool(Collection<Long> userIdList, CmdInfo cmdInfo, List<Boolean> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastUsers(userIdList, cmdInfo, codec.encodeListBool(dataList), dataList);
    }

    /** Broadcast with List&lt;String&gt; payload to a list of users. @see #broadcastUsers(Collection, CmdInfo, byte[], Object) */
    default void broadcastUsersListString(Collection<Long> userIdList, CmdInfo cmdInfo, List<String> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastUsers(userIdList, cmdInfo, codec.encodeListString(dataList), dataList);
    }
}
