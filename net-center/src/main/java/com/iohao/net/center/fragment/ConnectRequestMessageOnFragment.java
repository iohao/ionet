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
package com.iohao.net.center.fragment;

import com.iohao.net.center.*;
import com.iohao.net.common.*;
import com.iohao.net.common.kit.concurrent.executor.*;
import com.iohao.net.framework.protocol.*;
import com.iohao.net.sbe.*;
import io.aeron.*;
import io.aeron.logbuffer.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import lombok.*;
import lombok.experimental.*;
import lombok.extern.slf4j.*;
import org.agrona.*;

/**
 * Handles center-side connect-request fragments and synchronizes peer server discovery.
 *
 * @author 渔民小镇
 * @date 2025-08-25
 * @since 25.1
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PROTECTED)
public class ConnectRequestMessageOnFragment implements OnFragment, CenterServerSettingAware {

    final ConnectRequestMessageDecoder decoder = new ConnectRequestMessageDecoder();

    CenterConnectionManager connectionManager;
    Aeron aeron;

    @Override
    public void setCenterServerSetting(CenterServerSetting setting) {
        this.aeron = setting.aeron();
        this.connectionManager = setting.connectionManager();
    }

    @Override
    public void process(DirectBuffer buffer, int offset, int actingBlockLength, int actingVersion, Header header) {
        decoder.wrap(buffer, offset, actingBlockLength, actingVersion);

        var message = new ServerMessage();
        ServerMessageCodecKit.decoder(message, decoder.common());

        var cmdMergesDecoder = decoder.cmdMerges();
        if (cmdMergesDecoder.count() > 0) {
            var cmdMerges = new int[cmdMergesDecoder.count()];
            int index = 0;
            for (var groupElement : cmdMergesDecoder) {
                cmdMerges[index++] = groupElement.value();
            }

            message.setCmdMerges(cmdMerges);
        }

        var publication = ofPublication(message);
        var centerClientConnection = new CenterClientConnection(message, publication);
        connectionManager.addConnection(centerClientConnection);

        var payloadDecoder = decoder.payload();
        if (payloadDecoder.count() > 0) {
            for (var groupElement : payloadDecoder) {
                String key = groupElement.key();
                int valueLength = groupElement.valueLength();
                byte[] value = new byte[valueLength];
                groupElement.getValue(value, 0, valueLength);
                message.addPayload(key, value);
            }
        }

        var futureId = decoder.futureId();
        ExecutorRegionKit.getSimpleThreadExecutor(getTemplateId()).executeTry(() -> extractedByNetId(message, futureId));
    }

    protected Publication ofPublication(ServerMessage message) {
        int netId = message.getNetId();
        return connectionManager.containsNetId(netId)
                ? connectionManager.getPublicationByNetId(netId)
                : aeron.addExclusivePublication(AeronConst.ipcChannel, netId);
    }

    private void extractedByNetId(ServerMessage message, long futureId) {
        var netIdSet = new HashSet<>();
        var settingFutureId = new AtomicBoolean(true);

        connectionManager.streamServerMessage().forEach(otherServerMessage -> {
            int netId = otherServerMessage.getNetId();
            // Send the current logic service information to other logic services
            if (!netIdSet.contains(netId) && otherServerMessage.getId() != message.getId()) {
                netIdSet.add(netId);
                var serverResponseMessage = ConnectResponseMessage.of(message);
                serverResponseMessage.setJoinServerId(message.getId());

                connectionManager.publishMessage(otherServerMessage.getPubName(), serverResponseMessage);
            }

            // Fetch all logic service information for the current newly-online logic service
            var serverResponseMessage = ConnectResponseMessage.of(otherServerMessage);
            serverResponseMessage.setJoinServerId(message.getId());

            if (settingFutureId.get()) {
                settingFutureId.set(false);
                serverResponseMessage.setFutureId(futureId);
            }

            connectionManager.publishMessage(message.getPubName(), serverResponseMessage);
        });
    }

    @Override
    public int getTemplateId() {
        return ConnectRequestMessageDecoder.TEMPLATE_ID;
    }
}
