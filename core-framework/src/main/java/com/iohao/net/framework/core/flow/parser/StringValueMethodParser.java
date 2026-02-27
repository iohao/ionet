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

import com.iohao.net.framework.core.*;
import com.iohao.net.framework.core.codec.*;
import com.iohao.net.framework.protocol.wrapper.*;
import java.util.*;

/**
 * Method parser for {@link String} parameters and return types.
 * <p>
 * Converts between Java {@link String} values and the protocol wrapper types
 * {@link StringValue} and {@link StringValueList}.
 *
 * @author 渔民小镇
 * @date 2023-02-05
 */
final class StringValueMethodParser implements MethodParser {
    /** {@inheritDoc} */
    @Override
    public Class<?> getActualClazz(ActualParameter parameterReturn) {
        return parameterReturn.isList() ? StringValueList.class : StringValue.class;
    }

    /** {@inheritDoc} */
    @Override
    public Object parseParam(byte[] data, ActionMethodParameter actionMethodParameter, DataCodec codec) {

        if (actionMethodParameter.isList()) {
            if (data == null) {
                return Collections.emptyList();
            }

            return codec.decode(data, StringValueList.class).values;
        }

        if (data == null) {
            return null;
        }

        return codec.decode(data, StringValue.class).value;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public Object parseDataList(Object data, DataCodec codec) {
        StringValueList valueList = new StringValueList();
        valueList.values = (List<String>) data;
        return valueList;
    }

    /** {@inheritDoc} */
    @Override
    public Object parseData(Object data) {
        StringValue stringValue = new StringValue();
        stringValue.value = String.valueOf(data);
        return stringValue;
    }

    private StringValueMethodParser() {
    }

    /**
     * Return the singleton instance.
     *
     * @return the singleton {@code StringValueMethodParser}
     */
    public static StringValueMethodParser me() {
        return Holder.ME;
    }

    /** Singleton via JVM class-loading mechanism (lazy initialization) */
    private static class Holder {
        static final StringValueMethodParser ME = new StringValueMethodParser();
    }
}
