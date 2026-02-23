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
package com.iohao.net.framework.protocol;

import com.iohao.net.common.kit.ByteKit;
import lombok.Getter;
import lombok.Setter;

/**
 * Base message exchanged between external (Netty) servers and logic servers.
 * <p>
 * This sealed class carries a future correlation id, a binary payload, and the
 * originating external server id. Only {@link ExternalRequestMessage} and
 * {@link ExternalResponseMessage} are permitted subtypes.
 *
 * @author 渔民小镇
 * @date 2025-09-10
 * @since 25.1
 */
@Getter
@Setter
sealed class ExternalCommonMessage permits ExternalRequestMessage, ExternalResponseMessage {
    /** Correlation id used to match asynchronous request/response pairs. */
    long futureId;
    /** Serialized binary payload of the message. */
    byte[] payload;
    /** Identifier of the external server that originated or will receive this message. */
    int externalServerId;
}

/**
 * Mixin interface for setting a binary payload on a message.
 * <p>
 * Provides convenience default methods that convert common primitive types
 * and {@link String} to a {@code byte[]} before delegating to
 * {@link #setPayload(byte[])}.
 */
interface ExternalPayloadSetting {
    /**
     * Set the raw binary payload.
     *
     * @param payload the payload bytes
     */
    void setPayload(byte[] payload);

    /**
     * Set the payload from an {@code int} value.
     *
     * @param payload the int value to serialize
     */
    default void setPayload(int payload) {
        this.setPayload(ByteKit.toBytes(payload));
    }

    /**
     * Set the payload from a {@code long} value.
     *
     * @param payload the long value to serialize
     */
    default void setPayload(long payload) {
        this.setPayload(ByteKit.toBytes(payload));
    }

    /**
     * Set the payload from a {@code boolean} value.
     *
     * @param payload the boolean value to serialize
     */
    default void setPayload(boolean payload) {
        this.setPayload(ByteKit.toBytes(payload));
    }

    /**
     * Set the payload from a {@link String} value.
     *
     * @param payload the string value to serialize
     */
    default void setPayload(String payload) {
        this.setPayload(ByteKit.toBytes(payload));
    }
}

/**
 * Mixin interface for reading a binary payload from a message.
 * <p>
 * Provides convenience default methods that deserialize the raw {@code byte[]}
 * into common primitive types and {@link String}.
 */
interface ExternalPayloadGetting {
    /**
     * Get the raw binary payload.
     *
     * @return the payload bytes
     */
    byte[] getPayload();

    /**
     * Deserialize the payload as an {@code int}.
     *
     * @return the int value
     */
    default int getPayloadAsInt() {
        return ByteKit.getInt(this.getPayload());
    }

    /**
     * Deserialize the payload as a {@code long}.
     *
     * @return the long value
     */
    default long getPayloadAsLong() {
        return ByteKit.getLong(this.getPayload());
    }

    /**
     * Deserialize the payload as a {@code boolean}.
     *
     * @return the boolean value
     */
    default boolean getPayloadAsBool() {
        return ByteKit.getBoolean(this.getPayload());
    }

    /**
     * Deserialize the payload as a {@link String}.
     *
     * @return the string value
     */
    default String getPayloadAsString() {
        return ByteKit.getString(this.getPayload());
    }
}