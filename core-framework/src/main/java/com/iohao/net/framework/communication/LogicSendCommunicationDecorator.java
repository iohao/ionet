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
 * Decorator providing fire-and-forget message sending to other logic servers.
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
public interface LogicSendCommunicationDecorator extends CommonDecorator {
    private DataCodec getInternalDataCodec() {
        return DataCodecManager.getInternalDataCodec();
    }

    /**
     * Create a SendMessage for the given command with raw byte data.
     * <p>Populates the message with trace context, net ID, and source server ID
     * from the current flow context.
     *
     * @param cmdInfo the target command route
     * @param data    the raw byte payload
     * @return a fully initialized {@link SendMessage}
     */
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

    /** Create a SendMessage with int payload. @see #ofSendMessage(CmdInfo, byte[]) */
    default SendMessage ofSendMessage(CmdInfo cmdInfo, int data) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encode(data));
    }

    /** Create a SendMessage with long payload. @see #ofSendMessage(CmdInfo, byte[]) */
    default SendMessage ofSendMessage(CmdInfo cmdInfo, long data) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encode(data));
    }

    /** Create a SendMessage with boolean payload. @see #ofSendMessage(CmdInfo, byte[]) */
    default SendMessage ofSendMessage(CmdInfo cmdInfo, boolean data) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encode(data));
    }

    /** Create a SendMessage with String payload. @see #ofSendMessage(CmdInfo, byte[]) */
    default SendMessage ofSendMessage(CmdInfo cmdInfo, String data) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encode(data));
    }

    /** Create a SendMessage with Object payload. @see #ofSendMessage(CmdInfo, byte[]) */
    default SendMessage ofSendMessage(CmdInfo cmdInfo, Object data) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encode(data));
    }

    /** Create a SendMessage with List payload. @see #ofSendMessage(CmdInfo, byte[]) */
    default SendMessage ofSendMessage(CmdInfo cmdInfo, List<?> dataList) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encodeList(dataList));
    }

    /** Create a SendMessage with List&lt;Integer&gt; payload. @see #ofSendMessage(CmdInfo, byte[]) */
    default SendMessage ofSendMessageListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encodeListInt(dataList));
    }

    /** Create a SendMessage with List&lt;Long&gt; payload. @see #ofSendMessage(CmdInfo, byte[]) */
    default SendMessage ofSendMessageListLong(CmdInfo cmdInfo, List<Long> dataList) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encodeListLong(dataList));
    }

    /** Create a SendMessage with List&lt;Boolean&gt; payload. @see #ofSendMessage(CmdInfo, byte[]) */
    default SendMessage ofSendMessageListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encodeListBool(dataList));
    }

    /** Create a SendMessage with List&lt;String&gt; payload. @see #ofSendMessage(CmdInfo, byte[]) */
    default SendMessage ofSendMessageListString(CmdInfo cmdInfo, List<String> dataList) {
        var codec = this.getInternalDataCodec();
        return ofSendMessage(cmdInfo, codec.encodeListString(dataList));
    }

    /**
     * Send a message to another logic server.
     * <p>This is a fire-and-forget operation; no response is returned.
     *
     * @param message the message to send
     */
    default void send(SendMessage message) {
        this.getCommunicationAggregation().send(message);
    }

    /** Send raw bytes to another logic server. @see #send(SendMessage) */
    default void send(CmdInfo cmdInfo, byte[] data) {
        send(ofSendMessage(cmdInfo, data));
    }

    /** Send with no payload. @see #send(SendMessage) */
    default void send(CmdInfo cmdInfo) {
        send(cmdInfo, CommonConst.emptyBytes);
    }

    /** Send with int payload. @see #send(SendMessage) */
    default void send(CmdInfo cmdInfo, int data) {
        send(ofSendMessage(cmdInfo, data));
    }

    /** Send with long payload. @see #send(SendMessage) */
    default void send(CmdInfo cmdInfo, long data) {
        send(ofSendMessage(cmdInfo, data));
    }

    /** Send with boolean payload. @see #send(SendMessage) */
    default void send(CmdInfo cmdInfo, boolean data) {
        send(ofSendMessage(cmdInfo, data));
    }

    /** Send with String payload. @see #send(SendMessage) */
    default void send(CmdInfo cmdInfo, String data) {
        send(ofSendMessage(cmdInfo, data));
    }

    /** Send with Object payload. @see #send(SendMessage) */
    default void send(CmdInfo cmdInfo, Object data) {
        send(ofSendMessage(cmdInfo, data));
    }

    /** Send with List payload. @see #send(SendMessage) */
    default void send(CmdInfo cmdInfo, List<?> dataList) {
        send(ofSendMessage(cmdInfo, dataList));
    }

    /** Send with List&lt;Integer&gt; payload. @see #send(SendMessage) */
    default void sendListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        send(ofSendMessageListInt(cmdInfo, dataList));
    }

    /** Send with List&lt;Long&gt; payload. @see #send(SendMessage) */
    default void sendListLong(CmdInfo cmdInfo, List<Long> dataList) {
        send(ofSendMessageListLong(cmdInfo, dataList));
    }

    /** Send with List&lt;Boolean&gt; payload. @see #send(SendMessage) */
    default void sendListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        send(ofSendMessageListBool(cmdInfo, dataList));
    }

    /** Send with List&lt;String&gt; payload. @see #send(SendMessage) */
    default void sendListString(CmdInfo cmdInfo, List<String> dataList) {
        send(ofSendMessageListString(cmdInfo, dataList));
    }
}
