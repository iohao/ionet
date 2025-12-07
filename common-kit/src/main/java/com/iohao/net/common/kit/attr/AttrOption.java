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
package com.iohao.net.common.kit.attr;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Attribute option for dynamic attributes
 *
 * @param <T> The type of the attribute value
 * @author 渔民小镇
 * @date 2022-01-31
 */
public record AttrOption<T>(String name, T defaultValue, Supplier<T> supplier) implements Serializable {
    public AttrOption {
        Objects.requireNonNull(name);
    }

    /**
     * Initializes an AttrOption
     *
     * @param name name
     * @param <T>  The type of the attribute value
     * @return AttrOption
     */
    public static <T> AttrOption<T> valueOf(String name) {
        return new AttrOption<>(name, null, null);
    }

    /**
     * Initializes an AttrOption
     *
     * @param name         name
     * @param defaultValue Default value (singleton)
     * @param <T>          The type of the attribute value
     * @return AttrOption
     */
    public static <T> AttrOption<T> valueOf(String name, T defaultValue) {
        return new AttrOption<>(name, defaultValue, null);
    }

    /**
     * Initializes an AttrOption
     *
     * @param name     name
     * @param supplier Supplier to get the value from if the value does not exist
     * @param <T>      The type of the attribute value
     * @return AttrOption
     */
    public static <T> AttrOption<T> valueOf(String name, Supplier<T> supplier) {
        return new AttrOption<>(name, null, supplier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AttrOption<?> that = (AttrOption<?>) o;

        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
