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

import com.iohao.net.common.kit.CollKit;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Thread-safe attribute storage container. Holds key-value pairs keyed by {@link AttrOption} instances.
 *
 * @author 渔民小镇
 * @date 2022-01-31
 */
public class AttrOptions implements Serializable {
    @Serial
    private static final long serialVersionUID = 9042891580724596100L;

    final Map<AttrOption<?>, Object> options = CollKit.ofConcurrentHashMap();

    /**
     * Create a new container by copying all entries from the given source.
     *
     * @param attrOptions the source container whose entries are copied into this instance
     */
    public AttrOptions(AttrOptions attrOptions) {
        this.options.putAll(attrOptions.options);
    }

    /** Create an empty attribute storage container. */
    public AttrOptions() {
    }

    /**
     * Gets the option value.
     * <p>
     * Returns the default value if the option does not exist.
     *
     * @param option The attribute option
     * @return The option value, or the default option value if the option does not exist.
     */
    @SuppressWarnings("unchecked")
    public <T> T option(AttrOption<T> option) {
        Object value = options.get(option);
        if (Objects.nonNull(value)) {
            return (T) value;
        }

        if (Objects.nonNull(option.supplier())) {
            T newValue = option.supplier().get();
            this.option(option, newValue);
            return newValue;
        }

        if (Objects.nonNull(option.defaultValue())) {
            return option.defaultValue();
        }

        return null;
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
    public <T> AttrOptions option(AttrOption<T> option, T value) {
        if (Objects.isNull(value)) {
            options.remove(option);
            return this;
        }

        options.put(option, value);
        return this;
    }
}
