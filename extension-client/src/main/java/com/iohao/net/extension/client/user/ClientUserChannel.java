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
package com.iohao.net.extension.client.user;

import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.core.kit.CmdKit;
import com.iohao.net.framework.core.codec.DataCodecManager;
import com.iohao.net.common.kit.CollKit;
import com.iohao.net.extension.client.command.*;
import com.iohao.net.extension.client.kit.ClientMessageKit;
import com.iohao.net.extension.client.kit.ClientUserConfigs;
import com.iohao.net.external.core.message.ExternalMessage;
import com.iohao.net.framework.protocol.CmdCodeConst;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用户通信 channel
 * <pre>
 *     发送请求，接收服务器响应
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-07-13
 */
@Slf4j
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientUserChannel {
    final AtomicInteger msgIdSeq = new AtomicInteger(1);

    final AtomicBoolean starting = new AtomicBoolean();
    /**
     * 请求回调
     * <pre>
     *     key : msgId
     * </pre>
     */
    final Map<Integer, RequestCommand> callbackMap = CollKit.ofConcurrentHashMap();
    /**
     * 广播监听
     * <pre>
     *     key : cmdMerge
     * </pre>
     */
    final Map<Integer, ListenCommand> listenMap = CollKit.ofConcurrentHashMap();

    ClientChannelRead channelRead = new DefaultChannelRead();

    final DefaultClientUser clientUser;

    public Runnable closeChannel;
    ChannelAccept channelAccept;
    /** 目标 ip （服务器 ip） */
    public InetSocketAddress inetSocketAddress;

    public ClientUserChannel(DefaultClientUser clientUser) {
        this.clientUser = clientUser;
    }

    public void request(InputCommand inputCommand) {
        CmdInfo cmdInfo = inputCommand.getCmdInfo();
        // 生成请求参数
        RequestDataDelegate requestData = inputCommand.getRequestData();

        CallbackDelegate callback = inputCommand.getCallback();

        RequestCommand requestCommand = new RequestCommand()
                .setCmdMerge(cmdInfo.cmdMerge())
                .setTitle(inputCommand.getTitle())
                .setRequestData(requestData)
                .setCallback(callback);

        this.execute(requestCommand);
    }

    public void execute(RequestCommand requestCommand) {
        int msgId = this.msgIdSeq.incrementAndGet();
        this.callbackMap.put(msgId, requestCommand);
        CmdInfo cmdInfo = CmdInfo.of(requestCommand.getCmdMerge());

        ExternalMessage message = ClientMessageKit.ofCommunicationMessage(cmdInfo);
        message.setMsgId(msgId);

        RequestDataDelegate requestData = requestCommand.getRequestData();
        Object data = "";
        if (Objects.nonNull(requestData)) {
            data = requestData.createRequestData();
            data = ParseClientRequestDataKit.parse(data);

            byte[] encode = DataCodecManager.encode(data);
            message.setData(encode);
        }

        if (ClientUserConfigs.openLogRequestCommand) {
            long userId = clientUser.getUserId();
            log.info("User[{}] Request【{}】- [msgId:{}] {} {}"
                    , userId
                    , requestCommand.getTitle()
                    , msgId
                    , CmdKit.toString(cmdInfo.cmdMerge())
                    , data
            );
        }

        this.writeAndFlush(message);
    }

    public void readMessage(ExternalMessage message) {
        channelRead.read(message);
    }

    public void writeAndFlush(ExternalMessage message) {
        if (Objects.isNull(this.channelAccept)) {
            return;
        }

        InetSocketAddress inetSocketAddress = this.inetSocketAddress;
        if (Objects.nonNull(inetSocketAddress)) {
            message.setInetSocketAddress(inetSocketAddress);
        }

        this.channelAccept.accept(message);
    }

    public void addListen(ListenCommand listenCommand) {
        int cmdMerge = listenCommand.getCmdInfo().cmdMerge();
        this.listenMap.put(cmdMerge, listenCommand);
    }

    public void closeChannel() {
        this.clientUser.setActive(false);
        Optional.ofNullable(closeChannel).ifPresent(Runnable::run);
    }

    class DefaultChannelRead implements ClientChannelRead {
        @Override
        public void read(ExternalMessage message) {
            int responseStatus = message.getErrorCode();

            // 表示有异常消息，统一异常处理
            if (responseStatus != 0) {
                log.error("[ErrorCode:{}] - [ErrorMsg:{}] - {}",
                        responseStatus, message.getErrorMessage(), CmdInfo.of(message.getCmdMerge()));
                return;
            }

            if (message.getCmdCode() == CmdCodeConst.IDLE) {
                printLog(message);
                return;
            }

            CommandResult commandResult = new CommandResult(message);
            // 有回调的，交给回调处理
            int msgId = message.getMsgId();
            RequestCommand requestCommand = callbackMap.remove(msgId);

            if (Objects.nonNull(requestCommand)) {
                printLog(message, requestCommand);

                Optional.ofNullable(requestCommand.getCallback()).ifPresent(callback -> callback.callback(commandResult));

                return;
            }

            // 广播监听
            int cmdMerge = message.getCmdMerge();
            ListenCommand listenCommand = listenMap.get(cmdMerge);

            if (Objects.nonNull(listenCommand)) {
                printLog(listenCommand, cmdMerge);
                listenCommand.getCallback().callback(commandResult);
            }
        }

        private void printLog(ExternalMessage message) {
            if (ClientUserConfigs.openLogIdle) {
                log.info("Receive Idle: {}", message);
            }
        }

        private void printLog(ListenCommand listenCommand, int cmdMerge) {
            if (ClientUserConfigs.openLogListenBroadcast) {
                log.info("Listen Callback [{}] - {}"
                        , listenCommand.getTitle()
                        , CmdKit.toString(cmdMerge)
                );
            }
        }

        private void printLog(ExternalMessage message, RequestCommand requestCommand) {
            if (ClientUserConfigs.openLogRequestCallback) {
                // 用户接收服务器的响应数据
                long userId = clientUser.getUserId();
                int cmdMerge = message.getCmdMerge();

                log.info("User[{}] Receive【{}】- [msgId:{}] {}"
                        , userId
                        , requestCommand.getTitle()
                        , message.getMsgId()
                        , CmdKit.toString(cmdMerge)
                );

                CallbackDelegate callback = requestCommand.getCallback();
                if (Objects.isNull(callback)) {
                    log.warn("callback is null");
                }
            }
        }
    }
}
