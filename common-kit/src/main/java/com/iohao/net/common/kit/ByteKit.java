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
 * Byte array conversion utilities for primitive types using big-endian byte order.
 *
 * @author 渔民小镇
 * @date 2024-08-10
 * @since 21.15
 */
@UtilityClass
public class ByteKit {

    /**
     * Convert a long value to an 8-byte big-endian byte array.
     *
     * @param value the long value to convert
     * @return an 8-byte array in big-endian order (most significant byte at index 0)
     */
    public byte[] toBytes(long value) {
        byte[] b = new byte[8];
        // Big-endian: store the least significant byte at the highest index
        b[7] = (byte) (value & 0xff);         // bits  0-7  (LSB)
        b[6] = (byte) (value >> 8 & 0xff);    // bits  8-15
        b[5] = (byte) (value >> 16 & 0xff);   // bits 16-23
        b[4] = (byte) (value >> 24 & 0xff);   // bits 24-31
        b[3] = (byte) (value >> 32 & 0xff);   // bits 32-39
        b[2] = (byte) (value >> 40 & 0xff);   // bits 40-47
        b[1] = (byte) (value >> 48 & 0xff);   // bits 48-55
        b[0] = (byte) (value >> 56 & 0xff);   // bits 56-63 (MSB)
        return b;
    }

    /**
     * Reconstruct a long value from an 8-byte big-endian byte array.
     *
     * @param array the byte array (must be at least 8 bytes)
     * @return the reconstructed long value
     */
    public long getLong(byte[] array) {
        // Big-endian: most significant byte at index 0, shifted left by 56 bits
        return ((((long) array[0] & 0xff) << 56)   // MSB → bits 56-63
                | (((long) array[1] & 0xff) << 48)  // bits 48-55
                | (((long) array[2] & 0xff) << 40)  // bits 40-47
                | (((long) array[3] & 0xff) << 32)  // bits 32-39
                | (((long) array[4] & 0xff) << 24)  // bits 24-31
                | (((long) array[5] & 0xff) << 16)  // bits 16-23
                | (((long) array[6] & 0xff) << 8)   // bits  8-15
                | (((long) array[7] & 0xff)));       // bits  0-7  (LSB)
    }

    /**
     * Convert an int value to a 4-byte big-endian byte array.
     *
     * @param value the int value to convert
     * @return a 4-byte array in big-endian order (most significant byte at index 0)
     */
    public byte[] toBytes(int value) {
        byte[] b = new byte[4];
        // Big-endian: store the least significant byte at the highest index
        b[3] = (byte) (value & 0xff);         // bits  0-7  (LSB)
        b[2] = (byte) (value >> 8 & 0xff);    // bits  8-15
        b[1] = (byte) (value >> 16 & 0xff);   // bits 16-23
        b[0] = (byte) (value >> 24 & 0xff);   // bits 24-31 (MSB)
        return b;
    }

    /**
     * Reconstruct an int value from a 4-byte big-endian byte array.
     *
     * @param array the byte array (must be at least 4 bytes)
     * @return the reconstructed int value
     */
    public int getInt(byte[] array) {
        // Big-endian: most significant byte at index 0, shifted left by 24 bits
        return (((int) array[0] & 0xff) << 24)   // MSB → bits 24-31
                | (((int) array[1] & 0xff) << 16) // bits 16-23
                | (((int) array[2] & 0xff) << 8)  // bits  8-15
                | (((int) array[3] & 0xff));       // bits  0-7  (LSB)
    }

    /**
     * Convert a boolean value to a single-byte array.
     *
     * @param value the boolean value to convert
     * @return a 1-byte array containing {@code 1} for true, {@code 0} for false
     */
    public byte[] toBytes(boolean value) {
        return new byte[]{(byte) (value ? 1 : 0)};
    }

    /**
     * Reconstruct a boolean value from a byte array.
     *
     * @param array the byte array (must be at least 1 byte)
     * @return {@code true} if the first byte is non-zero, {@code false} otherwise
     */
    public boolean getBoolean(byte[] array) {
        return array[0] != 0;
    }

    /**
     * Convert a string to a UTF-8 encoded byte array.
     *
     * @param value the string to convert
     * @return the UTF-8 encoded byte array
     */
    public byte[] toBytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Decode a UTF-8 byte array into a string.
     *
     * @param array the UTF-8 encoded byte array
     * @return the decoded string
     */
    public String getString(byte[] array) {
        return new String(array, StandardCharsets.UTF_8);
    }

    /**
     * Extract a payload of the specified length from a byte array.
     * <p>
     * Return the original array if its length matches {@code payloadLength};
     * otherwise copy the first {@code payloadLength} bytes into a new array.
     *
     * @param payload       the source byte array
     * @param payloadLength the desired payload length
     * @return a byte array of exactly {@code payloadLength} bytes
     */
    public byte[] getPayload(byte[] payload, int payloadLength) {
        if (payload.length == payloadLength) {
            return payload;
        }

        byte[] data = new byte[payloadLength];
        System.arraycopy(payload, 0, data, 0, payloadLength);

        return data;
    }

    /**
     * Return the given byte array, or an empty byte array if it is null.
     *
     * @param data the byte array, may be null
     * @return the original array, or an empty array if null
     */
    public static byte[] getBytes(byte[] data) {
        return data == null ? CommonConst.emptyBytes : data;
    }

    /**
     * Create a byte array of the specified length, returning a shared empty array for length zero.
     *
     * @param length the desired array length
     * @return a new byte array, or an empty array if length is 0
     */
    public static byte[] ofBytes(int length) {
        return length == 0 ? CommonConst.emptyBytes : new byte[length];
    }
}
