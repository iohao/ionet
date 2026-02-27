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
 * Decorator providing multicast broadcast methods to send messages to all connected users.
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
public interface BroadcastMulticastCommunicationDecorator extends CommonDecorator {
    /** Broadcast a pre-built multicast message. @see #broadcastMulticast(CmdInfo, byte[]) */
    default void broadcast(BroadcastMulticastMessage message) {
        this.getCommunicationAggregation().broadcast(message);
    }

    /**
     * Broadcast a message to all connected users.
     *
     * @param cmdInfo      the command route identifying the message type
     * @param data         the raw byte payload
     * @param originalData the original pre-encoded data object (retained for hooks/interceptors)
     */
    private void broadcastMulticast(CmdInfo cmdInfo, byte[] data, Object originalData) {
        var message = new BroadcastMulticastMessage();
        message.setCmdMerge(cmdInfo.cmdMerge());
        message.setData(data);
        message.setOriginalData(originalData);

        this.broadcast(message);
    }

    /** Broadcast raw bytes to all connected users. @see #broadcastMulticast(CmdInfo, byte[], Object) */
    default void broadcastMulticast(CmdInfo cmdInfo, byte[] data) {
        broadcastMulticast(cmdInfo, data, null);
    }

    /** Broadcast with no payload. @see #broadcastMulticast(CmdInfo, byte[], Object) */
    default void broadcastMulticast(CmdInfo cmdInfo) {
        this.broadcastMulticast(cmdInfo, CommonConst.emptyBytes, null);
    }

    /** Broadcast with int payload. @see #broadcastMulticast(CmdInfo, byte[], Object) */
    default void broadcastMulticast(CmdInfo cmdInfo, int data) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encode(data), data);
    }

    /** Broadcast with long payload. @see #broadcastMulticast(CmdInfo, byte[], Object) */
    default void broadcastMulticast(CmdInfo cmdInfo, long data) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encode(data), data);
    }

    /** Broadcast with boolean payload. @see #broadcastMulticast(CmdInfo, byte[], Object) */
    default void broadcastMulticast(CmdInfo cmdInfo, boolean data) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encode(data), data);
    }

    /** Broadcast with String payload. @see #broadcastMulticast(CmdInfo, byte[], Object) */
    default void broadcastMulticast(CmdInfo cmdInfo, String data) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encode(data), data);
    }

    /** Broadcast with Object payload. @see #broadcastMulticast(CmdInfo, byte[], Object) */
    default void broadcastMulticast(CmdInfo cmdInfo, Object data) {
        var codec = DataCodecManager.getDataCodec();
        broadcastMulticast(cmdInfo, codec.encode(data), data);
    }

    /** Broadcast with Collection payload. @see #broadcastMulticast(CmdInfo, byte[], Object) */
    default void broadcastMulticast(CmdInfo cmdInfo, Collection<?> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encodeList(dataList), dataList);
    }

    /** Broadcast with List&lt;Integer&gt; payload. @see #broadcastMulticast(CmdInfo, byte[], Object) */
    default void broadcastMulticastListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encodeListInt(dataList), dataList);
    }

    /** Broadcast with List&lt;Long&gt; payload. @see #broadcastMulticast(CmdInfo, byte[], Object) */
    default void broadcastMulticastListLong(CmdInfo cmdInfo, List<Long> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encodeListLong(dataList), dataList);
    }

    /** Broadcast with List&lt;Boolean&gt; payload. @see #broadcastMulticast(CmdInfo, byte[], Object) */
    default void broadcastMulticastListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encodeListBool(dataList), dataList);
    }

    /** Broadcast with List&lt;String&gt; payload. @see #broadcastMulticast(CmdInfo, byte[], Object) */
    default void broadcastMulticastListString(CmdInfo cmdInfo, List<String> dataList) {
        var codec = DataCodecManager.getDataCodec();
        this.broadcastMulticast(cmdInfo, codec.encodeListString(dataList), dataList);
    }
}
