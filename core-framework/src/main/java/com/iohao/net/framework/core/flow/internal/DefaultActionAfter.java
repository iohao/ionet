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
package com.iohao.net.framework.core.flow.internal;

import com.iohao.net.framework.core.codec.*;
import com.iohao.net.framework.core.flow.*;
import com.iohao.net.framework.core.flow.parser.*;
import com.iohao.net.framework.core.kit.*;
import com.iohao.net.framework.protocol.*;
import lombok.extern.slf4j.*;

/**
 * Default {@link com.iohao.net.framework.core.flow.ActionAfter} implementation that encodes
 * the action method result into the response message.
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
@Slf4j
public final class DefaultActionAfter implements ActionAfter {
    /**
     * Encode the action method result and prepare the response for sending.
     * <p>
     * Delegates to the appropriate handler based on the communication type
     * (user request or internal call).
     *
     * @param flowContext the current request flow context
     */
    @Override
    public void execute(FlowContext flowContext) {
        switch (flowContext.getCommunicationType()) {
            case USER_REQUEST -> processUserRequest(flowContext);
            case INTERNAL_CALL -> processInternalRequest(flowContext);
        }
    }

    /**
     * Process an internal (logic-to-logic) call response.
     *
     * @param flowContext the current request flow context
     */
    private void processInternalRequest(FlowContext flowContext) {
        var request = flowContext.getRequest();
        var server = flowContext.getServer();

        var response = new ResponseMessage();
        response.setSourceServerId(server.id());
        BarMessageKit.employ(request, response);

        var netId = response.getNetId();
        var communicationAggregation = flowContext.getCommunicationAggregation();

        if (flowContext.hasError()) {
            response.setErrorCode(flowContext.getErrorCode());
            response.setErrorMessage(flowContext.getErrorMessage());
            communicationAggregation.publishMessageByNetId(netId, response);

            return;
        }

        var codec = DataCodecManager.getInternalDataCodec();
        var actionMethodReturnInfo = flowContext.getActionCommand().actionMethodReturn;
        var paramParser = MethodParsers.getMethodParser(actionMethodReturnInfo);
        var result = flowContext.getOriginalMethodResult();

        var methodResult = actionMethodReturnInfo.isList()
                ? paramParser.parseDataList(result, codec)
                : paramParser.parseData(result);

        flowContext.setMethodResult(methodResult);

        byte[] dataBytes = codec.encode(methodResult);
        response.setData(dataBytes);
        communicationAggregation.publishMessageByNetId(netId, response);
    }

    /**
     * Process a user request response, encoding the result and writing it back to the client.
     *
     * @param flowContext the current request flow context
     */
    private void processUserRequest(FlowContext flowContext) {
        if (flowContext.hasError()) {
            var response = ofUserResponseMessage(flowContext);
            response.setErrorCode(flowContext.getErrorCode());
            response.setErrorMessage(flowContext.getErrorMessage());

            flowContext.getCommunicationAggregation().writeMessage(response);
            return;
        }

        var actionMethodReturnInfo = flowContext.getActionCommand().actionMethodReturn;
        if (actionMethodReturnInfo.isVoid()) {
            return;
        }

        var codec = DataCodecManager.getDataCodec();
        var paramParser = MethodParsers.getMethodParser(actionMethodReturnInfo);
        var result = flowContext.getOriginalMethodResult();

        var methodResult = actionMethodReturnInfo.isList()
                ? paramParser.parseDataList(result, codec)
                : paramParser.parseData(result);

        flowContext.setMethodResult(methodResult);

        var response = ofUserResponseMessage(flowContext);
        response.setData(codec.encode(methodResult));

        flowContext.getCommunicationAggregation().writeMessage(response);
    }

    /**
     * Create a {@link UserResponseMessage} populated with request metadata.
     *
     * @param flowContext the current request flow context
     * @return a new user response message
     */
    private UserResponseMessage ofUserResponseMessage(FlowContext flowContext) {
        var request = flowContext.getRequest();
        var server = flowContext.getServer();

        var response = UserResponseMessage.of();
        response.setSourceServerId(server.id());
        BarMessageKit.employ(request, response);

        return response;
    }

    private DefaultActionAfter() {
    }

    /**
     * Return the singleton instance.
     *
     * @return the singleton {@code DefaultActionAfter}
     */
    public static DefaultActionAfter me() {
        return Holder.ME;
    }

    private static class Holder {
        static final DefaultActionAfter ME = new DefaultActionAfter();
    }
}
