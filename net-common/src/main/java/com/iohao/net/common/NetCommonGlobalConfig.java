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
package com.iohao.net.common;

import lombok.*;
import lombok.experimental.*;

/**
 * Global configuration entry for net-common components.
 *
 * <p>Provides the shared {@link Publisher} instance used by Aeron publishing flows.</p>
 *
 * @author 渔民小镇
 * @date 2025-10-14
 * @since 25.1
 */
@UtilityClass
public final class NetCommonGlobalConfig {
    @Setter
    Publisher publisher = new DefaultPublisher();

    /**
     * Returns the shared publisher instance used by net-common.
     *
     * @return shared publisher
     */
    public static Publisher getPublisher() {
        if (publisher == null) {
            // Lazily recreate the default publisher when a custom publisher was cleared.
            publisher = new DefaultPublisher();
        }

        return publisher;
    }
}
