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

import com.iohao.net.common.kit.*;
import com.iohao.net.external.core.*;
import com.iohao.net.external.core.hook.*;
import com.iohao.net.external.core.session.*;
import com.iohao.net.framework.i18n.*;
import lombok.extern.slf4j.*;

/**
 * Default logging implementation of {@link UserHook}.
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

    /**
     * Build the localized log message and include current online user count.
     *
     * @param userSession user session
     * @param key i18n message key
     * @return formatted log content
     */
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
