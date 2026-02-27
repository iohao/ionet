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

import com.iohao.net.common.kit.*;
import com.iohao.net.framework.protocol.wrapper.*;

/**
 * Protobuf-based implementation of {@link DataCodec}.
 * <p>
 * Delegates serialization and deserialization to {@link ProtoKit} (jprotobuf).
 * Pre-encodes and caches the byte representations of {@code true} and {@code false}
 * {@link BoolValue} instances to avoid repeated encoding of common boolean values.
 *
 * @author 渔民小镇
 * @date 2022-05-18
 */
public final class ProtoDataCodec implements DataCodec {
    @Override
    public byte[] encode(Object data) {
        return ProtoKit.encode(data);
    }

    @Override
    public <T> T decode(byte[] data, Class<T> dataClass) {
        if (data == null) {
            return ProtoKit.decode(CommonConst.emptyBytes, dataClass);
        }

        return ProtoKit.decode(data, dataClass);
    }

    /** cached encoded bytes for BoolValue(true) */
    static final byte[] cacheBoolTrue;
    /** cached encoded bytes for BoolValue(false) */
    static final byte[] cacheBoolFalse;

    static {
        cacheBoolTrue = ProtoKit.encode(BoolValue.of(true));
        cacheBoolFalse = ProtoKit.encode(BoolValue.of(false));
    }

    /**
     * Encode a boolean value using cached byte arrays for efficiency.
     *
     * @param data the boolean value
     * @return the pre-cached encoded byte array
     */
    @Override
    public byte[] encode(boolean data) {
        return data ? cacheBoolTrue : cacheBoolFalse;
    }

    @Override
    public String codecName() {
        return "j-protobuf";
    }
}
