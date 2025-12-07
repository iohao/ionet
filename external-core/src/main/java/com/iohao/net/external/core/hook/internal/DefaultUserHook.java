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
package com.iohao.net.external.core.hook.internal;

import com.iohao.net.framework.i18n.Bundle;
import com.iohao.net.framework.i18n.MessageKey;
import com.iohao.net.common.kit.IonetLogName;
import com.iohao.net.external.core.ExternalSetting;
import com.iohao.net.external.core.ExternalSettingAware;
import com.iohao.net.external.core.hook.UserHook;
import com.iohao.net.external.core.session.UserSession;
import com.iohao.net.external.core.session.UserSessions;
import lombok.extern.slf4j.Slf4j;

/**
 * DefaultUserHook
 *
 * @author 渔民小镇
 * @date 2023-02-20
 */
@Slf4j(topic = IonetLogName.CommonStdout)
public class DefaultUserHook implements UserHook, ExternalSettingAware {
    UserSessions<?, ?> userSessions;

    @Override
    public void setExternalSetting(ExternalSetting setting) {
        userSessions = setting.userSessions();
    }

    @Override
    public void into(UserSession userSession) {
        log.info(getString(userSession, MessageKey.userHookInto));
    }

    @Override
    public void quit(UserSession userSession) {
        log.info(getString(userSession, MessageKey.userHookQuit));
    }

    private String getString(UserSession userSession, String key) {
        return """
                %s:%s
                userId:%s
                userChannelId:%s
                """.formatted(
                Bundle.getMessage(key), userSessions.countOnline(),
                userSession.getUserId(),
                userSession.getUserChannelId());
    }
}
