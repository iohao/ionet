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
package com.iohao.net.framework.protocol.wrapper;

import lombok.*;

/**
 * Record that pairs a single-value wrapper class with its corresponding list wrapper class.
 * <p>
 * For example, {@link IntValue} is paired with {@link IntValueList}. Used by {@link WrapperKit}
 * to resolve the correct wrapper types for primitive and boxed Java types.
 *
 * @author 渔民小镇
 * @date 2024-11-01
 * @since 21.20
 */
@Getter
public final class ValueRecord {
    /** the single-value wrapper class (e.g., IntValue.class) */
    final Class<?> valueClazz;
    /** the list wrapper class (e.g., IntValueList.class) */
    final Class<?> valueListClazz;

    /**
     * Create a ValueRecord pairing a single-value class with a list class.
     *
     * @param valueClazz     the single-value wrapper class
     * @param valueListClazz the list wrapper class
     */
    ValueRecord(Class<?> valueClazz, Class<?> valueListClazz) {
        this.valueClazz = valueClazz;
        this.valueListClazz = valueListClazz;
    }
}
