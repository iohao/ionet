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

import com.iohao.net.framework.core.kit.BarMessageKit;
import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.core.FlowContextKeys;
import com.iohao.net.framework.core.codec.DataCodec;
import com.iohao.net.framework.core.codec.DataCodecManager;
import com.iohao.net.framework.protocol.SendMessage;
import com.iohao.net.framework.protocol.Server;
import com.iohao.net.common.kit.CommonConst;

import java.util.List;


/**
 * LogicSendCommunicationDecorator
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
public interface LogicSendCommunicationDecorator extends CommonDecorator {
    private DataCodec getInternalDataCodec() {
        return DataCodecManager.getInternalDataCodec();
    }

    default SendMessage ofSendMessage(CmdInfo cmdInfo, byte[] data) {
        var flowContext = FlowContextKeys.getFlowContext();
        var request = flowContext.getRequest();

        var message = SendMessage.of(cmdInfo, data);
        BarMessageKit.employ(request, message);

        Server server = flowContext.getServer();
        message.setNetId(server.netId());
        message.setSourceServerId(server.id());

        return message;
    }

    default SendMessage ofSendMessage(CmdInfo cmdInfo, int data) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encode(data));
    }

    default SendMessage ofSendMessage(CmdInfo cmdInfo, long data) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encode(data));
    }

    default SendMessage ofSendMessage(CmdInfo cmdInfo, boolean data) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encode(data));
    }

    default SendMessage ofSendMessage(CmdInfo cmdInfo, String data) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encode(data));
    }

    default SendMessage ofSendMessage(CmdInfo cmdInfo, Object data) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encode(data));
    }

    default SendMessage ofSendMessage(CmdInfo cmdInfo, List<?> dataList) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encodeList(dataList));
    }

    default SendMessage ofSendMessageListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encodeListInt(dataList));
    }

    default SendMessage ofSendMessageListLong(CmdInfo cmdInfo, List<Long> dataList) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encodeListLong(dataList));
    }

    default SendMessage ofSendMessageListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encodeListBool(dataList));
    }

    default SendMessage ofSendMessageListString(CmdInfo cmdInfo, List<String> dataList) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encodeListString(dataList));
    }

    default void send(SendMessage message) {
        this.getCommunicationAggregation().send(message);
    }

    default void send(CmdInfo cmdInfo, byte[] data) {
        send(ofSendMessage(cmdInfo, data));
    }

    default void send(CmdInfo cmdInfo) {
        send(cmdInfo, CommonConst.emptyBytes);
    }

    default void send(CmdInfo cmdInfo, int data) {
        send(ofSendMessage(cmdInfo, data));
    }

    default void send(CmdInfo cmdInfo, long data) {
        send(ofSendMessage(cmdInfo, data));
    }

    default void send(CmdInfo cmdInfo, boolean data) {
        send(ofSendMessage(cmdInfo, data));
    }

    default void send(CmdInfo cmdInfo, String data) {
        send(ofSendMessage(cmdInfo, data));
    }

    default void send(CmdInfo cmdInfo, Object data) {
        send(ofSendMessage(cmdInfo, data));
    }

    default void send(CmdInfo cmdInfo, List<?> dataList) {
        send(ofSendMessage(cmdInfo, dataList));
    }

    default void sendListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        send(ofSendMessageListInt(cmdInfo, dataList));
    }

    default void sendListLong(CmdInfo cmdInfo, List<Long> dataList) {
        send(ofSendMessageListLong(cmdInfo, dataList));
    }

    default void sendListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        send(ofSendMessageListBool(cmdInfo, dataList));
    }

    default void sendListString(CmdInfo cmdInfo, List<String> dataList) {
        send(ofSendMessageListString(cmdInfo, dataList));
    }
}
