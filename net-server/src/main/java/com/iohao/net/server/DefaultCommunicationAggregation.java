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
package com.iohao.net.server;

import com.iohao.net.framework.CoreGlobalConfig;
import com.iohao.net.framework.communication.CommunicationAggregation;
import com.iohao.net.framework.communication.FutureManager;
import com.iohao.net.framework.core.exception.ActionErrorEnum;
import com.iohao.net.framework.core.exception.ErrorInformation;
import com.iohao.net.framework.communication.eventbus.EventBusMessage;
import com.iohao.net.common.Publisher;
import com.iohao.net.common.kit.ArrayKit;
import com.iohao.net.common.kit.concurrent.TaskKit;
import com.iohao.net.common.kit.exception.EnterpriseSupportException;
import com.iohao.net.framework.protocol.*;
import com.iohao.net.server.balanced.ExternalServerLoadBalanced;
import com.iohao.net.server.connection.ConnectionManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.iohao.net.server.CommunicationAggregationErrorConst.*;

/**
 * DefaultCommunicationAggregation
 *
 * @author 渔民小镇
 * @date 2025-09-07
 * @since 25.1
 */
@Slf4j
@SuppressWarnings("all")
@FieldDefaults(level = AccessLevel.PROTECTED)
public class DefaultCommunicationAggregation implements CommunicationAggregation, NetServerSettingAware {
    static final int futureTimeoutMillis = CoreGlobalConfig.getFutureTimeoutMillis();

    ConnectionManager connectionManager;
    ExternalServerLoadBalanced externalServerLoadBalanced;
    FutureManager futureManager;
    Publisher publisher;
    FindServer findServer;

    @Override
    public void setNetServerSetting(NetServerSetting setting) {
        this.findServer = setting.findServer();
        this.publisher = setting.publisher();
        this.connectionManager = setting.connectionManager();
        this.futureManager = setting.futureManager();

        var balancedManager = setting.balancedManager();
        this.externalServerLoadBalanced = balancedManager.getExternalBalanced();
    }

    @Override
    public void publishMessageByNetId(int netId, Object message) {
        connectionManager.publishMessageByNetId(netId, message);
    }

    @Override
    public void publishMessage(int serverId, Object message) {
        connectionManager.publishMessage(serverId, message);
    }

    @Override
    public void publishMessage(String name, Object message) {
        connectionManager.publishMessage(name, message);
    }

    @Override
    public void writeMessage(UserResponseMessage message) {
        int serverId = message.getExternalServerId();
        connectionManager.publishMessage(serverId, message);
    }

    @Override
    public void broadcast(BroadcastUserMessage message) {
        int serverId = message.getExternalServerId();
        if (serverId != 0) {
            connectionManager.publishMessage(serverId, message);
            return;
        }

        externalServerLoadBalanced.listServer().forEach(server -> {
            connectionManager.publishMessage(server.id(), message);
        });
    }

    @Override
    public void broadcast(BroadcastUserListMessage message) {
        if (ArrayKit.isEmpty(message.getUserIds())) {
            return;
        }

        externalServerLoadBalanced.listServer().forEach(server -> {
            connectionManager.publishMessage(server.id(), message);
        });
    }

    @Override
    public void broadcast(BroadcastMulticastMessage message) {
        externalServerLoadBalanced.listServer().forEach(server -> {
            connectionManager.publishMessage(server.id(), message);
        });
    }

    @Override
    public ExternalResponse callExternal(ExternalRequestMessage message) {
        try {
            return callExternalFuture(message).get(futureTimeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(e.getMessage(), e);
            return externalResponseDataNotExist;
        }
    }

    @Override
    public CompletableFuture<ExternalResponse> callExternalFuture(ExternalRequestMessage message) {
        var futureId = this.futureManager.nextFutureId();
        message.setFutureId(futureId);

        CompletableFuture<ExternalResponse> future = this.futureManager.ofFuture(futureId);

        int externalServerId = message.getExternalServerId();
        var server = externalServerLoadBalanced.getServer(externalServerId);
        if (server != null) {
            publisher.publishMessage(server.pubName(), message);
            return future;
        }

        future.complete(externalResponseDataNotExist);
        return future;
    }

    @Override
    public CompletableFuture<ResponseCollectExternal> callCollectExternalFuture(ExternalRequestMessage message) {

        var server = externalServerLoadBalanced.getAnyServer();
        if (server == null) {
            return CompletableFuture.completedFuture(responseCollectExternalEmpty);
        }

        message.setExternalServerId(server.id());

        return CompletableFuture.supplyAsync(() -> {
            var response = new ResponseCollectExternalMessage();
            response.setResponseList(List.of(callExternal(message)));
            return response;
        }, TaskKit.getNetVirtualExecutor());
    }

    @Override
    public ResponseCollectExternal callCollectExternal(ExternalRequestMessage message) {
        try {
            return callCollectExternalFuture(message).get(futureTimeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(e.getMessage(), e);
            return responseCollectExternalEmpty;
        }
    }

    @Override
    public CompletableFuture<CommonResponse> bindingLogicServerFuture(BindingLogicServerMessage message) {
        return CompletableFuture.completedFuture(bindingLogicServerEnterprise);
    }

    @Override
    public CommonResponse bindingLogicServer(BindingLogicServerMessage message) {
        try {
            return bindingLogicServerFuture(message).get(futureTimeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(e.getMessage(), e);
            return bindingLogicServerError;
        }
    }

    @Override
    public CompletableFuture<Response> callFuture(RequestMessage message) {
        var server = this.findServer.getServer(message);
        if (server == null) {
            return ofErrorFuture(message.getOutputError());
        }

        if (message.getHopCount() == 0) {
            message.setHopCount(2);
        }

        var futureId = this.futureManager.nextFutureId();
        message.setFutureId(futureId);

        if (message.getSourceServerId() == 0) {
            message.setSourceServerId(server.id());
        }

        publishMessage(server.pubName(), message);
        return this.futureManager.ofFuture(futureId);
    }


    @Override
    public Response call(RequestMessage message) {
        try {
            var future = callFuture(message);
            return future.get(futureTimeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.warn("call timeout: {}", e.getMessage());

            var response = new ResponseMessage();
            response.setError(ActionErrorEnum.internalCommunicationError);
            this.futureManager.remove(message.getFutureId());
            return response;
        }
    }

    @Override
    public void send(SendMessage message) {
        var server = findServer.getServer(message);
        if (server == null) {
            return;
        }

        message.setLogicServerId(server.id());

        if (message.getHopCount() == 0) {
            message.setHopCount(2);
        }

        publisher.publishMessage(server.pubName(), message);
    }

    @Override
    public CompletableFuture<ResponseCollect> callCollectFuture(RequestMessage message) {
        var response = new ResponseCollectMessage();
        response.setError(ActionErrorEnum.enterpriseFunction);
        return CompletableFuture.completedFuture(response);
    }

    @Override
    public ResponseCollect callCollect(RequestMessage message) {
        try {
            return callCollectFuture(message).get(futureTimeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(e.getMessage(), e);
            var response = new ResponseCollectMessage();
            response.setError(ActionErrorEnum.internalCommunicationError);
            return response;
        }
    }

    protected CompletableFuture<Response> ofErrorFuture(ErrorInformation errorInformation) {
        var message = new UserResponseMessage();
        message.setError(errorInformation);
        return CompletableFuture.completedFuture(message);
    }

    @Override
    public void fireRemote(EventBusMessage message) {
        throw new EnterpriseSupportException();
    }
}
