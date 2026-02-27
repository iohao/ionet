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

import com.iohao.net.framework.core.*;
import com.iohao.net.framework.core.codec.*;
import com.iohao.net.framework.protocol.*;
import java.util.*;

/**
 * Decorator interface that provides convenience factory methods for creating {@link RequestMessage}
 * instances with various data types.
 * <p>
 * Each overload encodes the given data using the internal {@link DataCodec} before delegating
 * to the core {@link #ofRequestMessage(CmdInfo, byte[])} method. This eliminates repetitive
 * codec lookup and encoding boilerplate in calling code.
 *
 * @author 渔民小镇
 * @date 2025-10-09
 * @since 25.1
 */
public interface LogicCommunicationDecorator {
    /** Return the internal data codec used for encoding request payloads. */
    private DataCodec getInternalDataCodec() {
        return DataCodecManager.getInternalDataCodec();
    }

    /**
     * Create a {@link RequestMessage} with raw byte-array data for the given command.
     *
     * @param cmdInfo   the command routing information
     * @param dataBytes the serialized request data
     * @return a new request message
     */
    RequestMessage ofRequestMessage(CmdInfo cmdInfo, final byte[] dataBytes);

    /**
     * Create a {@link RequestMessage} with an {@code int} payload.
     *
     * @param cmdInfo the command routing information
     * @param data    the integer payload
     * @return a new request message
     */
    default RequestMessage ofRequestMessage(CmdInfo cmdInfo, int data) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encode(data));
    }

    /**
     * Create a {@link RequestMessage} with a {@code long} payload.
     *
     * @param cmdInfo the command routing information
     * @param data    the long payload
     * @return a new request message
     */
    default RequestMessage ofRequestMessage(CmdInfo cmdInfo, long data) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encode(data));
    }

    /**
     * Create a {@link RequestMessage} with a {@code boolean} payload.
     *
     * @param cmdInfo the command routing information
     * @param data    the boolean payload
     * @return a new request message
     */
    default RequestMessage ofRequestMessage(CmdInfo cmdInfo, boolean data) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encode(data));
    }

    /**
     * Create a {@link RequestMessage} with a {@link String} payload.
     *
     * @param cmdInfo the command routing information
     * @param data    the string payload
     * @return a new request message
     */
    default RequestMessage ofRequestMessage(CmdInfo cmdInfo, String data) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encode(data));
    }

    /**
     * Create a {@link RequestMessage} with a business object payload.
     *
     * @param cmdInfo the command routing information
     * @param data    the business data object
     * @return a new request message
     */
    default RequestMessage ofRequestMessage(CmdInfo cmdInfo, Object data) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encode(data));
    }

    /**
     * Create a {@link RequestMessage} with a list of business objects as payload.
     *
     * @param cmdInfo  the command routing information
     * @param dataList the list of business objects
     * @return a new request message
     */
    default RequestMessage ofRequestMessage(CmdInfo cmdInfo, List<?> dataList) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encodeList(dataList));
    }

    /**
     * Create a {@link RequestMessage} with a list of {@link Integer} values as payload.
     *
     * @param cmdInfo  the command routing information
     * @param dataList the list of integer values
     * @return a new request message
     */
    default RequestMessage ofRequestMessageListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encodeListInt(dataList));
    }

    /**
     * Create a {@link RequestMessage} with a list of {@link Long} values as payload.
     *
     * @param cmdInfo  the command routing information
     * @param dataList the list of long values
     * @return a new request message
     */
    default RequestMessage ofRequestMessageListLong(CmdInfo cmdInfo, List<Long> dataList) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encodeListLong(dataList));
    }

    /**
     * Create a {@link RequestMessage} with a list of {@link Boolean} values as payload.
     *
     * @param cmdInfo  the command routing information
     * @param dataList the list of boolean values
     * @return a new request message
     */
    default RequestMessage ofRequestMessageListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encodeListBool(dataList));
    }

    /**
     * Create a {@link RequestMessage} with a list of {@link String} values as payload.
     *
     * @param cmdInfo  the command routing information
     * @param dataList the list of string values
     * @return a new request message
     */
    default RequestMessage ofRequestMessageListString(CmdInfo cmdInfo, List<String> dataList) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encodeListString(dataList));
    }
}
