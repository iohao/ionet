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

import com.iohao.net.framework.core.ActionCommand;
import com.iohao.net.framework.core.BarSkeleton;
import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.communication.CommunicationAggregation;
import com.iohao.net.framework.communication.CommunicationType;
import com.iohao.net.framework.protocol.Request;
import com.iohao.net.common.kit.concurrent.executor.ThreadExecutor;

/**
 * The FlowContext's lifecycle is limited to the duration of a single request flow.
 *
 * @author 渔民小镇
 * @date 2021-12-21
 */
public interface FlowContext extends
        // External
        FlowAttachmentCommunication
        , FLowUserIdSettingCommunication
        , FlowExternalCommunication
        // Logic
        , FlowLogicCallCommunication
        , FlowLogicSendCommunication
        // Broadcast
        , FlowBroadcastCommunication
        // Enterprise
        , FlowCommunicationEventBus
        , FlowExternalWriteCommunication
        , FlowLogicCallCollectCommunication
        , FlowBindingLogicServerCommunication {

    /**
     * Check if the flow context has an error.
     *
     * @return {@code true} if the error code is non-zero
     */
    default boolean hasError() {
        return getErrorCode() != 0;
    }

    /**
     * Get the merged command ID ({@code cmd << 16 | subCmd}).
     *
     * @return the merged command identifier
     */
    default int getCmdMerge() {
        return getCmdInfo().cmdMerge();
    }

    /**
     * Get the nano time when this flow context was created.
     *
     * @return creation timestamp in nanoseconds
     */
    long getNanoTime();

    /**
     * Get the user ID associated with this request.
     *
     * @return the user ID
     */
    long getUserId();

    /**
     * Set the user ID associated with this request.
     *
     * @param userId the user ID
     */
    void setUserId(long userId);

    /**
     * Get the BarSkeleton that is processing this flow.
     *
     * @return the bar skeleton instance
     */
    BarSkeleton getBarSkeleton();

    /**
     * Set the BarSkeleton for this flow.
     *
     * @param barSkeleton the bar skeleton instance
     */
    void setBarSkeleton(BarSkeleton barSkeleton);

    /**
     * Get the action command metadata for the current request.
     *
     * @return the action command
     */
    ActionCommand getActionCommand();

    /**
     * Set the action command metadata.
     *
     * @param actionCommand the action command
     */
    void setActionCommand(ActionCommand actionCommand);

    /**
     * Get the action controller instance handling this request.
     *
     * @return the controller instance
     */
    Object getActionController();

    /**
     * Set the action controller instance.
     *
     * @param actionController the controller instance
     */
    void setActionController(Object actionController);

    /**
     * Get the request message.
     *
     * @return the request
     */
    Request getRequest();

    /**
     * Set the request message.
     *
     * @param request the request
     */
    void setRequest(Request request);

    /**
     * Get the result returned by the action method.
     *
     * @return the method result
     */
    Object getMethodResult();

    /**
     * Set the action method result.
     *
     * @param methodResult the method result
     */
    void setMethodResult(Object methodResult);

    /**
     * Get the original unprocessed result from the action method.
     *
     * @return the original method result
     */
    Object getOriginalMethodResult();

    /**
     * Set the original action method result.
     *
     * @param originalMethodResult the original method result
     */
    void setOriginalMethodResult(Object originalMethodResult);

    /**
     * Get the communication type for this flow.
     *
     * @return the communication type
     */
    CommunicationType getCommunicationType();

    /**
     * Set the communication type.
     *
     * @param communicationType the communication type
     */
    void setCommunicationType(CommunicationType communicationType);

    /**
     * Get the error code, 0 means no error.
     *
     * @return the error code
     */
    int getErrorCode();

    /**
     * Set the error code.
     *
     * @param errorCode the error code
     */
    void setErrorCode(int errorCode);

    /**
     * Get the error message.
     *
     * @return the error message, or {@code null} if none
     */
    String getErrorMessage();

    /**
     * Set the error message.
     *
     * @param errorMessage the error message
     */
    void setErrorMessage(String errorMessage);

    /**
     * Get the communication aggregation for cross-service calls.
     *
     * @return the communication aggregation
     */
    CommunicationAggregation getCommunicationAggregation();

    /**
     * Set the communication aggregation.
     *
     * @param communicationAggregation the communication aggregation
     */
    void setCommunicationAggregation(CommunicationAggregation communicationAggregation);

    /**
     * Get the deserialized request data parameter.
     *
     * @return the data parameter
     */
    Object getDataParam();

    /**
     * Set the request data parameter.
     *
     * @param dataParam the data parameter
     */
    void setDataParam(Object dataParam);

    /**
     * Get the command info (cmd, subCmd) for this request.
     *
     * @return the command info
     */
    CmdInfo getCmdInfo();

    /**
     * Set the command info.
     *
     * @param cmdInfo the command info
     */
    void setCmdInfo(CmdInfo cmdInfo);

    /**
     * Get the thread executor assigned to this flow.
     *
     * @return the thread executor
     */
    ThreadExecutor getCurrentThreadExecutor();

    /**
     * Set the thread executor for this flow.
     *
     * @param currentThreadExecutor the thread executor
     */
    void setCurrentThreadExecutor(ThreadExecutor currentThreadExecutor);
}
