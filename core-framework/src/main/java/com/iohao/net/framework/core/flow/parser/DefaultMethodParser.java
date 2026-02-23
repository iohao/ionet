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
import com.iohao.net.framework.protocol.wrapper.ByteValueList;
import com.iohao.net.common.kit.CollKit;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Default method parser for complex (non-primitive) action method parameters and return types.
 * <p>
 * Handles single objects and lists by delegating to the configured {@link DataCodec} for
 * serialization and deserialization. Uses {@link com.iohao.net.framework.protocol.wrapper.ByteValueList}
 * as the wire format for list parameters.
 *
 * @author 渔民小镇
 * @date 2022-06-26
 */
class DefaultMethodParser implements MethodParser {
    /** {@inheritDoc} */
    @Override
    public Class<?> getActualClazz(ActualParameter parameterReturn) {
        return parameterReturn.getActualTypeArgumentClass();
    }

    /** {@inheritDoc} */
    @Override
    public Object parseParam(byte[] data, ActionMethodParameter actionMethodParameter, DataCodec codec) {
        Class<?> actualTypeArgumentClazz = actionMethodParameter.getActualTypeArgumentClass();

        if (actionMethodParameter.isList()) {
            if (Objects.isNull(data)) {
                return Collections.emptyList();
            }

            ByteValueList byteValueList = codec.decode(data, ByteValueList.class);

            if (CollKit.isEmpty(byteValueList.values)) {
                return Collections.emptyList();
            }

            return byteValueList.values.stream()
                    .map(bytes -> codec.decode(bytes, actualTypeArgumentClazz))
                    .toList();
        }

        if (Objects.isNull(data)) {
            // If an action parameter type Supplier is configured, the object is created through the Supplier.
            var o = MethodParsers.newObject(actualTypeArgumentClazz);
            if (Objects.nonNull(o)) {
                return o;
            }
        }

        return codec.decode(data, actualTypeArgumentClazz);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public Object parseDataList(Object data, DataCodec codec) {
        List<Object> list = (List<Object>) data;

        var byteValueList = new ByteValueList();
        byteValueList.values = list.stream()
                .map(codec::encode)
                .collect(Collectors.toList());

        return byteValueList;
    }

    /** {@inheritDoc} */
    @Override
    public Object parseData(Object data) {
        return data;
    }

    private DefaultMethodParser() {
    }

    /**
     * Return the singleton instance.
     *
     * @return the singleton {@code DefaultMethodParser}
     */
    public static DefaultMethodParser me() {
        return Holder.ME;
    }

    private static class Holder {
        static final DefaultMethodParser ME = new DefaultMethodParser();
    }
}
