package com.iohao.net.framework.core.flow.internal;

import com.iohao.net.framework.IonetVersion;
import com.iohao.net.framework.core.flow.ActionMethodInOut;
import com.iohao.net.framework.core.flow.FlowContext;
import com.iohao.net.framework.i18n.Bundle;
import com.iohao.net.framework.i18n.MessageKey;
import com.iohao.net.framework.protocol.Request;
import com.iohao.net.framework.protocol.UserRequestMessage;
import com.iohao.net.framework.toy.IonetBanner;
import com.iohao.net.common.kit.StrKit;
import com.iohao.net.common.kit.time.TimeKit;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Debug plugin that logs detailed request/response information for each action method invocation.
 * <p>
 * Prints a structured diagnostic message including the action command, parameters, return value,
 * execution time, user ID, and error details (if any). An optional time threshold can be set
 * to only log invocations that exceed a given duration in milliseconds.
 * <p>
 * PluginInOut - <a href="https://iohao.github.io/ionet/docs/core_plugin/action_debug">DebugInOut</a>.
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
public final class DebugInOut implements ActionMethodInOut {
    final long time;
    @Setter
    BiConsumer<String, FlowContext> printConsumer = (message, _) -> IonetBanner.printlnMsg(message);

    /**
     * Create a DebugInOut with no time threshold (logs every invocation).
     */
    public DebugInOut() {
        this(0);
    }

    /**
     * Create a DebugInOut that only logs invocations exceeding the given time threshold.
     *
     * @param time minimum elapsed time in milliseconds to trigger logging
     */
    public DebugInOut(long time) {
        this.time = time;
    }

    /**
     * Record the start time before action method execution.
     *
     * @param flowContext the current request flow context
     */
    @Override
    public void fuckIn(final FlowContext flowContext) {
        flowContext.getNanoTime();
    }

    /**
     * Log the debug information after action method execution.
     * <p>
     * Skips logging if the elapsed time is below the configured threshold.
     * Delegates to either {@code printError} or {@code printNormal} based on
     * whether the flow context contains an error.
     *
     * @param flowContext the current request flow context
     */
    @Override
    public void fuckOut(final FlowContext flowContext) {
        long ms = TimeKit.elapsedMillis(flowContext.getNanoTime());
        if (this.time > ms) {
            return;
        }

        var actionCommand = flowContext.getActionCommand();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("IonetVersion", IonetVersion.VERSION);
        paramMap.put("threadName", Thread.currentThread().getName());
        paramMap.put("className", actionCommand.actionControllerClass.getSimpleName());
        paramMap.put("actionMethodName", actionCommand.getActionMethodName());
        paramMap.put("time", ms);
        paramMap.put("lineNumber", actionCommand.actionCommandDoc.lineNumber);

        var cmdInfo = flowContext.getCmdInfo();
        paramMap.put("cmdInfo", "[%d-%d]".formatted(cmdInfo.cmd(), cmdInfo.subCmd()));
        paramMap.put("userId", flowContext.getUserId());

        paramMap.put("paramData", actionCommand.hasDataParameter() ? flowContext.getDataParam() : "");
        paramMap.put("paramInfo", Objects.requireNonNullElse(actionCommand.dataParameter, ""));

        var actionMethodReturn = actionCommand.actionMethodReturn;
        paramMap.put("returnInfo", actionMethodReturn.toString());

        var server = flowContext.getServer();
        paramMap.put("logicServerId", server.id());
        paramMap.put("logicServerTag", server.tag());

        var request = flowContext.getRequest();
        paramMap.put("userIdKey", request.isVerifyIdentity() ? "userId" : "userChannelId");

        paramMap.put("joinName", "");
        paramMap.put("traceId", "");
        paramMap.put("hopCount", "");

        extractedJoin(request, paramMap);
        extractedTraceId(request, paramMap);
        extractedHopCount(request, paramMap);
        extractedI18n(paramMap);

        if (flowContext.hasError()) {
            this.printValidate(flowContext, paramMap);
        } else {
            this.printNormal(flowContext, paramMap);
        }
    }

    /**
     * Extract the hop count from the request and add it to the parameter map.
     *
     * @param request  the request message
     * @param paramMap the template parameter map
     */
    private void extractedHopCount(Request request, Map<String, Object> paramMap) {
        if (request.getHopCount() > 0) {
            paramMap.put("hopCount", " [%s:%s] ".formatted(
                    Bundle.getMessage(MessageKey.debugInOutHopCountName),
                    request.getHopCount()
            ));
        }
    }

    /**
     * Populate i18n-resolved label strings into the parameter map.
     *
     * @param paramMap the template parameter map
     */
    private void extractedI18n(Map<String, Object> paramMap) {
        Consumer<String> i18n = key -> paramMap.put(key, Bundle.getMessage(key));

        i18n.accept(MessageKey.debugInOutThreadName);
        i18n.accept(MessageKey.debugInOutParamName);
        i18n.accept(MessageKey.debugInOutReturnData);
        i18n.accept(MessageKey.debugInOutErrorCode);
        i18n.accept(MessageKey.debugInOutErrorMessage);
        i18n.accept(MessageKey.debugInOutTime);
    }

    /**
     * Extract the trace ID from the request and add it to the parameter map.
     *
     * @param request  the request message
     * @param paramMap the template parameter map
     */
    private void extractedTraceId(Request request, Map<String, Object> paramMap) {
        String traceId = request.getTraceId();
        if (traceId != null) {
            paramMap.put("traceId", " [traceId:%s] ".formatted(traceId));
        }
    }

    /**
     * Extract the connection type (TCP/WebSocket/UDP) from the request and add it to the parameter map.
     *
     * @param request  the request message
     * @param paramMap the template parameter map
     */
    private void extractedJoin(Request request, Map<String, Object> paramMap) {
        if (request instanceof UserRequestMessage) {
            String connectionWay = Bundle.getMessage(MessageKey.connectionWay);
            String str = switch (request.getStick()) {
                case 1 -> " [%s:TCP] ".formatted(connectionWay);
                case 2 -> " [%s:WebSocket] ".formatted(connectionWay);
                case 3 -> " [%s:UDP] ".formatted(connectionWay);
                default -> "";
            };

            paramMap.put("joinName", str);
        }
    }

    /**
     * Print error details when the action method resulted in a validation or business error.
     *
     * @param flowContext the current request flow context
     * @param paramMap    the template parameter map
     */
    private void printValidate(FlowContext flowContext, Map<String, Object> paramMap) {
        paramMap.put("errorCode", flowContext.getErrorCode());
        paramMap.put("validatorMsg", flowContext.getErrorMessage());

        if (StrKit.isEmpty(flowContext.getErrorMessage())) {
            paramMap.put("validatorMsg", flowContext.getErrorMessage());
        }

        var template = """
                ┏━━ Error.({className}.java:{lineNumber}) ━━ [{returnInfo} {actionMethodName}({paramInfo})] ━━ {cmdInfo} ━━ [tag:{logicServerTag}, serverId:{logicServerId}] ━━━━
                ┣ {userIdKey}: {userId}
                ┣ {debugInOutParamName}: {paramData}
                ┣ {debugInOutErrorCode}: {errorCode}
                ┣ {debugInOutErrorMessage}: {validatorMsg}
                ┣ {debugInOutTime}: {time} ms
                ┗━━ [ionet:{IonetVersion}] ━━ [{debugInOutThreadName}:{threadName}] ━━{joinName}━━{traceId}━━{hopCount}━━━━━━━━━━━━━━━━━━━━━━━━━━━
                """;

        var message = StrKit.format(template, paramMap);
        this.printConsumer.accept(message, flowContext);
    }

    /**
     * Print normal debug output when the action method completed successfully.
     *
     * @param flowContext the current request flow context
     * @param paramMap    the template parameter map
     */
    private void printNormal(FlowContext flowContext, Map<String, Object> paramMap) {
        var actionCommand = flowContext.getActionCommand();
        paramMap.put("returnData", "void");

        if (!actionCommand.actionMethodReturn.isVoid()) {
            paramMap.put("returnData", Objects.requireNonNullElse(flowContext.getOriginalMethodResult(), "null"));
        }

        var template = """
                ┏━━ Debug.({className}.java:{lineNumber}) ━━ [{returnInfo} {actionMethodName}({paramInfo})] ━━ {cmdInfo} ━━ [tag:{logicServerTag}, serverId:{logicServerId}] ━━━━
                ┣ {userIdKey}: {userId}
                ┣ {debugInOutParamName}: {paramData}
                ┣ {debugInOutReturnData}: {returnData}
                ┣ {debugInOutTime}: {time} ms
                ┗━━ [ionet:{IonetVersion}] ━━ [{debugInOutThreadName}:{threadName}] ━━{joinName}━━{traceId}━━{hopCount}━━━━━━━━━━━━━━━━━━━━━━━━━━━
                """;
        var message = StrKit.format(template, paramMap);
        this.printConsumer.accept(message, flowContext);
    }
}
