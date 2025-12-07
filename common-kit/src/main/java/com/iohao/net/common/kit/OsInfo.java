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

import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * OsInfo
 *
 * @author 渔民小镇
 * @date 2025-08-27
 * @since 25.1
 */
@UtilityClass
@Slf4j(topic = IonetLogName.CommonStdout)
public final class OsInfo {
    @Getter
    final String osName = getName();
    @Getter
    final boolean linux = getOsMatches("Linux") || getOsMatches("LINUX");
    @Getter
    final boolean mac = getOsMatches("Mac") || getOsMatches("Mac OS X");

    private boolean getOsMatches(String osNamePrefix) {
        if (osName == null) {
            return false;
        }

        return osName.startsWith(osNamePrefix);
    }

    private String getName() {
        String value = null;
        try {
            value = System.getProperty("os.name");
        } catch (SecurityException e) {
            log.error("Caught a SecurityException reading the system property '{}'; the SystemPropsKit property value will default to null.", "os.name");
        }

        if (null == value) {
            try {
                value = System.getenv("os.name");
            } catch (SecurityException e) {
                log.error("Caught a SecurityException reading the system env '{}'; the SystemPropsKit env value will default to null.", "os.name");
            }
        }

        return value;
    }
}
