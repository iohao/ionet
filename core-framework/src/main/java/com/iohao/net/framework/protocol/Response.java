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
 * Represent an outbound response message with typed data extraction capabilities.
 * <p>
 * Extends {@link RemoteMessage} for inter-server fields, {@link CommonResponse} for error
 * information, and {@link UserIdentity} for user binding. Provides convenience methods
 * to decode the raw response payload into single values or lists of common types
 * (String, int, long, boolean) as well as arbitrary protobuf-compatible types.
 *
 * @author 渔民小镇
 * @date 2025-09-07
 * @since 25.1
 */
public interface Response extends RemoteMessage, CommonResponse, UserIdentity {
    /**
     * Decode the response payload into the specified type.
     * <p>
     * Alias for {@link #getValue(Class)}.
     *
     * @param <T>   the target type
     * @param clazz the class to decode into
     * @return the decoded object
     */
    default <T> T getData(Class<T> clazz) {
        return this.getValue(clazz);
    }

    /**
     * Decode the response payload into the specified type.
     *
     * @param <T>   the target type
     * @param clazz the class to decode into
     * @return the decoded object
     */
    <T> T getValue(Class<T> clazz);

    /**
     * Decode the response payload into a list of the specified element type.
     *
     * @param <T>   the element type
     * @param clazz the element class to decode into
     * @return a list of decoded objects
     */
    <T> List<T> listValue(Class<? extends T> clazz);

    /**
     * Get the response payload as a single String value.
     *
     * @return the decoded string
     */
    default String getString() {
        return this.getValue(StringValue.class).value;
    }

    /**
     * Get the response payload as a list of String values.
     *
     * @return the decoded string list
     */
    default List<String> listString() {
        return this.getValue(StringValueList.class).values;
    }

    /**
     * Get the response payload as a single int value.
     *
     * @return the decoded int
     */
    default int getInt() {
        return this.getValue(IntValue.class).value;
    }

    /**
     * Get the response payload as a list of Integer values.
     *
     * @return the decoded integer list
     */
    default List<Integer> listInt() {
        return this.getValue(IntValueList.class).values;
    }

    /**
     * Get the response payload as a single long value.
     *
     * @return the decoded long
     */
    default long getLong() {
        return this.getValue(LongValue.class).value;
    }

    /**
     * Get the response payload as a list of Long values.
     *
     * @return the decoded long list
     */
    default List<Long> listLong() {
        return this.getValue(LongValueList.class).values;
    }

    /**
     * Get the response payload as a single boolean value.
     *
     * @return the decoded boolean
     */
    default boolean getBoolean() {
        return this.getValue(BoolValue.class).value;
    }

    /**
     * Get the response payload as a list of Boolean values.
     *
     * @return the decoded boolean list
     */
    default List<Boolean> listBoolean() {
        return this.getValue(BoolValueList.class).values;
    }
}
