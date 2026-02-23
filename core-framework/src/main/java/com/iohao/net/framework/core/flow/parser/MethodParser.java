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
package com.iohao.net.framework.core.flow.parser;

import com.iohao.net.framework.core.ActionMethodParameter;
import com.iohao.net.framework.core.ActualParameter;
import com.iohao.net.framework.core.codec.DataCodec;

/**
 * Action method parsing: parses method parameters, parses method return value
 *
 * @author 渔民小镇
 * @date 2022-06-26
 */
public interface MethodParser {

    /**
     * Get the actual class for the parameter or return type.
     * <p>
     * For primitive-wrapper parsers this returns the corresponding protocol wrapper class
     * (e.g. {@code IntValue}, {@code IntValueList}). For the default parser it delegates
     * to {@link ActualParameter#getActualTypeArgumentClass()}.
     *
     * @param parameterReturn the parameter or return type metadata
     * @return the actual class to use for codec operations
     */
    Class<?> getActualClazz(ActualParameter parameterReturn);

    /**
     * Parses action method parameters
     *
     * @param data                  data
     * @param actionMethodParameter paramInfo
     * @param codec                 codec
     * @return The parsed data
     */
    Object parseParam(byte[] data, ActionMethodParameter actionMethodParameter, DataCodec codec);

    /**
     * Parse a list of data values into the protocol-compatible format.
     * <p>
     * Wraps the given list into the appropriate value-list wrapper type for serialization.
     *
     * @param data  the list of values returned by the action method
     * @param codec the data codec for encoding individual elements
     * @return the protocol-compatible list wrapper object
     */
    Object parseDataList(Object data, DataCodec codec);

    /**
     * Parse a single data value into the protocol-compatible format.
     * <p>
     * Wraps the given value into the appropriate value wrapper type for serialization.
     *
     * @param data the value returned by the action method
     * @return the protocol-compatible wrapper object
     */
    Object parseData(Object data);
}