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
package com.iohao.net.framework.core;

import com.iohao.net.framework.communication.*;
import com.iohao.net.framework.communication.eventbus.*;
import com.iohao.net.framework.core.exception.*;
import com.iohao.net.framework.core.flow.*;
import com.iohao.net.framework.core.flow.internal.*;
import com.iohao.net.framework.core.runner.*;
import com.iohao.net.framework.protocol.*;
import java.util.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests flow-level error response handling.
 *
 * @author 渔民小镇
 * @date 2026-05-01
 * @since 25.4
 */
class DefaultFlowExecutorTest {
    @Test
    void flowInFailureWritesUserErrorResponse() {
        var communication = new RecordingCommunicationAggregation();
        var skeleton = newSkeleton(new ThrowingInOut(new IllegalStateException("in failed")));
        skeleton.communicationAggregation = communication;
        skeleton.server = newServer(skeleton);

        skeleton.handle(newFlowContext(CommunicationType.USER_REQUEST, newUserRequest()));

        assertEquals(1, communication.userResponses.size());
        var response = communication.userResponses.getFirst();
        assertEquals(ActionErrorEnum.systemOtherErrCode.getCode(), response.getErrorCode());
        assertEquals(11, response.getMsgId());
        assertTrue(communication.publishedByNetId.isEmpty());
    }

    @Test
    void flowInFailurePublishesInternalCallErrorResponse() {
        var communication = new RecordingCommunicationAggregation();
        var skeleton = newSkeleton(new ThrowingInOut(new IllegalStateException("in failed")));
        skeleton.communicationAggregation = communication;
        skeleton.server = newServer(skeleton);

        skeleton.handle(newFlowContext(CommunicationType.INTERNAL_CALL, newInternalRequest()));

        assertEquals(1, communication.publishedByNetId.size());
        var published = communication.publishedByNetId.getFirst();
        assertEquals(9, published.netId);
        var response = assertInstanceOf(ResponseMessage.class, published.message);
        assertEquals(ActionErrorEnum.systemOtherErrCode.getCode(), response.getErrorCode());
        assertTrue(communication.userResponses.isEmpty());
    }

    @Test
    void actionInvokeErrorStillSendsOnlyOneResponse() {
        var communication = new RecordingCommunicationAggregation();
        var skeleton = newSkeleton(flowContext -> {
            flowContext.setErrorCode(ActionErrorEnum.systemOtherErrCode.getCode());
            flowContext.setErrorMessage(ActionErrorEnum.systemOtherErrCode.getMessage());
            return null;
        });
        skeleton.communicationAggregation = communication;
        skeleton.server = newServer(skeleton);

        skeleton.handle(newFlowContext(CommunicationType.USER_REQUEST, newUserRequest()));

        assertEquals(1, communication.userResponses.size());
        assertEquals(ActionErrorEnum.systemOtherErrCode.getCode(), communication.userResponses.getFirst().getErrorCode());
    }

    @Test
    void internalSendFlowFailureDoesNotSendResponse() {
        var communication = new RecordingCommunicationAggregation();
        var skeleton = newSkeleton(new ThrowingInOut(new IllegalStateException("in failed")));
        skeleton.communicationAggregation = communication;
        skeleton.server = newServer(skeleton);

        skeleton.handle(newFlowContext(CommunicationType.INTERNAL_SEND, newSendMessage()));

        assertTrue(communication.userResponses.isEmpty());
        assertTrue(communication.publishedByNetId.isEmpty());
    }

    @Test
    void controllerFactoryFailureWritesUserErrorResponse() {
        var communication = new RecordingCommunicationAggregation();
        var skeleton = newSkeleton(
                flowContext -> null,
                new NoopInOut(),
                DefaultActionAfter.me(),
                _ -> {
                    throw new IllegalStateException("factory failed");
                }
        );
        skeleton.communicationAggregation = communication;
        skeleton.server = newServer(skeleton);

        skeleton.handle(newFlowContext(CommunicationType.USER_REQUEST, newUserRequest()));

        assertEquals(1, communication.userResponses.size());
        var response = communication.userResponses.getFirst();
        assertEquals(ActionErrorEnum.systemOtherErrCode.getCode(), response.getErrorCode());
        assertEquals(11, response.getMsgId());
    }

    @Test
    void actionAfterFailureDoesNotEscapeExecutor() {
        var communication = new RecordingCommunicationAggregation();
        var skeleton = newSkeleton(
                flowContext -> null,
                new NoopInOut(),
                new ThrowingActionAfter(),
                _ -> new Object()
        );
        skeleton.communicationAggregation = communication;
        skeleton.server = newServer(skeleton);

        assertDoesNotThrow(() -> skeleton.handle(newFlowContext(CommunicationType.USER_REQUEST, newUserRequest())));
        assertTrue(communication.userResponses.isEmpty());
        assertTrue(communication.publishedByNetId.isEmpty());
    }

    @Test
    void flowOutFailureRunsFallbackActionAfterWithErrorState() {
        var actionAfter = new RecordingActionAfter();
        var communication = new RecordingCommunicationAggregation();
        var skeleton = newSkeleton(
                flowContext -> null,
                new ThrowingOut(new IllegalStateException("out failed")),
                actionAfter,
                _ -> new Object()
        );
        skeleton.communicationAggregation = communication;
        skeleton.server = newServer(skeleton);

        skeleton.handle(newFlowContext(CommunicationType.USER_REQUEST, newUserRequest()));

        assertEquals(List.of(false, true), actionAfter.errorStates);
    }

    @Test
    void fatalErrorsAreRethrown() {
        var communication = new RecordingCommunicationAggregation();
        var error = new LinkageError("fatal");
        var skeleton = newSkeleton(new ThrowingInOut(error));
        skeleton.communicationAggregation = communication;
        skeleton.server = newServer(skeleton);

        var thrown = assertThrows(LinkageError.class,
                () -> skeleton.handle(newFlowContext(CommunicationType.USER_REQUEST, newUserRequest())));

        assertSame(error, thrown);
        assertTrue(communication.userResponses.isEmpty());
        assertTrue(communication.publishedByNetId.isEmpty());
    }

    private static BarSkeleton newSkeleton(ActionMethodInOut inOut) {
        return newSkeleton(DefaultActionMethodInvoke.me(), inOut);
    }

    private static BarSkeleton newSkeleton(ActionMethodInvoke actionMethodInvoke) {
        return newSkeleton(actionMethodInvoke, new NoopInOut());
    }

    private static BarSkeleton newSkeleton(ActionMethodInvoke actionMethodInvoke, ActionMethodInOut inOut) {
        return newSkeleton(actionMethodInvoke, inOut, DefaultActionAfter.me(), _ -> new Object());
    }

    private static BarSkeleton newSkeleton(
            ActionMethodInvoke actionMethodInvoke,
            ActionMethodInOut inOut,
            ActionAfter actionAfter,
            ActionFactoryBean<Object> actionFactoryBean) {
        return BarSkeleton.internalBuilder()
                .setRunners(new Runners())
                .setActionAfter(actionAfter)
                .setFlowExecutor(new DefaultFlowExecutor())
                .setInOuts(new ActionMethodInOut[]{inOut})
                .setActionFactoryBean(actionFactoryBean)
                .setActionMethodInvoke(actionMethodInvoke)
                .setActionMethodExceptionProcess(DefaultActionMethodExceptionProcess.me())
                .build();
    }

    private static DefaultFlowContext newFlowContext(CommunicationType communicationType, Request request) {
        var flowContext = new DefaultFlowContext();
        flowContext.setCommunicationType(communicationType);
        flowContext.setRequest(request);
        flowContext.setUserId(request.getUserId());
        flowContext.setCmdInfo(request.getCmdInfo());
        return flowContext;
    }

    private static UserRequestMessage newUserRequest() {
        var request = new UserRequestMessage();
        request.setCmdInfo(CmdInfo.of(1, 1));
        request.setUserId(100);
        request.setMsgId(11);
        request.setNetId(9);
        return request;
    }

    private static RequestMessage newInternalRequest() {
        var request = RequestMessage.of(CmdInfo.of(1, 1), null);
        request.setUserId(100);
        request.setNetId(9);
        return request;
    }

    private static SendMessage newSendMessage() {
        var request = SendMessage.of(CmdInfo.of(1, 1), null);
        request.setUserId(100);
        request.setNetId(9);
        return request;
    }

    private static Server newServer(BarSkeleton skeleton) {
        return Server.recordBuilder()
                .setId(1001)
                .setName("logic")
                .setTag("logic")
                .setServerType(ServerTypeEnum.LOGIC)
                .setNetId(9)
                .setIp("127.0.0.1")
                .setPubName("logic")
                .setCmdMerges(new int[]{CmdInfo.of(1, 1).cmdMerge()})
                .setPayloadMap(new HashMap<>())
                .setBarSkeleton(skeleton)
                .build();
    }

    private record PublishedByNetId(int netId, Object message) {
    }

    private static final class ThrowingInOut implements ActionMethodInOut {
        private final Throwable throwable;

        private ThrowingInOut(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public void fuckIn(FlowContext flowContext) {
            sneakyThrow(this.throwable);
        }

        @Override
        public void fuckOut(FlowContext flowContext) {
        }
    }

    private static final class NoopInOut implements ActionMethodInOut {
        @Override
        public void fuckIn(FlowContext flowContext) {
        }

        @Override
        public void fuckOut(FlowContext flowContext) {
        }
    }

    private static final class ThrowingOut implements ActionMethodInOut {
        private final Throwable throwable;

        private ThrowingOut(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public void fuckIn(FlowContext flowContext) {
        }

        @Override
        public void fuckOut(FlowContext flowContext) {
            sneakyThrow(this.throwable);
        }
    }

    private static final class ThrowingActionAfter implements ActionAfter {
        @Override
        public void execute(FlowContext flowContext) {
            throw new IllegalStateException("action after failed");
        }
    }

    private static final class RecordingActionAfter implements ActionAfter {
        final List<Boolean> errorStates = new ArrayList<>();

        @Override
        public void execute(FlowContext flowContext) {
            this.errorStates.add(flowContext.hasError());
        }
    }

    private static final class RecordingCommunicationAggregation implements CommunicationAggregation {
        final List<UserResponseMessage> userResponses = new ArrayList<>();
        final List<PublishedByNetId> publishedByNetId = new ArrayList<>();

        @Override
        public void send(SendMessage message) {
        }

        @Override
        public CompletableFuture<Response> callFuture(RequestMessage message) {
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public Response call(RequestMessage message) {
            return null;
        }

        @Override
        public void publishMessageByNetId(int netId, Object message) {
            this.publishedByNetId.add(new PublishedByNetId(netId, message));
        }

        @Override
        public void publishMessage(int serverId, Object message) {
        }

        @Override
        public void publishMessage(String name, Object message) {
        }

        @Override
        public CompletableFuture<ExternalResponse> callExternalFuture(ExternalRequestMessage message) {
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public ExternalResponse callExternal(ExternalRequestMessage message) {
            return null;
        }

        @Override
        public void broadcast(BroadcastUserMessage message) {
        }

        @Override
        public void broadcast(BroadcastUserListMessage message) {
        }

        @Override
        public void broadcast(BroadcastMulticastMessage message) {
        }

        @Override
        public CompletableFuture<ResponseCollect> callCollectFuture(RequestMessage message) {
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public ResponseCollect callCollect(RequestMessage message) {
            return null;
        }

        @Override
        public CompletableFuture<ResponseCollectExternal> callCollectExternalFuture(ExternalRequestMessage message) {
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public ResponseCollectExternal callCollectExternal(ExternalRequestMessage message) {
            return null;
        }

        @Override
        public CompletableFuture<CommonResponse> bindingLogicServerFuture(BindingLogicServerMessage message) {
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public CommonResponse bindingLogicServer(BindingLogicServerMessage message) {
            return null;
        }

        @Override
        public void writeMessage(UserResponseMessage message) {
            this.userResponses.add(message);
        }

        @Override
        public void fireRemote(EventBusMessage message) {
        }
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void sneakyThrow(Throwable throwable) throws E {
        throw (E) throwable;
    }
}
