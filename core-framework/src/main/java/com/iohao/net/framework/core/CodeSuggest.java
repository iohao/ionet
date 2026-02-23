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
package com.iohao.net.framework.core;

/**
 * Strategy interface for inspecting action commands and emitting code-improvement suggestions.
 *
 * @author 渔民小镇
 * @date 2025-10-13
 * @since 25.1
 */
public interface CodeSuggest {
    /**
     * Inspect the given suggestion context and emit any applicable recommendations.
     *
     * @param suggest the suggestion context containing the action command to inspect
     */
    void inspect(SuggestInformation suggest);
}
