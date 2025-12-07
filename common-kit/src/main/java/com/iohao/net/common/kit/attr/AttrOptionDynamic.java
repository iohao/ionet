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

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/// AttrOptionDynamic
/// ```java
/// AttrOptionDynamic options = ...;
/// AttrOption<Long> timeKey = AttrOption.valueOf("myLongValue");
///// set long value
/// this.option(timeKey, 123L);
///// get long value
/// long val = this.option(timeKey);
///
/// AttrOption<Integer> intKey = AttrOption.valueOf("myIntegerValue");
///// set int value
/// this.option(intKey, 123);
///// get int value
/// int age = this.option(intKey);
///```
///
/// @author 渔民小镇
/// @date 2022-01-31
public interface AttrOptionDynamic {
    /**
     * Gets the dynamic member attributes (options)
     *
     * @return The dynamic member attributes
     */
    AttrOptions getOptions();

    /**
     * Gets the option value. Returns the default value if the option does not exist.
     *
     * @param option The attribute option
     * @return The option value, or the default option value if the option does not exist.
     */
    default <T> T option(AttrOption<T> option) {
        return this.getOptions().option(option);
    }

    /**
     * Gets the option value. Returns the specified value if the option does not exist or is null.
     *
     * @param option The attribute option
     * @param value  The specified value
     * @return The option value, or the specified value if the option is null or does not exist.
     */
    default <T> T optionValue(AttrOption<T> option, T value) {
        T data = this.option(option);

        if (Objects.isNull(data)) {
            return value;
        }

        return data;
    }

    /**
     * Sets a new option with a specific value.
     * <p>
     * Use a null value to remove the previously set {@link AttrOption}.
     *
     * @param option The attribute option
     * @param value  The option value, null to remove the previously set {@link AttrOption}.
     * @return this
     */
    default <T> AttrOptions option(AttrOption<T> option, T value) {
        return this.getOptions().option(option, value);
    }

    /**
     * Executes the given operation if the dynamic attribute exists and is not null, otherwise does nothing.
     *
     * @param option   The attribute option
     * @param consumer The given operation. Executed only if the option's value exists and is not null.
     * @param <T>      The type of the attribute value
     */
    default <T> void ifPresent(AttrOption<T> option, Consumer<T> consumer) {
        T data = this.option(option);
        if (Objects.nonNull(data)) {
            consumer.accept(data);
        }
    }

    /**
     * Executes the given operation if the dynamic attribute value is null, otherwise does nothing. The returned value
     * from the operation will be set as the dynamic attribute's value.
     *
     * @param option   The attribute option
     * @param supplier The given operation. Executed only when the option's value is null.
     * @param <T>      The type of the attribute value
     */
    default <T> void ifNull(AttrOption<T> option, Supplier<T> supplier) {
        T data = this.option(option);
        if (Objects.isNull(data)) {
            this.option(option, supplier.get());
        }
    }
}
