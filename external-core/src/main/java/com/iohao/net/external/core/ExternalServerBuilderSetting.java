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

import com.iohao.net.common.kit.attr.AttrOptionDynamic;
import com.iohao.net.external.core.hook.internal.IdleProcessSettingBuilder;
import com.iohao.net.external.core.micro.MicroBootstrap;
import com.iohao.net.external.core.micro.MicroBootstrapFlow;
import com.iohao.net.external.core.session.UserSessions;

/**
 * Mutable state used while building an external server.
 *
 * @author 渔民小镇
 * @date 2023-05-05
 */
public interface ExternalServerBuilderSetting extends AttrOptionDynamic {
    /**
     * Get the micro bootstrap flow customization chain.
     *
     * @return micro bootstrap flow
     */
    MicroBootstrapFlow<?> getMicroBootstrapFlow();

    /**
     * Set the micro bootstrap flow customization chain.
     *
     * @param microBootstrapFlow micro bootstrap flow
     */
    void setMicroBootstrapFlow(MicroBootstrapFlow<?> microBootstrapFlow);

    /**
     * Get the user session manager.
     *
     * @return user session manager
     */
    UserSessions<?, ?> getUserSessions();

    /**
     * Set the user session manager.
     *
     * @param userSessions user session manager
     */
    void setUserSessions(UserSessions<?, ?> userSessions);

    /**
     * Get the micro bootstrap instance.
     *
     * @return micro bootstrap
     */
    MicroBootstrap getMicroBootstrap();

    /**
     * Set the micro bootstrap instance.
     *
     * @param microBootstrap micro bootstrap
     */
    void setMicroBootstrap(MicroBootstrap microBootstrap);

    /**
     * Get the idle/heartbeat setting builder.
     *
     * @return idle process setting builder
     */
    IdleProcessSettingBuilder getIdleProcessSettingBuilder();
}
