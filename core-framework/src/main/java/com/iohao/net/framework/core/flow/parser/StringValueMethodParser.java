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
import com.iohao.net.framework.protocol.wrapper.StringValue;
import com.iohao.net.framework.protocol.wrapper.StringValueList;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * StringValueMethodParser
 *
 * @author 渔民小镇
 * @date 2023-02-05
 */
final class StringValueMethodParser implements MethodParser {
    @Override
    public Class<?> getActualClazz(ActualParameter parameterReturn) {
        return parameterReturn.isList() ? StringValueList.class : StringValue.class;
    }

    @Override
    public Object parseParam(byte[] data, ActionMethodParameter actionMethodParameter, DataCodec codec) {

        if (actionMethodParameter.isList()) {
            if (Objects.isNull(data)) {
                return Collections.emptyList();
            }

            return codec.decode(data, StringValueList.class).values;
        }

        if (Objects.isNull(data)) {
            return null;
        }

        return codec.decode(data, StringValue.class).value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object parseDataList(Object data, DataCodec codec) {
        StringValueList valueList = new StringValueList();
        valueList.values = (List<String>) data;
        return valueList;
    }

    @Override
    public Object parseData(Object data) {
        StringValue stringValue = new StringValue();
        stringValue.value = String.valueOf(data);
        return stringValue;
    }

    private StringValueMethodParser() {
    }

    public static StringValueMethodParser me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final StringValueMethodParser ME = new StringValueMethodParser();
    }
}
