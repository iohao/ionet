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
import com.iohao.net.framework.protocol.wrapper.BoolValue;
import com.iohao.net.framework.protocol.wrapper.BoolValueList;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Method parser for {@code boolean}/{@link Boolean} parameters and return types.
 * <p>
 * Converts between Java {@code boolean}/{@link Boolean} values and the protocol wrapper types
 * {@link BoolValue} and {@link BoolValueList}.
 *
 * @author 渔民小镇
 * @date 2023-02-07
 */
final class BoolValueMethodParser implements MethodParser {
    /** {@inheritDoc} */
    @Override
    public Class<?> getActualClazz(ActualParameter parameterReturn) {
        return parameterReturn.isList() ? BoolValueList.class : BoolValue.class;
    }

    /** {@inheritDoc} */
    @Override
    public Object parseParam(byte[] data, ActionMethodParameter actionMethodParameter, DataCodec codec) {

        if (actionMethodParameter.isList()) {
            if (Objects.isNull(data)) {
                return Collections.emptyList();
            }

            return codec.decode(data, BoolValueList.class).values;
        }

        if (Objects.isNull(data)) {
            return false;
        }

        return codec.decode(data, BoolValue.class).value;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public Object parseDataList(Object data, DataCodec codec) {
        var valueList = new BoolValueList();
        valueList.values = (List<Boolean>) data;
        return valueList;
    }

    /** {@inheritDoc} */
    @Override
    public Object parseData(Object data) {
        return BoolValue.of((boolean) data);
    }

    private BoolValueMethodParser() {
    }

    /**
     * Return the singleton instance.
     *
     * @return the singleton {@code BoolValueMethodParser}
     */
    public static BoolValueMethodParser me() {
        return Holder.ME;
    }

    private static class Holder {
        static final BoolValueMethodParser ME = new BoolValueMethodParser();
    }
}
