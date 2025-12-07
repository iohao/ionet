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

import java.util.Collection;
import java.util.List;

/**
 * BizDataCodec
 *
 * @author 渔民小镇
 * @date 2022-05-18
 */
public interface DataCodec {
    /**
     * encode data
     *
     * @param data data
     * @return bytes
     */
    byte[] encode(Object data);

    /**
     * decode data
     *
     * @param data      data
     * @param dataClass dataClass
     * @param <T>       t
     * @return data
     */
    <T> T decode(byte[] data, Class<T> dataClass);

    /**
     * codecName
     *
     * @return codecName
     */
    default String codecName() {
        return this.getClass().getSimpleName();
    }

    default byte[] encode(int data) {
        return encode(IntValue.of(data));
    }

    default byte[] encode(boolean data) {
        return encode(BoolValue.of(data));
    }

    default byte[] encode(long data) {
        return encode(LongValue.of(data));
    }

    default byte[] encode(String data) {
        return encode(StringValue.of(data));
    }

    default byte[] encodeListInt(List<Integer> dataList) {
        return encode(IntValueList.of(dataList));
    }

    default byte[] encodeListBool(List<Boolean> dataList) {
        return encode(BoolValueList.of(dataList));
    }

    default byte[] encodeListLong(List<Long> dataList) {
        return encode(LongValueList.of(dataList));
    }

    default byte[] encodeListString(List<String> dataList) {
        return encode(StringValueList.of(dataList));
    }

    default byte[] encodeList(Collection<?> dataList) {
        return encodeList(dataList, DataCodecManager.getDataCodec());
    }

    default byte[] encodeList(Collection<?> dataList, DataCodec codec) {
        return encode(ByteValueList.of(dataList, codec));
    }
}
