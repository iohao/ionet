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
package com.iohao.net.framework;

import com.iohao.net.common.kit.*;
import com.iohao.net.framework.core.*;
import java.util.*;
import lombok.experimental.*;

/**
 * Eagerly initializes framework-wide configuration and network utilities at class-load time.
 * <p>
 * On first access the static initializer discovers all {@link CoreConfigLoader} implementations
 * via {@link ServiceLoader} and applies their configuration, then forces early resolution of
 * the local IP address so that later lookups are instantaneous.
 *
 * @author 渔民小镇
 * @date 2025-10-18
 * @since 25.1
 */
@UtilityClass
final class Preloading {
    static {
        ServiceLoader.load(CoreConfigLoader.class).forEach(CoreConfigLoader::config);
        empty(NetworkKit.LOCAL_IP);
    }

    /** No-op consumer used solely to trigger evaluation of a lazily-computed value. */
    private void empty(Object x) {
    }

    /** Trigger class loading (and therefore the static initializer) from external code. */
    void loading() {
    }
}
