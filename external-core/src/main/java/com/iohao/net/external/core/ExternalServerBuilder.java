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

import com.iohao.net.framework.core.codec.DataCodecManager;
import com.iohao.net.framework.core.codec.ProtoDataCodec;
import com.iohao.net.framework.i18n.Bundle;
import com.iohao.net.framework.i18n.MessageKey;
import com.iohao.net.framework.protocol.Server;
import com.iohao.net.framework.protocol.ServerBuilder;
import com.iohao.net.framework.protocol.ServerTypeEnum;
import com.iohao.net.common.kit.ProtoKit;
import com.iohao.net.common.kit.RandomKit;
import com.iohao.net.common.kit.attr.AttrOptions;
import com.iohao.net.external.core.config.ExternalGlobalConfig;
import com.iohao.net.external.core.config.ExternalJoinEnum;
import com.iohao.net.external.core.hook.UserHook;
import com.iohao.net.external.core.hook.internal.DefaultUserHook;
import com.iohao.net.external.core.hook.internal.IdleProcessSettingBuilder;
import com.iohao.net.external.core.message.ExternalMessage;
import com.iohao.net.external.core.micro.MicroBootstrap;
import com.iohao.net.external.core.micro.MicroBootstrapFlow;
import com.iohao.net.external.core.micro.ExternalJoinSelector;
import com.iohao.net.external.core.micro.ExternalJoinSelectors;
import com.iohao.net.external.core.session.UserSessions;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * ExternalServerBuilder
 *
 * @author 渔民小镇
 * @date 2023-02-19
 * @since 25.1
 */
@Getter
@Setter
public final class ExternalServerBuilder implements ExternalServerBuilderSetting {
    final AttrOptions options = new AttrOptions();
    /** externalServerId */
    int id;
    int port;
    /** Connection method: defaults to Websocket */
    ExternalJoinEnum joinEnum;
    /** The startup process for the server that connects with real players */
    MicroBootstrapFlow<?> microBootstrapFlow;
    /** Heartbeat-related settings */
    IdleProcessSettingBuilder idleProcessSettingBuilder;
    /** User (player) session manager */
    UserSessions<?, ?> userSessions;
    /** UserHook hook interface, triggered on login and logout */
    UserHook userHook;
    MicroBootstrap microBootstrap;
    ExternalServerCreator externalServerCreator;

    public ExternalServer build() {
        this.defaultSetting();
        // Generate a MicroBootstrapFlow, MicroBootstrap, UserSessions, PipelineCustom according to ExternalJoinEnum
        ExternalJoinSelectors.getExternalJoinSelector(joinEnum).defaultSetting(this);

        // ------------ userSessions ------------
        userSessions.setUserHook(userHook);
        userSessions.setJoinEnum(joinEnum);

        // ------------ IdleProcessSetting ------------
        var idleProcessSetting = idleProcessSettingBuilder == null ? null : idleProcessSettingBuilder.ofIdleProcessSetting();

        // ------------ ExternalServer ------------
        Server server = new ServerBuilder()
                .setId(this.id)
                .setName(Bundle.getMessage(MessageKey.externalServer))
                .setTag("ExternalServer")
                .setServerType(ServerTypeEnum.EXTERNAL)
                .build();

        var setting = ExternalSetting.builder()
                .setPort(port)
                .setServer(server)
                .setUserSessions(userSessions)
                .setIdleProcessSetting(idleProcessSetting)
                .setOptions(new AttrOptions(this.options))
                .build();

        // ------------ inject ------------
        Set<Object> injectSet = new HashSet<>();
        injectSet.add(userHook);
        injectSet.add(microBootstrap);
        injectSet.add(microBootstrapFlow);
        if (idleProcessSetting != null) {
            injectSet.add(idleProcessSetting.idleHook());
        }

        return externalServerCreator.of(ExternalServerCreatorParameter.builder()
                .setSetting(setting)
                .setMicroBootstrap(microBootstrap)
                .setMicroBootstrapFlow(microBootstrapFlow)
                .setInjectSet(injectSet)
                .build()
        );
    }

    private void defaultSetting() {
        Objects.requireNonNull(externalServerCreator);

        if (id == 0) {
            id = RandomKit.randomInt(1_000_000, 2_000_000);
        }

        if (port == 0) {
            port = ExternalGlobalConfig.externalPort;
        }

        if (joinEnum == null) {
            joinEnum = ExternalJoinEnum.WEBSOCKET;
        }

        if (userHook == null) {
            userHook = new DefaultUserHook();
        }

        // ------------ Others ------------
        if (DataCodecManager.getDataCodec() instanceof ProtoDataCodec) {
            ProtoKit.create(ExternalMessage.class);
        }
    }

    static {
        ServiceLoader.load(ExternalJoinSelector.class).forEach(ExternalJoinSelectors::putIfAbsent);
    }
}
