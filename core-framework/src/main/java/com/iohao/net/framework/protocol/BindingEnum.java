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

import lombok.Getter;

/**
 * Dynamic Binding Enum for Logic Server
 *
 * @author 渔民小镇
 * @date 2023-06-07
 */
public enum BindingEnum {
    /**
     * Override the bound logic servers
     * <p>
     * Example:
     * <pre>
     * Before overriding, if the player was already bound to logic server IDs [1-1];
     *
     * Now the player adds logic server IDs [2-2, 3-1];
     *
     * The player's bound logic server data at this time will be [2-2, 3-1], a total of 2 pieces of data, the new overrides the old;
     * The currently set values will be used regardless of any previously bound data;
     * </pre>
     */
    COVER(0),
    /**
     * Append to the bound logic servers
     * <p>
     * Example:
     * <pre>
     * Before appending, if the player was already bound to logic server IDs [1-1];
     *
     * Now the player adds logic server IDs [2-2, 3-1];
     *
     * The player's bound logic server data at this time will be [1-1, 2-2, 3-1], a total of 3 pieces of data
     * </pre>
     */
    APPEND(1),

    /**
     * Remove the bound logic servers
     * <p>
     * Example:
     * <pre>
     * Before removal, if the player was already bound to logic server IDs [1-1, 2-2, 3-1];
     *
     * Now the player adds [2-2, 3-1] as the logic server IDs to be removed;
     *
     * The player's bound logic server data at this time will be [1-1], a total of 1 piece of data, with 2 pieces of data removed;
     * </pre>
     */
    REMOVE(2),
    /** Clear all bound logic servers */
    CLEAR(3);

    @Getter
    final int value;

    BindingEnum(int value) {
        this.value = value;
    }

    public static BindingEnum ofValue(int value) {
        return switch (value) {
            case 0 -> COVER;
            case 1 -> APPEND;
            case 2 -> REMOVE;
            default -> CLEAR;
        };
    }
}
