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

import com.iohao.net.framework.core.*;
import com.iohao.net.framework.core.codec.*;
import com.iohao.net.framework.core.exception.*;
import com.iohao.net.framework.core.flow.*;
import com.iohao.net.framework.core.flow.parser.*;
import com.iohao.net.framework.core.kit.*;
import java.util.*;

/**
 * Default action method invoker using {@link java.lang.invoke.MethodHandle} for high-performance
 * method dispatch.
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public final class DefaultActionMethodInvoke implements ActionMethodInvoke {
    /**
     * Invoke the action method via MethodHandle, handling parameter parsing and validation.
     * <p>
     * Parses request data into the expected parameter type, runs JSR-380 validation if configured,
     * and dispatches the call through the appropriate {@link java.lang.invoke.MethodHandle} based
     * on the action's parameter position layout. Any exception thrown during invocation is caught
     * and converted into a {@link com.iohao.net.framework.core.exception.MessageException}.
     *
     * @param flowContext the current request flow context
     * @return the action method return value, or {@code null} if an error occurred
     */
    @Override
    public Object invoke(FlowContext flowContext) {
        try {
            var actionCommand = flowContext.getActionCommand();
            extractedValidator(flowContext, actionCommand);
            if (flowContext.hasError()) {
                return null;
            }

            var actionParameterPosition = actionCommand.actionParameterPosition;
            var methodHandle = actionCommand.methodHandle;
            var instance = flowContext.getActionController();

            return switch (actionParameterPosition) {
                case dataAndFlowContext -> methodHandle.invoke(instance, flowContext.getDataParam(), flowContext);
                case flowContext -> methodHandle.invoke(instance, flowContext);
                case flowContextAndData -> methodHandle.invoke(instance, flowContext, flowContext.getDataParam());
                case data -> methodHandle.invoke(instance, flowContext.getDataParam());
                case none -> methodHandle.invoke(instance);
            };
        } catch (Throwable e) {
            var barSkeleton = flowContext.getBarSkeleton();
            var exceptionProcess = barSkeleton.actionMethodExceptionProcess;
            var msgException = exceptionProcess.processException(e);
            flowContext.setErrorCode(msgException.getErrorCode());
            flowContext.setErrorMessage(msgException.getMessage());

            return msgException;
        }
    }

    /**
     * Extract, parse, and validate the request data parameter.
     *
     * @param flowContext   the current request flow context
     * @param actionCommand the action command metadata
     */
    private void extractedValidator(FlowContext flowContext, ActionCommand actionCommand) {
        if (!actionCommand.hasDataParameter()) {
            return;
        }

        var dataParam = actionCommand.dataParameter;
        var paramClazz = dataParam.getActualTypeArgumentClass();
        var methodParser = MethodParsers.getMethodParser(paramClazz);

        var codec = DataCodecManager.getDataCodec(flowContext.getCommunicationType());
        var dataBytes = flowContext.getRequest().getData();
        var data = methodParser.parseParam(dataBytes, dataParam, codec);
        flowContext.setDataParam(data);

        if (dataParam.validator) {
            // JSR380
            Class<?>[] groups = dataParam.validatorGroup;
            String validateMsg = ValidatorKit.validate(data, groups);
            if (Objects.nonNull(validateMsg)) {
                int errorCode = ActionErrorEnum.validateErrCode.getCode();
                flowContext.setErrorCode(errorCode);
                flowContext.setErrorMessage(validateMsg);
            }
        }
    }

    private DefaultActionMethodInvoke() {
    }

    /**
     * Return the singleton instance.
     *
     * @return the singleton {@code DefaultActionMethodInvoke}
     */
    public static DefaultActionMethodInvoke me() {
        return Holder.ME;
    }

    private static class Holder {
        static final DefaultActionMethodInvoke ME = new DefaultActionMethodInvoke();
    }
}