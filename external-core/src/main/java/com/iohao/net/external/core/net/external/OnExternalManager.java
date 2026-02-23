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
 * Registry of {@link OnExternal} handlers indexed by template id sign and value.
 *
 * @author 渔民小镇
 * @date 2025-09-10
 * @since 25.1
 */
public final class OnExternalManager {
    static final OnExternal[] onExternals = new OnExternal[16];
    static final OnExternal[] internalOnExternals = new OnExternal[8];

    /**
     * Select the handler table based on whether the template id is public or internal.
     *
     * @param templateId template id
     * @return handler array to read from
     */
    public static OnExternal[] getOnExternals(int templateId) {
        return templateId >= 0 ? OnExternalManager.onExternals : OnExternalManager.internalOnExternals;
    }

    /**
     * Register a handler into the appropriate template-id table.
     *
     * @param onExternal handler implementation
     */
    public static void register(OnExternal onExternal) {
        int templateId = onExternal.getTemplateId();
        if (templateId >= 0) {
            onExternals[templateId] = onExternal;
        } else {
            internalOnExternals[Math.abs(templateId)] = onExternal;
        }
    }
}
