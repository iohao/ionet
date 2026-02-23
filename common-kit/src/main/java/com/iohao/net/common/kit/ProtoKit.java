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
package com.iohao.net.common.kit;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.iohao.net.common.kit.concurrent.TaskKit;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * JProtobuf serialization utilities for encoding and decoding objects.
 *
 * @author 渔民小镇
 * @date 2022-01-11
 */
@UtilityClass
@Slf4j(topic = IonetLogName.CommonStdout)
public class ProtoKit {
    /**
     * Encode an object to a protobuf byte array.
     *
     * @param data the object to encode; if {@code null}, returns an empty byte array
     * @return the encoded bytes, or an empty byte array on failure
     */
    @SuppressWarnings("unchecked")
    public byte[] encode(Object data) {

        if (Objects.isNull(data)) {
            return CommonConst.emptyBytes;
        }

        Class clazz = data.getClass();
        Codec<Object> codec = ProtobufProxy.create(clazz);

        try {
            return codec.encode(data);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

        return CommonConst.emptyBytes;
    }

    /**
     * Decode a protobuf byte array into an object of the specified type.
     *
     * @param data  the byte array to decode; if {@code null}, returns {@code null}
     * @param clazz the target class
     * @param <T>   the target type
     * @return the decoded object, or {@code null} on failure
     */
    public <T> T decode(byte[] data, Class<T> clazz) {

        if (Objects.isNull(data)) {
            return null;
        }

        Codec<T> codec = ProtobufProxy.create(clazz);

        try {
            return codec.decode(data);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * Pre-create and warm up the protobuf codec for the given class on a virtual thread.
     *
     * @param clazz the class to pre-create a codec for
     */
    public void create(Class<?> clazz) {
        TaskKit.executeVirtual(() -> {
            try {
                ProtobufProxy.create(clazz);
                encode(clazz.getDeclaredConstructor().newInstance());
            } catch (Throwable ignored) {
            }
        });
    }
}
