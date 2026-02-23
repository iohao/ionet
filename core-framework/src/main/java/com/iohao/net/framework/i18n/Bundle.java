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
package com.iohao.net.framework.i18n;

import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Locale-aware resource bundle accessor for framework i18n messages.
 * <p>
 * Loads the {@code iohao.properties} (or locale-specific variant) on first access
 * and provides a simple {@link #getMessage(String)} lookup.
 *
 * @author 渔民小镇
 * @date 2024-10-02
 * @since 21.18
 */
@UtilityClass
public final class Bundle {
    final String baseName = "iohao";
    ResourceBundle bundle;

    /**
     * Return the lazily-loaded resource bundle for the default locale.
     *
     * @return the resource bundle
     */
    ResourceBundle getBundle() {
        if (Objects.isNull(bundle)) {
            bundle = ResourceBundle.getBundle(baseName, Locale.getDefault());
        }

        return bundle;
    }

    /**
     * Look up a localized message by key.
     *
     * @param key the message key
     * @return the localized message string
     */
    public String getMessage(String key) {
        return getBundle().getString(key);
    }
}
