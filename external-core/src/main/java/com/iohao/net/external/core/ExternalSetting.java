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
package com.iohao.net.external.core;

import com.iohao.net.framework.protocol.Server;
import com.iohao.net.common.kit.attr.AttrOption;
import com.iohao.net.common.kit.attr.AttrOptionDynamic;
import com.iohao.net.common.kit.attr.AttrOptions;
import com.iohao.net.external.core.hook.internal.IdleProcessSetting;
import com.iohao.net.external.core.session.UserSessions;
import com.iohao.net.server.ConvenientCommunication;
import com.iohao.net.server.NetServerSetting;
import com.iohao.net.server.NetServerSettingAware;
import lombok.Builder;

/**
 * Immutable runtime settings for one external server instance.
 *
 * @param port port exposed to real players
 * @param server server metadata descriptor
 * @param userSessions user session manager
 * @param idleProcessSetting heartbeat/idle processing settings
 * @param options extensible attribute options
 * @author 渔民小镇
 * @date 2025-10-15
 * @since 25.1
 */
@Builder(setterPrefix = "set", builderClassName = "RecordBuilder")
public record ExternalSetting(
        int port
        , Server server
        , UserSessions<?, ?> userSessions
        , IdleProcessSetting idleProcessSetting
        , AttrOptions options
) implements AttrOptionDynamic {

    /** Attribute key storing the resolved {@link NetServerSetting}. */
    public static final AttrOption<NetServerSetting> netServerSetting = AttrOption.valueOf("NetServerSetting");

    /**
     * Get the net server setting attached to this external setting.
     *
     * @return net server setting, or {@code null} if not attached
     */
    public NetServerSetting netServerSetting() {
        return this.option(netServerSetting);
    }

    /**
     * Get the convenient communication facade from the attached net server setting.
     *
     * @return convenient communication facade
     */
    public ConvenientCommunication convenientCommunication() {
        return netServerSetting().convenientCommunication();
    }

    @Override
    public AttrOptions getOptions() {
        return options;
    }

    /**
     * Inject this setting into supported aware components.
     *
     * @param o target component
     */
    public void inject(Object o) {
        if (o == null) {
            return;
        }

        if (o instanceof ExternalSettingAware aware) {
            aware.setExternalSetting(this);
        }

        if (o instanceof NetServerSettingAware aware) {
            aware.setNetServerSetting(netServerSetting());
        }
    }
}
