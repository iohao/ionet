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
 * Notification record indicating that a server has gone offline.
 * <p>
 * Published by the center server when a registered server disconnects or is detected
 * as unavailable. Consumers use the {@code serverId} to clean up routing tables and
 * release associated resources.
 *
 * @param serverId the ID of the server that went offline
 *
 * @author 渔民小镇
 * @date 2025-09-25
 * @since 25.1
 */
public record ServerOfflineMessage(int serverId) {
}
