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
package com.iohao.net.framework.communication;

import com.iohao.net.framework.core.codec.DataCodec;
import com.iohao.net.framework.core.codec.DataCodecManager;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Broadcast message delivery scoped to a specific set of users.
 * <p>
 * Provides a fluent API for selecting target user IDs, setting the broadcast payload
 * (with automatic codec encoding for various data types), and executing the broadcast.
 * Users can be added or excluded individually or in bulk before calling {@link #execute()}.
 *
 * @author 渔民小镇
 * @date 2024-06-02
 * @since 21.9
 */
public interface RangeBroadcast {
    /**
     * Return the mutable set of user IDs that will receive this broadcast.
     *
     * @return mutable set of target user IDs
     */
    Set<Long> listUserId();

    /**
     * Set the raw byte-array payload for this broadcast.
     *
     * @param data encoded payload bytes
     * @return this instance for chaining
     */
    RangeBroadcast setData(byte[] data);

    /**
     * Store the original (pre-encoding) data object for inspection or debugging.
     *
     * @param originalData the original business data before encoding
     */
    void setOriginal(Object originalData);

    /**
     * Send the response message to the remote endpoint (user, player)
     */
    void execute();

    /**
     * Users receiving the broadcast
     *
     * @param userIds userIds
     * @return this
     */
    default RangeBroadcast addUserId(Collection<Long> userIds) {
        this.listUserId().addAll(userIds);
        return this;
    }

    /**
     * Users receiving the broadcast
     *
     * @param userId userId
     * @return this
     */
    default RangeBroadcast addUserId(long userId) {
        this.listUserId().add(userId);
        return this;
    }

    /**
     * Add users to receive the broadcast, simultaneously excluding one user who should not receive it
     *
     * @param userIds       User IDs to receive the broadcast
     * @param excludeUserId User ID to be excluded
     * @return this
     */
    default RangeBroadcast addUserId(Collection<Long> userIds, long excludeUserId) {
        return this.addUserId(userIds).removeUserId(excludeUserId);
    }

    /**
     * Exclude userId
     *
     * @param excludeUserId User ID to be excluded
     * @return this
     */
    default RangeBroadcast removeUserId(long excludeUserId) {
        if (excludeUserId > 0) {
            this.listUserId().remove(excludeUserId);
        }

        return this;
    }

    private DataCodec codec() {
        return DataCodecManager.getDataCodec();
    }

    /**
     * Set the broadcast payload from an {@code int} value, encoding it via the configured codec.
     *
     * @param data the integer value to broadcast
     * @return this instance for chaining
     */
    default RangeBroadcast setData(int data) {
        this.setOriginal(data);
        return this.setData(codec().encode(data));
    }

    /**
     * Set the broadcast payload from a {@code boolean} value, encoding it via the configured codec.
     *
     * @param data the boolean value to broadcast
     * @return this instance for chaining
     */
    default RangeBroadcast setData(boolean data) {
        this.setOriginal(data);
        return this.setData(codec().encode(data));
    }

    /**
     * Set the broadcast payload from a {@code long} value, encoding it via the configured codec.
     *
     * @param data the long value to broadcast
     * @return this instance for chaining
     */
    default RangeBroadcast setData(long data) {
        this.setOriginal(data);
        return this.setData(codec().encode(data));
    }

    /**
     * Set the broadcast payload from a {@link String} value, encoding it via the configured codec.
     *
     * @param data the string value to broadcast (must not be null)
     * @return this instance for chaining
     */
    default RangeBroadcast setData(String data) {
        Objects.requireNonNull(data);
        this.setOriginal(data);
        return this.setData(codec().encode(data));
    }

    /**
     * Set the broadcast payload from a business object, encoding it via the configured codec.
     *
     * @param data the business data object to broadcast (must not be null)
     * @return this instance for chaining
     */
    default RangeBroadcast setData(Object data) {
        Objects.requireNonNull(data);
        this.setOriginal(data);
        return this.setData(codec().encode(data));
    }

    /**
     * Set the broadcast payload from a collection of business objects, encoding it via the configured codec.
     *
     * @param dataList the collection of objects to broadcast
     * @return this instance for chaining
     */
    default RangeBroadcast setData(Collection<?> dataList) {
        this.setOriginal(dataList);
        return this.setData(codec().encodeList(dataList));
    }

    /**
     * Set the broadcast payload from a list of {@link Integer} values, encoding it via the configured codec.
     *
     * @param dataList the list of integer values to broadcast
     * @return this instance for chaining
     */
    default RangeBroadcast setDataListInt(List<Integer> dataList) {
        this.setOriginal(dataList);
        return this.setData(codec().encodeListInt(dataList));
    }

    /**
     * Set the broadcast payload from a list of {@link Boolean} values, encoding it via the configured codec.
     *
     * @param dataList the list of boolean values to broadcast
     * @return this instance for chaining
     */
    default RangeBroadcast setDataListBool(List<Boolean> dataList) {
        this.setOriginal(dataList);
        return this.setData(codec().encodeListBool(dataList));
    }

    /**
     * Set the broadcast payload from a list of {@link Long} values, encoding it via the configured codec.
     *
     * @param dataList the list of long values to broadcast
     * @return this instance for chaining
     */
    default RangeBroadcast setDataListLong(List<Long> dataList) {
        this.setOriginal(dataList);
        return this.setData(codec().encodeListLong(dataList));
    }

    /**
     * Set the broadcast payload from a list of {@link String} values, encoding it via the configured codec.
     *
     * @param dataList the list of string values to broadcast
     * @return this instance for chaining
     */
    default RangeBroadcast setDataListString(List<String> dataList) {
        this.setOriginal(dataList);
        return this.setData(codec().encodeListString(dataList));
    }
}
