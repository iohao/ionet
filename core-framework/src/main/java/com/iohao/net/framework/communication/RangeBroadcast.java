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
 * Broadcast within a range
 *
 * @author 渔民小镇
 * @date 2024-06-02
 * @since 21.9
 */
public interface RangeBroadcast {
    /**
     * Users receiving the broadcast
     *
     * @return Users receiving the broadcast
     */
    Set<Long> listUserId();

    RangeBroadcast setData(byte[] data);

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
     * set data
     *
     * @param data data
     * @return this
     */
    default RangeBroadcast setData(int data) {
        this.setOriginal(data);
        return this.setData(codec().encode(data));
    }

    /**
     * set data
     *
     * @param data data
     * @return this
     */
    default RangeBroadcast setData(boolean data) {
        this.setOriginal(data);
        return this.setData(codec().encode(data));
    }

    /**
     * set data
     *
     * @param data data
     * @return this
     */
    default RangeBroadcast setData(long data) {
        this.setOriginal(data);
        return this.setData(codec().encode(data));
    }

    /**
     * set data
     *
     * @param data data
     * @return this
     */
    default RangeBroadcast setData(String data) {
        Objects.requireNonNull(data);
        this.setOriginal(data);
        return this.setData(codec().encode(data));
    }

    /**
     * Set the response broadcast data
     *
     * @param data Business data
     * @return this
     */
    default RangeBroadcast setData(Object data) {
        Objects.requireNonNull(data);
        this.setOriginal(data);
        return this.setData(codec().encode(data));
    }

    /**
     * set dataList
     *
     * @param dataList dataList
     * @return this
     */
    default RangeBroadcast setData(Collection<?> dataList) {
        this.setOriginal(dataList);
        return this.setData(codec().encodeList(dataList));
    }

    /**
     * set dataList
     *
     * @param dataList dataList
     * @return this
     */
    default RangeBroadcast setDataListInt(List<Integer> dataList) {
        this.setOriginal(dataList);
        return this.setData(codec().encodeListInt(dataList));
    }

    /**
     * set dataList
     *
     * @param dataList dataList
     * @return this
     */
    default RangeBroadcast setDataListBool(List<Boolean> dataList) {
        this.setOriginal(dataList);
        return this.setData(codec().encodeListBool(dataList));
    }

    /**
     * set dataList
     *
     * @param dataList dataList
     * @return this
     */
    default RangeBroadcast setDataListLong(List<Long> dataList) {
        this.setOriginal(dataList);
        return this.setData(codec().encodeListLong(dataList));
    }

    /**
     * set dataList
     *
     * @param dataList dataList
     * @return this
     */
    default RangeBroadcast setDataListString(List<String> dataList) {
        this.setOriginal(dataList);
        return this.setData(codec().encodeListString(dataList));
    }
}
