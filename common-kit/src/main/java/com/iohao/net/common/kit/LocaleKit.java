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
package com.iohao.net.common.kit;

import java.util.*;
import lombok.experimental.*;

/**
 * Locale detection utilities.
 *
 * @author 渔民小镇
 * @date 2025-10-04
 * @since 25.1
 */
@UtilityClass
public class LocaleKit {
    private boolean checked;
    private boolean defaultValue;

    /**
     * Check if the default locale is Chinese (zh_CN).
     * <p>
     * Result is cached after first invocation.
     *
     * @return {@code true} if the default locale is zh_CN
     */
    public boolean isChina() {
        if (checked) {
            return defaultValue;
        }

        checked = true;
        var locale = Locale.getDefault();
        defaultValue = "zh".equals(locale.getLanguage()) && "CN".equals(locale.getCountry());
        return defaultValue;
    }
}
