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
package com.iohao.net.framework.protocol;

/**
 * Reserved template id constants used by the external server for internal operations.
 * <p>
 * Negative values are reserved so they do not collide with user-defined template ids.
 * Each constant identifies a specific built-in operation type.
 *
 * @author 渔民小镇
 * @date 2025-09-11
 * @since 25.1
 */
public interface OnExternalTemplateId {
    /** Template id for checking whether a user already exists on the external server. */
    int existUser = -1;
    /** Template id for forcibly disconnecting (kicking) a user. */
    int forcedOffline = -2;
    /** Template id for updating user session attachment data. */
    int attachmentUpdate = -3;
    /** Template id for setting (binding) a user id to a session. */
    int settingUserId = -4;
}
