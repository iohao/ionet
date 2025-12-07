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
 * BroadcastUserListCommunicationDecorator
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
public interface BroadcastUserListCommunicationDecorator extends CommonDecorator {
    default void broadcast(BroadcastUserListMessage message) {
        this.getCommunicationAggregation().broadcast(message);
    }

    private void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo, byte[] data, Object originalData) {
        var userIds = ArrayKit.toArrayLong(userIdList);

        var message = new BroadcastUserListMessage();
        message.setUserIds(userIds);
        message.setCmdMerge(cmdInfo.cmdMerge());
        message.setOriginalData(originalData);
        message.setData(data);

        this.broadcast(message);
    }

    default void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo, byte[] data) {
        broadcastUsers(userIdList, cmdInfo, data, null);
    }


    default void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo) {
        broadcastUsers(userIdList, cmdInfo, CommonConst.emptyBytes, null);
    }

    default void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo, int data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastUsers(userIdList, cmdInfo, codec.encode(data), data);
    }

    default void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo, long data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastUsers(userIdList, cmdInfo, codec.encode(data), data);
    }

    default void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo, boolean data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastUsers(userIdList, cmdInfo, codec.encode(data), data);
    }

    default void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo, String data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastUsers(userIdList, cmdInfo, codec.encode(data), data);
    }

    default void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo, Object data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastUsers(userIdList, cmdInfo, codec.encode(data), data);
    }

    default void broadcastUsers(Collection<Long> userIdList, CmdInfo cmdInfo, List<?> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastUsers(userIdList, cmdInfo, codec.encodeList(dataList), dataList);
    }

    default void broadcastUsersListInt(Collection<Long> userIdList, CmdInfo cmdInfo, List<Integer> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastUsers(userIdList, cmdInfo, codec.encodeListInt(dataList), dataList);
    }

    default void broadcastUsersListLong(Collection<Long> userIdList, CmdInfo cmdInfo, List<Long> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastUsers(userIdList, cmdInfo, codec.encodeListLong(dataList), dataList);
    }

    default void broadcastUsersListBool(Collection<Long> userIdList, CmdInfo cmdInfo, List<Boolean> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastUsers(userIdList, cmdInfo, codec.encodeListBool(dataList), dataList);
    }

    default void broadcastUsersListString(Collection<Long> userIdList, CmdInfo cmdInfo, List<String> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastUsers(userIdList, cmdInfo, codec.encodeListString(dataList), dataList);
    }
}
