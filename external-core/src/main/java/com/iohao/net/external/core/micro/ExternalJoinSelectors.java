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
package com.iohao.net.external.core.micro;

import com.iohao.net.common.kit.exception.*;
import com.iohao.net.external.core.config.*;
import java.util.*;
import lombok.experimental.*;

/**
 * Registry of {@link ExternalJoinSelector} implementations keyed by transport type.
 *
 * @author 渔民小镇
 * @date 2023-05-29
 */
@UtilityClass
public final class ExternalJoinSelectors {
    final EnumMap<ExternalJoinEnum, ExternalJoinSelector> map = new EnumMap<>(ExternalJoinEnum.class);

    /**
     * Register a selector if no selector exists for its transport type.
     *
     * @param joinSelector selector implementation
     */
    public void putIfAbsent(ExternalJoinSelector joinSelector) {
        putIfAbsent(joinSelector.getExternalJoinEnum(), joinSelector);
    }

    /**
     * Register a selector for the given transport type if absent.
     *
     * @param joinEnum transport type
     * @param joinSelector selector implementation
     */
    public void putIfAbsent(ExternalJoinEnum joinEnum, ExternalJoinSelector joinSelector) {
        map.putIfAbsent(joinEnum, joinSelector);
    }

    /**
     * Get ExternalJoinSelector By ExternalJoinEnum
     *
     * @param joinEnum transport type
     * @return registered selector
     */
    public ExternalJoinSelector getExternalJoinSelector(ExternalJoinEnum joinEnum) {
        if (!map.containsKey(joinEnum)) {
            ThrowKit.ofRuntimeException(joinEnum + " has no implementation class");
        }

        return map.get(joinEnum);
    }
}
