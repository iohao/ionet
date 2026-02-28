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

import com.iohao.net.external.core.session.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import org.agrona.collections.*;

/**
 * Registry of external server settings and their user session containers.
 *
 * @author 渔民小镇
 * @date 2025-09-11
 * @since 25.1
 */
public final class ExternalManager {
    static final Int2ObjectHashMap<ExternalSetting> map = new Int2ObjectHashMap<>();
    static final List<UserSessions<?, ?>> list = new CopyOnWriteArrayList<>();

    /**
     * Register an external server setting and expose its session container.
     *
     * @param setting external server setting
     */
    public static void addExternalSetting(ExternalSetting setting) {
        int externalServerId = setting.server().id();
        map.put(externalServerId, setting);
        list.add(setting.userSessions());
    }

    /**
     * List all registered user session containers.
     *
     * @return registered user session containers
     */
    public static List<UserSessions<?, ?>> listUserSessions() {
        return list;
    }

    /**
     * Get the user session container for an external server id.
     *
     * @param externalServerId external server id
     * @return user sessions, or {@code null} if not registered
     */
    public static UserSessions<?, ?> getUserSessions(int externalServerId) {
        var setting = map.get(externalServerId);
        if (setting == null) {
            return null;
        }

        return setting.userSessions();
    }
}
