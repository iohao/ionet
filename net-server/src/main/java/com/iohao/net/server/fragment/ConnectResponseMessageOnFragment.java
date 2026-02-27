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
package com.iohao.net.server.fragment;

import com.iohao.net.common.*;
import com.iohao.net.framework.communication.*;
import com.iohao.net.framework.protocol.*;
import com.iohao.net.sbe.*;
import com.iohao.net.server.*;
import com.iohao.net.server.connection.*;
import io.aeron.*;
import io.aeron.logbuffer.*;
import java.util.concurrent.*;
import lombok.*;
import lombok.experimental.*;
import lombok.extern.slf4j.*;
import org.agrona.*;

/**
 * Handles center connect-response fragments and registers newly discovered server connections.
 *
 * @author 渔民小镇
 * @date 2025-08-26
 * @since 25.1
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PROTECTED)
public class ConnectResponseMessageOnFragment implements OnFragment, NetServerSettingAware {
    final ConnectResponseMessageDecoder decoder = new ConnectResponseMessageDecoder();

    Aeron aeron;
    ConnectionManager connectionManager;
    NetServerSetting setting;
    FutureManager futureManager;

    @Override
    public void setNetServerSetting(NetServerSetting setting) {
        this.setting = setting;
        this.aeron = setting.aeron();
        this.connectionManager = setting.connectionManager();
        this.futureManager = setting.futureManager();
    }

    @Override
    public void process(DirectBuffer buffer, int offset, int actingBlockLength, int actingVersion, Header header) {
        decoder.wrap(buffer, offset, actingBlockLength, actingVersion);

        var message = new ServerMessage();
        ServerMessageCodecKit.decoder(message, decoder.common());

        var cmdMergesDecoder = decoder.cmdMerges();
        if (cmdMergesDecoder.count() > 0) {
            var bytes = new int[cmdMergesDecoder.count()];
            int index = 0;
            for (var groupElement : cmdMergesDecoder) {
                bytes[index++] = groupElement.value();
            }

            message.setCmdMerges(bytes);
        }

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

        var server = ServerManager.ofServer(message);

        var publication = ofPublication(message);
        var connectionItem = new ConnectionItem(server, publication);
        this.connectionManager.addConnection(connectionItem);

        ServerLineKit.onlineProcess(server, this.setting);

        // connected callback
        var futureId = decoder.futureId();
        if (futureId != 0) {
            CompletableFuture<Integer> future = futureManager.remove(futureId);
            if (future != null) {
                future.complete(decoder.joinServerId());
            }
        }
    }

    protected Publication ofPublication(ServerMessage message) {
        int netId = message.getNetId();
        Publication publication;
        if (this.connectionManager.containsNetId(netId)) {
            publication = this.connectionManager.getPublicationByNetId(netId);
        } else {
            publication = this.aeron.addExclusivePublication(AeronConst.ipcChannel, netId);
        }

        return publication;
    }

    @Override
    public int getTemplateId() {
        return ConnectResponseMessageDecoder.TEMPLATE_ID;
    }
}
