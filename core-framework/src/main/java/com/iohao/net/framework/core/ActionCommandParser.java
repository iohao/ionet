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

import java.util.Set;

/**
 * Parse {@code @ActionController} annotated classes and build the corresponding
 * {@link ActionCommandRegions} routing table.
 *
 * @author 渔民小镇
 * @date 2025-09-09
 * @since 25.1
 */
public interface ActionCommandParser {
    /**
     * Return the action command regions built by this parser.
     *
     * @return the parsed action command regions
     */
    ActionCommandRegions getActionCommandRegions();

    /**
     * Scan the given set of controller classes and register their action commands.
     *
     * @param classSet classes annotated with {@code @ActionController}
     */
    void buildAction(Set<Class<?>> classSet);
}
