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

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.ToString;

import java.util.List;

/**
 * Protocol wrapper for a list of String values.
 * <p>
 * Wraps a {@link List} of {@link String} for protobuf serialization, allowing it to be used
 * as a parameter or return type in {@code @ActionMethod} handlers.
 *
 * @author 渔民小镇
 * @date 2023-02-05
 */
@ToString
@ProtobufClass
public final class StringValueList {
    /** the wrapped list of string values */
    @Protobuf(fieldType = FieldType.STRING, order = 1)
    public List<String> values;

    /**
     * Create a StringValueList wrapping the given list of strings.
     *
     * @param values the list of string values to wrap
     * @return a new StringValueList instance
     */
    public static StringValueList of(List<String> values) {
        var theValue = new StringValueList();
        theValue.values = values;
        return theValue;
    }
}
