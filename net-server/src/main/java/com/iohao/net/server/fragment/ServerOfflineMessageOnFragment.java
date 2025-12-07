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

import com.iohao.net.common.OnFragment;
import com.iohao.net.sbe.ServerOfflineMessageDecoder;
import com.iohao.net.server.NetServerSetting;
import com.iohao.net.server.ServerManager;
import com.iohao.net.server.NetServerSettingAware;
import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;

/**
 * ServerOfflineMessageOnFragment
 *
 * @author 渔民小镇
 * @date 2025-09-26
 * @since 25.1
 */
public final class ServerOfflineMessageOnFragment implements OnFragment, NetServerSettingAware {
    final ServerOfflineMessageDecoder decoder = new ServerOfflineMessageDecoder();
    NetServerSetting setting;

    @Override
    public void setNetServerSetting(NetServerSetting setting) {
        this.setting = setting;
    }

    @Override
    public void process(DirectBuffer buffer, int offset, int actingBlockLength, int actingVersion, Header header) {
        decoder.wrap(buffer, offset, actingBlockLength, actingVersion);

        int serverId = decoder.serverId();
        var server = ServerManager.getServerById(serverId);
        ServerLineKit.offlineProcess(server, setting);
    }

    @Override
    public int getTemplateId() {
        return ServerOfflineMessageDecoder.TEMPLATE_ID;
    }
}