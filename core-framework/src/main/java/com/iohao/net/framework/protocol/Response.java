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
package com.iohao.net.framework.protocol;

import com.iohao.net.framework.protocol.wrapper.*;

import java.util.List;

/**
 * Response
 *
 * @author 渔民小镇
 * @date 2025-09-07
 * @since 25.1
 */
public interface Response extends RemoteMessage, CommonResponse, UserIdentity {
    default <T> T getData(Class<T> clazz) {
        return this.getValue(clazz);
    }

    <T> T getValue(Class<T> clazz);

    <T> List<T> listValue(Class<? extends T> clazz);

    default String getString() {
        return this.getValue(StringValue.class).value;
    }

    default List<String> listString() {
        return this.getValue(StringValueList.class).values;
    }

    default int getInt() {
        return this.getValue(IntValue.class).value;
    }

    default List<Integer> listInt() {
        return this.getValue(IntValueList.class).values;
    }

    default long getLong() {
        return this.getValue(LongValue.class).value;
    }

    default List<Long> listLong() {
        return this.getValue(LongValueList.class).values;
    }

    default boolean getBoolean() {
        return this.getValue(BoolValue.class).value;
    }

    default List<Boolean> listBoolean() {
        return this.getValue(BoolValueList.class).values;
    }
}
