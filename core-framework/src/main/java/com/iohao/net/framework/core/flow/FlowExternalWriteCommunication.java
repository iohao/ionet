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

import com.iohao.net.framework.core.codec.DataCodec;
import com.iohao.net.framework.core.codec.DataCodecManager;
import com.iohao.net.framework.communication.CommunicationType;
import com.iohao.net.framework.communication.ExternalCommunicationDecorator;
import com.iohao.net.framework.core.flow.parser.MethodParser;
import com.iohao.net.framework.core.flow.parser.MethodParsers;
import com.iohao.net.framework.core.kit.BarMessageKit;
import com.iohao.net.framework.protocol.Response;
import com.iohao.net.framework.protocol.ResponseMessage;
import com.iohao.net.framework.protocol.UserResponseMessage;
import com.iohao.net.common.kit.CollKit;

import java.util.List;

/**
 * Flow-level communication for writing response data back to the external client, supporting
 * various data types and list encodings.
 *
 * @author 渔民小镇
 * @date 2025-10-09
 * @since 25.1
 */
public interface FlowExternalWriteCommunication extends FlowCommon, ExternalCommunicationDecorator {
    /** Write an int response message to the external client. @see #writeMessage(Object) */
    default void writeMessage(int data) {
        _writeMessage(data);
    }

    /** Write a long response message to the external client. @see #writeMessage(Object) */
    default void writeMessage(long data) {
        _writeMessage(data);
    }

    /** Write a boolean response message to the external client. @see #writeMessage(Object) */
    default void writeMessage(boolean data) {
        _writeMessage(data);
    }

    /** Write a String response message to the external client. @see #writeMessage(Object) */
    default void writeMessage(String data) {
        _writeMessage(data);
    }

    /**
     * Write a response message with the given data to the external client.
     * <p>
     * The data is encoded using the appropriate {@link DataCodec} based on the communication type,
     * wrapped in a response, and published to the external server via the communication aggregation.
     *
     * @param data the response data object to encode and send
     */
    default void writeMessage(Object data) {
        _writeMessage(data);
    }

    /**
     * Write a response message containing a list of objects to the external client.
     *
     * @param dataList the list of objects to encode and send
     * @see #writeMessage(Object)
     */
    default void writeMessage(List<?> dataList) {
        var response = ofResponse();

        if (CollKit.notEmpty(dataList)) {
            Class<?> typeClass = dataList.getFirst().getClass();
            var codec = this.getDataCodec();

            MethodParser methodParser = MethodParsers.getMethodParser(typeClass);
            var methodResult = methodParser.parseDataList(dataList, codec);
            byte[] dataBytes = codec.encode(methodResult);
            response.setData(dataBytes);
        }

        int netId = response.getNetId();
        this.getCommunicationAggregation().publishMessageByNetId(netId, response);
    }

    /** Create a response message appropriate for the current communication type. */
    private Response ofResponse() {
        Response response;
        var request = this.getRequest();
        if (this.getCommunicationType() == CommunicationType.INTERNAL_CALL) {
            var res = new ResponseMessage();
            BarMessageKit.employ(request, res);
            response = res;
        } else {
            var res = UserResponseMessage.of();
            BarMessageKit.employ(request, res);
            response = res;
        }

        var server = this.getServer();
        response.setSourceServerId(server.id());

        return response;
    }

    /** Get the data codec based on the current communication type. */
    private DataCodec getDataCodec() {
        return this.getCommunicationType() == CommunicationType.USER_REQUEST
                ? DataCodecManager.getDataCodec()
                : DataCodecManager.getInternalDataCodec();
    }

    /** Internal helper that encodes a single data object and writes the response. */
    private void _writeMessage(Object data) {

        MethodParser methodParser = MethodParsers.getMethodParser(data.getClass());
        var methodResult = methodParser.parseData(data);

        var codec = getDataCodec();
        byte[] dataBytes = codec.encode(methodResult);

        var response = ofResponse();
        response.setData(dataBytes);

        int netId = response.getNetId();
        this.getCommunicationAggregation().publishMessageByNetId(netId, response);
    }
}
