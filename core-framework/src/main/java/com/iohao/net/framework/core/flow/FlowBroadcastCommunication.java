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
package com.iohao.net.framework.core.flow;

import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.core.codec.DataCodecManager;
import com.iohao.net.framework.communication.BroadcastMulticastCommunicationDecorator;
import com.iohao.net.framework.communication.BroadcastUserCommunicationDecorator;
import com.iohao.net.framework.communication.BroadcastUserListCommunicationDecorator;
import com.iohao.net.framework.protocol.BroadcastUserMessage;
import com.iohao.net.common.kit.CommonConst;

import java.util.List;

/**
 * FlowBroadcast
 *
 * @author 渔民小镇
 * @date 2025-10-09
 * @since 25.1
 */
public interface FlowBroadcastCommunication extends FlowCommon
        , BroadcastUserCommunicationDecorator
        , BroadcastUserListCommunicationDecorator
        , BroadcastMulticastCommunicationDecorator {

    private void broadcastMe(CmdInfo cmdInfo, byte[] data, Object originalData) {
        var message = new BroadcastUserMessage();
        message.setCmdMerge(cmdInfo.cmdMerge());
        message.setOriginalData(originalData);
        message.setData(data);

        broadcastMe(message);
    }

    default void broadcastMe(BroadcastUserMessage message) {
        var request = this.getRequest();
        message.setUserIdentity(request);
        message.setExternalServerId(request.getExternalServerId());

        this.getCommunicationAggregation().broadcast(message);
    }

    default void broadcastMe(CmdInfo cmdInfo, byte[] data) {
        broadcastMe(cmdInfo, data, null);
    }

    default void broadcastMe(CmdInfo cmdInfo) {
        broadcastMe(cmdInfo, CommonConst.emptyBytes, null);
    }

    default void broadcastMe(CmdInfo cmdInfo, int data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastMe(cmdInfo, codec.encode(data), data);
    }

    default void broadcastMe(CmdInfo cmdInfo, long data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastMe(cmdInfo, codec.encode(data), data);
    }

    default void broadcastMe(CmdInfo cmdInfo, boolean data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastMe(cmdInfo, codec.encode(data), data);
    }

    default void broadcastMe(CmdInfo cmdInfo, String data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastMe(cmdInfo, codec.encode(data), data);
    }

    default void broadcastMe(CmdInfo cmdInfo, Object data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastMe(cmdInfo, codec.encode(data), data);
    }

    default void broadcastMe(CmdInfo cmdInfo, List<?> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMe(cmdInfo, codec.encodeList(dataList), dataList);
    }

    default void broadcastMeListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMe(cmdInfo, codec.encodeListInt(dataList), dataList);
    }

    default void broadcastMeListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMe(cmdInfo, codec.encodeListBool(dataList), dataList);
    }

    default void broadcastMeListLong(CmdInfo cmdInfo, List<Long> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMe(cmdInfo, codec.encodeListLong(dataList), dataList);
    }

    default void broadcastMeListString(CmdInfo cmdInfo, List<String> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMe(cmdInfo, codec.encodeListString(dataList), dataList);
    }
}
