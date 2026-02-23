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
package com.iohao.net.external.core.net.external;

/**
 * Handler for template-based internal external operations.
 *
 * @author 渔民小镇
 * @date 2025-09-10
 * @since 25.1
 */
public interface OnExternal {
    /**
     * Process the template payload and write the result into the context response.
     *
     * @param payload serialized payload
     * @param payloadLength valid payload length
     * @param context execution context
     */
    void process(byte[] payload, int payloadLength, OnExternalContext context);

    /**
     * Get the template id handled by this processor.
     *
     * @return template id
     */
    int getTemplateId();
}
