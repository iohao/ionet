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
 * PluginInOut - <a href="https://iohao.github.io/ionet/docs/core_plugin/action_debug">DebugInOut</a>。
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
public final class DebugInOut implements ActionMethodInOut {
    final long time;
    @Setter
    BiConsumer<String, FlowContext> printConsumer = (message, _) -> IonetBanner.printlnMsg(message);

    public DebugInOut() {
        this(0);
    }

    public DebugInOut(long time) {
        this.time = time;
    }

    @Override
    public void fuckIn(final FlowContext flowContext) {
        flowContext.getNanoTime();
    }

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

    private void extractedHopCount(Request request, Map<String, Object> paramMap) {
        if (request.getHopCount() > 0) {
            paramMap.put("hopCount", " [%s:%s] ".formatted(
                    Bundle.getMessage(MessageKey.debugInOutHopCountName),
                    request.getHopCount()
            ));
        }
    }

    private void extractedI18n(Map<String, Object> paramMap) {
        Consumer<String> i18n = key -> paramMap.put(key, Bundle.getMessage(key));

        i18n.accept(MessageKey.debugInOutThreadName);
        i18n.accept(MessageKey.debugInOutParamName);
        i18n.accept(MessageKey.debugInOutReturnData);
        i18n.accept(MessageKey.debugInOutErrorCode);
        i18n.accept(MessageKey.debugInOutErrorMessage);
        i18n.accept(MessageKey.debugInOutTime);
    }

    private void extractedTraceId(Request request, Map<String, Object> paramMap) {
        String traceId = request.getTraceId();
        if (traceId != null) {
            paramMap.put("traceId", " [traceId:%s] ".formatted(traceId));
        }
    }

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
