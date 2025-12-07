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

    default boolean hasError() {
        return getErrorCode() != 0;
    }

    default int getCmdMerge() {
        return getCmdInfo().cmdMerge();
    }

    long getNanoTime();

    long getUserId();

    void setUserId(long userId);

    BarSkeleton getBarSkeleton();

    void setBarSkeleton(BarSkeleton barSkeleton);

    ActionCommand getActionCommand();

    void setActionCommand(ActionCommand actionCommand);

    Object getActionController();

    void setActionController(Object actionController);

    Request getRequest();

    void setRequest(Request request);

    Object getMethodResult();

    void setMethodResult(Object methodResult);

    Object getOriginalMethodResult();

    void setOriginalMethodResult(Object originalMethodResult);

    CommunicationType getCommunicationType();

    void setCommunicationType(CommunicationType communicationType);

    int getErrorCode();

    void setErrorCode(int errorCode);

    String getErrorMessage();

    void setErrorMessage(String errorMessage);

    CommunicationAggregation getCommunicationAggregation();

    void setCommunicationAggregation(CommunicationAggregation communicationAggregation);

    Object getDataParam();

    void setDataParam(Object dataParam);

    CmdInfo getCmdInfo();

    void setCmdInfo(CmdInfo cmdInfo);

    ThreadExecutor getCurrentThreadExecutor();

    void setCurrentThreadExecutor(ThreadExecutor currentThreadExecutor);
}
