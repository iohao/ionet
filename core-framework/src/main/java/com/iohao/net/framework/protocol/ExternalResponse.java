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
 * Response returned from an external (Netty) server.
 * <p>
 * Combines {@link CommonResponse} error handling, binary payload read/write
 * capabilities, and future correlation into a single contract. Implementations
 * carry the id of the external server that produced the response.
 *
 * @author 渔民小镇
 * @date 2025-09-16
 * @since 25.1
 */
public interface ExternalResponse extends CommonResponse, ExternalPayloadGetting, ExternalPayloadSetting, FutureMessage {
    /**
     * Get the identifier of the external server that produced this response.
     *
     * @return the external server id
     */
    int getExternalServerId();
}
