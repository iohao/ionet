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
 * Method parser for {@code int}/{@link Integer} parameters and return types.
 * <p>
 * Converts between Java {@code int}/{@link Integer} values and the protocol wrapper types
 * {@link IntValue} and {@link IntValueList}.
 *
 * @author 渔民小镇
 * @date 2023-02-10
 */
final class IntValueMethodParser implements MethodParser {

    /** {@inheritDoc} */
    @Override
    public Class<?> getActualClazz(ActualParameter parameterReturn) {
        return parameterReturn.isList() ? IntValueList.class : IntValue.class;
    }

    /** {@inheritDoc} */
    @Override
    public Object parseParam(byte[] data, ActionMethodParameter actionMethodParameter, DataCodec codec) {

        if (actionMethodParameter.isList()) {
            if (data == null) {
                return Collections.emptyList();
            }

            return codec.decode(data, IntValueList.class).values;
        }

        if (data == null) {
            return 0;
        }

        return codec.decode(data, IntValue.class).value;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public Object parseDataList(Object data, DataCodec codec) {
        var valueList = new IntValueList();
        valueList.values = (List<Integer>) data;
        return valueList;
    }

    /** {@inheritDoc} */
    @Override
    public Object parseData(Object data) {
        var intValue = new IntValue();
        intValue.value = (int) data;
        return intValue;
    }

    private IntValueMethodParser() {
    }

    /**
     * Return the singleton instance.
     *
     * @return the singleton {@code IntValueMethodParser}
     */
    public static IntValueMethodParser me() {
        return Holder.ME;
    }

    private static class Holder {
        static final IntValueMethodParser ME = new IntValueMethodParser();
    }
}
