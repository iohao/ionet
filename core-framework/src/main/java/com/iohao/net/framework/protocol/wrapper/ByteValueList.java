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
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.net.framework.core.codec.DataCodec;
import com.iohao.net.framework.core.codec.DataCodecManager;
import com.iohao.net.common.kit.CollKit;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

/**
 * Protocol wrapper for a list of byte array values.
 * <p>
 * Wraps a {@link List} of {@code byte[]} for protobuf serialization. Each element in the list
 * is an individually encoded object, enabling transport of heterogeneous or complex object lists
 * over the wire. An empty singleton is cached for the no-data case.
 *
 * @author 渔民小镇
 * @date 2023-04-17
 */
@ToString
@ProtobufClass
public final class ByteValueList {
    /** the wrapped list of encoded byte arrays */
    @Protobuf(fieldType = FieldType.BYTES, order = 1)
    public List<byte[]> values;

    @Ignore
    private static final ByteValueList empty = new ByteValueList();

    /**
     * Create a ByteValueList from pre-encoded byte arrays.
     *
     * @param values the list of byte arrays
     * @return a new ByteValueList, or an empty instance if the list is empty
     */
    private static ByteValueList ofBytes(List<byte[]> values) {
        if (CollKit.isEmpty(values)) {
            return new ByteValueList();
        }

        var theValue = new ByteValueList();
        theValue.values = values;
        return theValue;
    }

    /**
     * Encode each element in the collection using the default codec and wrap the results.
     *
     * @param values the collection of objects to encode
     * @param <T>    the element type
     * @return a ByteValueList containing the encoded byte arrays
     */
    public static <T> ByteValueList of(Collection<T> values) {
        return of(values, DataCodecManager.getDataCodec());
    }

    /**
     * Encode each element in the collection using the specified codec and wrap the results.
     *
     * @param values the collection of objects to encode
     * @param codec  the codec to use for encoding each element
     * @param <T>    the element type
     * @return a ByteValueList containing the encoded byte arrays, or a cached empty instance
     */
    public static <T> ByteValueList of(Collection<T> values, DataCodec codec) {
        if (CollKit.isEmpty(values)) {
            return empty;
        }

        return ofBytes(values.stream().map(codec::encode).toList());
    }
}
