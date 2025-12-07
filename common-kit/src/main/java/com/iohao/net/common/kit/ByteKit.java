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

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;

/**
 * ByteKit
 *
 * @author 渔民小镇
 * @date 2024-08-10
 * @since 21.15
 */
@UtilityClass
public class ByteKit {

    public byte[] toBytes(long value) {
        byte[] b = new byte[8];
        b[7] = (byte) (value & 0xff);
        b[6] = (byte) (value >> 8 & 0xff);
        b[5] = (byte) (value >> 16 & 0xff);
        b[4] = (byte) (value >> 24 & 0xff);
        b[3] = (byte) (value >> 32 & 0xff);
        b[2] = (byte) (value >> 40 & 0xff);
        b[1] = (byte) (value >> 48 & 0xff);
        b[0] = (byte) (value >> 56 & 0xff);
        return b;
    }

    public long getLong(byte[] array) {
        return ((((long) array[0] & 0xff) << 56)
                | (((long) array[1] & 0xff) << 48)
                | (((long) array[2] & 0xff) << 40)
                | (((long) array[3] & 0xff) << 32)
                | (((long) array[4] & 0xff) << 24)
                | (((long) array[5] & 0xff) << 16)
                | (((long) array[6] & 0xff) << 8)
                | (((long) array[7] & 0xff)));
    }

    public byte[] toBytes(int value) {
        byte[] b = new byte[4];
        b[3] = (byte) (value & 0xff);
        b[2] = (byte) (value >> 8 & 0xff);
        b[1] = (byte) (value >> 16 & 0xff);
        b[0] = (byte) (value >> 24 & 0xff);
        return b;
    }

    public int getInt(byte[] array) {
        return (((int) array[0] & 0xff) << 24)
                | (((int) array[1] & 0xff) << 16)
                | (((int) array[2] & 0xff) << 8)
                | (((int) array[3] & 0xff));
    }

    public byte[] toBytes(boolean value) {
        return new byte[]{(byte) (value ? 1 : 0)};
    }

    public boolean getBoolean(byte[] array) {
        return array[0] != 0;
    }

    public byte[] toBytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    public String getString(byte[] array) {
        return new String(array, StandardCharsets.UTF_8);
    }

    public byte[] getPayload(byte[] payload, int payloadLength) {
        if (payload.length == payloadLength) {
            return payload;
        }

        byte[] data = new byte[payloadLength];
        System.arraycopy(payload, 0, data, 0, payloadLength);

        return data;
    }

    public static byte[] getBytes(byte[] data) {
        return data == null ? CommonConst.emptyBytes : data;
    }

    public static byte[] ofBytes(int length) {
        return length == 0 ? CommonConst.emptyBytes : new byte[length];
    }
}
