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
package com.iohao.net.extension.client.join;

import com.iohao.net.framework.i18n.Bundle;
import com.iohao.net.framework.i18n.MessageKey;
import com.iohao.net.common.kit.IonetLogName;
import com.iohao.net.common.kit.PresentKit;
import com.iohao.net.common.kit.concurrent.IntervalTaskListener;
import com.iohao.net.common.kit.concurrent.TaskKit;
import com.iohao.net.extension.client.ClientConnectOption;
import com.iohao.net.extension.client.InputCommandRegion;
import com.iohao.net.extension.client.kit.ClientMessageKit;
import com.iohao.net.extension.client.user.ClientUser;
import com.iohao.net.extension.client.user.ClientUserChannel;
import com.iohao.net.extension.client.user.PressureKit;
import com.iohao.net.extension.client.user.DefaultClientUser;
import com.iohao.net.external.core.config.ExternalGlobalConfig;
import com.iohao.net.external.core.config.ExternalJoinEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Convenience bootstrap for starting a single simulated client instance.
 *
 * @author 渔民小镇
 * @date 2023-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j(topic = IonetLogName.CommonStdout)
public final class ClientRunOne {
    List<InputCommandRegion> inputCommandRegions;
    ClientUser clientUser;

    int connectPort = ExternalGlobalConfig.externalPort;
    String connectAddress = "127.0.0.1";
    /** websocketPath , default : /websocket */
    String websocketPath = ExternalGlobalConfig.websocketPath;
    String websocketVerify = "";

    ExternalJoinEnum joinEnum = ExternalJoinEnum.WEBSOCKET;
    ClientConnectOption option;

    public void startup() {
        if (Objects.isNull(this.clientUser)) {
            this.clientUser = new DefaultClientUser();
        }

        var inputCommandRegionsName = Bundle.getMessage(MessageKey.clientInputCommandRegions);
        Objects.requireNonNull(this.inputCommandRegions, inputCommandRegionsName);

        PressureKit.addClientUser(clientUser);
        clientUser.setInputCommandRegions(this.inputCommandRegions);

        this.inputCommandRegions.forEach(inputCommandRegion -> {
            inputCommandRegion.setClientUser(clientUser);
            inputCommandRegion.initInputCommand();
        });

        ClientConnectOption option = getOption();

        ClientConnect clientConnect = ClientConnects.getClientConnect(joinEnum);
        if (Objects.isNull(clientConnect)) {
            log.error("{} has no corresponding implementation class", joinEnum);
            return;
        }

        option.setConnectedCallback(() -> this.inputCommandRegions.forEach(InputCommandRegion::connectionComplete));

        TaskKit.execute(() -> clientConnect.connect(option));

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Starts a timer that sends periodic idle/heartbeat messages.
     *
     * @param idlePeriod heartbeat period in seconds
     * @return this
     */
    public ClientRunOne idle(int idlePeriod) {
        TaskKit.runInterval(new IntervalTaskListener() {
            @Override
            public void onUpdate() {
                var message = ClientMessageKit.ofIdleMessage();

                ClientUserChannel clientUserChannel = clientUser.getClientUserChannel();
                clientUserChannel.writeAndFlush(message);
            }

            @Override
            public boolean isActive() {
                return clientUser.isActive();
            }
        }, idlePeriod, TimeUnit.SECONDS);

        return this;
    }

    private ClientConnectOption getOption() {

        if (Objects.isNull(option)) {
            option = new ClientConnectOption();
        }

        PresentKit.ifNull(option.getWsUrl(), () -> {
            String wsUrl = String.format("ws://%s:%d%s%s", connectAddress, connectPort, websocketPath, websocketVerify);
            option.setWsUrl(wsUrl);
        });

        PresentKit.ifNull(option.getSocketAddress(), () -> {
            InetSocketAddress socketAddress = new InetSocketAddress(connectAddress, connectPort);
            option.setSocketAddress(socketAddress);
        });

        PresentKit.ifNull(option.getClientUser(), () -> option.setClientUser(clientUser));

        return option;
    }
}
