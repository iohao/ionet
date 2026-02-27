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
package com.iohao.net.framework.core.codec;

import com.iohao.net.framework.protocol.wrapper.*;
import java.util.*;

/**
 * Strategy interface for encoding and decoding business data.
 * <p>
 * Provides methods to serialize objects to {@code byte[]} and deserialize {@code byte[]} back
 * to typed objects. Default convenience methods handle primitive wrappers ({@link IntValue},
 * {@link LongValue}, {@link BoolValue}, {@link StringValue}) and list wrappers
 * ({@link IntValueList}, {@link LongValueList}, {@link BoolValueList}, {@link StringValueList},
 * {@link ByteValueList}) transparently.
 *
 * @author 渔民小镇
 * @date 2022-05-18
 */
public interface DataCodec {
    /**
     * Encode an object to a byte array.
     *
     * @param data the object to encode
     * @return the encoded byte array
     */
    byte[] encode(Object data);

    /**
     * Decode a byte array into an object of the specified type.
     *
     * @param data      the byte array to decode
     * @param dataClass the target class
     * @param <T>       the target type
     * @return the decoded object
     */
    <T> T decode(byte[] data, Class<T> dataClass);

    /**
     * Return the human-readable name of this codec.
     *
     * @return the codec name
     */
    default String codecName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Encode an int value by wrapping it in {@link IntValue}.
     *
     * @param data the int value
     * @return the encoded byte array
     */
    default byte[] encode(int data) {
        return encode(IntValue.of(data));
    }

    /**
     * Encode a boolean value by wrapping it in {@link BoolValue}.
     *
     * @param data the boolean value
     * @return the encoded byte array
     */
    default byte[] encode(boolean data) {
        return encode(BoolValue.of(data));
    }

    /**
     * Encode a long value by wrapping it in {@link LongValue}.
     *
     * @param data the long value
     * @return the encoded byte array
     */
    default byte[] encode(long data) {
        return encode(LongValue.of(data));
    }

    /**
     * Encode a string value by wrapping it in {@link StringValue}.
     *
     * @param data the string value
     * @return the encoded byte array
     */
    default byte[] encode(String data) {
        return encode(StringValue.of(data));
    }

    /**
     * Encode a list of integers by wrapping it in {@link IntValueList}.
     *
     * @param dataList the list of integers
     * @return the encoded byte array
     */
    default byte[] encodeListInt(List<Integer> dataList) {
        return encode(IntValueList.of(dataList));
    }

    /**
     * Encode a list of booleans by wrapping it in {@link BoolValueList}.
     *
     * @param dataList the list of booleans
     * @return the encoded byte array
     */
    default byte[] encodeListBool(List<Boolean> dataList) {
        return encode(BoolValueList.of(dataList));
    }

    /**
     * Encode a list of longs by wrapping it in {@link LongValueList}.
     *
     * @param dataList the list of longs
     * @return the encoded byte array
     */
    default byte[] encodeListLong(List<Long> dataList) {
        return encode(LongValueList.of(dataList));
    }

    /**
     * Encode a list of strings by wrapping it in {@link StringValueList}.
     *
     * @param dataList the list of strings
     * @return the encoded byte array
     */
    default byte[] encodeListString(List<String> dataList) {
        return encode(StringValueList.of(dataList));
    }

    /**
     * Encode a collection of objects into a {@link ByteValueList} using the default codec.
     *
     * @param dataList the collection of objects to encode
     * @return the encoded byte array
     */
    default byte[] encodeList(Collection<?> dataList) {
        return encodeList(dataList, DataCodecManager.getDataCodec());
    }

    /**
     * Encode a collection of objects into a {@link ByteValueList} using the specified codec.
     *
     * @param dataList the collection of objects to encode
     * @param codec    the codec to use for encoding each element
     * @return the encoded byte array
     */
    default byte[] encodeList(Collection<?> dataList, DataCodec codec) {
        return encode(ByteValueList.of(dataList, codec));
    }
}
