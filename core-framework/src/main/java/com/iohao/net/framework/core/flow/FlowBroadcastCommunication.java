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
 * Flow-level broadcast communication providing methods to broadcast messages to the requesting
 * user (broadcastMe) with various data type overloads.
 *
 * @author 渔民小镇
 * @date 2025-10-09
 * @since 25.1
 */
public interface FlowBroadcastCommunication extends FlowCommon
        , BroadcastUserCommunicationDecorator
        , BroadcastUserListCommunicationDecorator
        , BroadcastMulticastCommunicationDecorator {

    /**
     * Broadcast a message to the requesting user.
     * <p>
     * This is the main broadcast method. All typed overloads delegate to this method
     * after encoding the data.
     *
     * @param cmdInfo      the command info identifying the broadcast route
     * @param data         the encoded byte data to broadcast
     * @param originalData the original pre-encoded data object (may be {@code null})
     */
    private void broadcastMe(CmdInfo cmdInfo, byte[] data, Object originalData) {
        var message = new BroadcastUserMessage();
        message.setCmdMerge(cmdInfo.cmdMerge());
        message.setOriginalData(originalData);
        message.setData(data);

        broadcastMe(message);
    }

    /**
     * Broadcast a pre-built message to the requesting user.
     *
     * @param message the broadcast message with user identity and data already set
     */
    default void broadcastMe(BroadcastUserMessage message) {
        var request = this.getRequest();
        message.setUserIdentity(request);
        message.setExternalServerId(request.getExternalServerId());

        this.getCommunicationAggregation().broadcast(message);
    }

    /** Broadcast raw byte data to the requesting user. @see #broadcastMe(CmdInfo, byte[], Object) */
    default void broadcastMe(CmdInfo cmdInfo, byte[] data) {
        broadcastMe(cmdInfo, data, null);
    }

    /** Broadcast an empty message to the requesting user. @see #broadcastMe(CmdInfo, byte[], Object) */
    default void broadcastMe(CmdInfo cmdInfo) {
        broadcastMe(cmdInfo, CommonConst.emptyBytes, null);
    }

    /** Broadcast an int value to the requesting user. @see #broadcastMe(CmdInfo, byte[], Object) */
    default void broadcastMe(CmdInfo cmdInfo, int data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastMe(cmdInfo, codec.encode(data), data);
    }

    /** Broadcast a long value to the requesting user. @see #broadcastMe(CmdInfo, byte[], Object) */
    default void broadcastMe(CmdInfo cmdInfo, long data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastMe(cmdInfo, codec.encode(data), data);
    }

    /** Broadcast a boolean value to the requesting user. @see #broadcastMe(CmdInfo, byte[], Object) */
    default void broadcastMe(CmdInfo cmdInfo, boolean data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastMe(cmdInfo, codec.encode(data), data);
    }

    /** Broadcast a String value to the requesting user. @see #broadcastMe(CmdInfo, byte[], Object) */
    default void broadcastMe(CmdInfo cmdInfo, String data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastMe(cmdInfo, codec.encode(data), data);
    }

    /** Broadcast an Object value to the requesting user. @see #broadcastMe(CmdInfo, byte[], Object) */
    default void broadcastMe(CmdInfo cmdInfo, Object data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastMe(cmdInfo, codec.encode(data), data);
    }

    /** Broadcast a list of objects to the requesting user. @see #broadcastMe(CmdInfo, byte[], Object) */
    default void broadcastMe(CmdInfo cmdInfo, List<?> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMe(cmdInfo, codec.encodeList(dataList), dataList);
    }

    /**
     * Broadcast a list of int values to the requesting user.
     *
     * @param cmdInfo  the command info identifying the broadcast route
     * @param dataList the list of int values to broadcast
     */
    default void broadcastMeListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMe(cmdInfo, codec.encodeListInt(dataList), dataList);
    }

    /**
     * Broadcast a list of boolean values to the requesting user.
     *
     * @param cmdInfo  the command info identifying the broadcast route
     * @param dataList the list of boolean values to broadcast
     */
    default void broadcastMeListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMe(cmdInfo, codec.encodeListBool(dataList), dataList);
    }

    /**
     * Broadcast a list of long values to the requesting user.
     *
     * @param cmdInfo  the command info identifying the broadcast route
     * @param dataList the list of long values to broadcast
     */
    default void broadcastMeListLong(CmdInfo cmdInfo, List<Long> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMe(cmdInfo, codec.encodeListLong(dataList), dataList);
    }

    /**
     * Broadcast a list of String values to the requesting user.
     *
     * @param cmdInfo  the command info identifying the broadcast route
     * @param dataList the list of String values to broadcast
     */
    default void broadcastMeListString(CmdInfo cmdInfo, List<String> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMe(cmdInfo, codec.encodeListString(dataList), dataList);
    }
}
