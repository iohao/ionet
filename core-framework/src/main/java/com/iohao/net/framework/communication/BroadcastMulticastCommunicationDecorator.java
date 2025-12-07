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
import com.iohao.net.framework.protocol.BroadcastMulticastMessage;
import com.iohao.net.common.kit.CommonConst;

import java.util.Collection;
import java.util.List;


/**
 * BroadcastMulticastCommunicationDecorator
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
public interface BroadcastMulticastCommunicationDecorator extends CommonDecorator {
    default void broadcast(BroadcastMulticastMessage message) {
        this.getCommunicationAggregation().broadcast(message);
    }

    private void broadcastMulticast(CmdInfo cmdInfo, byte[] data, Object originalData) {
        var message = new BroadcastMulticastMessage();
        message.setCmdMerge(cmdInfo.cmdMerge());
        message.setData(data);
        message.setOriginalData(originalData);

        this.broadcast(message);
    }

    default void broadcastMulticast(CmdInfo cmdInfo, byte[] data) {
        broadcastMulticast(cmdInfo, data, null);
    }

    default void broadcastMulticast(CmdInfo cmdInfo) {
        this.broadcastMulticast(cmdInfo, CommonConst.emptyBytes, null);
    }

    default void broadcastMulticast(CmdInfo cmdInfo, int data) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encode(data), data);
    }

    default void broadcastMulticast(CmdInfo cmdInfo, long data) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encode(data), data);
    }

    default void broadcastMulticast(CmdInfo cmdInfo, boolean data) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encode(data), data);
    }

    default void broadcastMulticast(CmdInfo cmdInfo, String data) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encode(data), data);
    }

    default void broadcastMulticast(CmdInfo cmdInfo, Object data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastMulticast(cmdInfo, codec.encode(data), data);
    }

    default void broadcastMulticast(CmdInfo cmdInfo, Collection<?> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encodeList(dataList), dataList);
    }

    default void broadcastMulticastListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encodeListInt(dataList), dataList);
    }

    default void broadcastMulticastListLong(CmdInfo cmdInfo, List<Long> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encodeListLong(dataList), dataList);
    }

    default void broadcastMulticastListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encodeListBool(dataList), dataList);
    }

    default void broadcastMulticastListString(CmdInfo cmdInfo, List<String> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encodeListString(dataList), dataList);
    }
}
