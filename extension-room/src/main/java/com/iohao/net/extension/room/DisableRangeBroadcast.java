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
package com.iohao.net.extension.room;

import com.iohao.net.framework.communication.RangeBroadcast;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * DisableRangeBroadcast
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
public final class DisableRangeBroadcast implements RangeBroadcast {

    @Override
    public Set<Long> listUserId() {
        return Collections.emptySet();
    }

    @Override
    public RangeBroadcast setData(byte[] data) {
        return this;
    }

    @Override
    public void setOriginal(Object originalData) {

    }

    @Override
    public void execute() {

    }

    @Override
    public RangeBroadcast addUserId(Collection<Long> userIds) {
        return this;
    }

    @Override
    public RangeBroadcast addUserId(long userId) {
        return this;
    }

    @Override
    public RangeBroadcast addUserId(Collection<Long> userIds, long excludeUserId) {
        return this;
    }

    @Override
    public RangeBroadcast removeUserId(long excludeUserId) {
        return this;
    }

    @Override
    public RangeBroadcast setData(int data) {
        return this;
    }

    @Override
    public RangeBroadcast setData(boolean data) {
        return this;
    }

    @Override
    public RangeBroadcast setData(long data) {
        return this;
    }

    @Override
    public RangeBroadcast setData(String data) {
        return this;
    }

    @Override
    public RangeBroadcast setData(Object data) {
        return this;
    }

    @Override
    public RangeBroadcast setData(Collection<?> dataList) {
        return this;
    }

    @Override
    public RangeBroadcast setDataListInt(List<Integer> dataList) {
        return this;
    }

    @Override
    public RangeBroadcast setDataListBool(List<Boolean> dataList) {
        return this;
    }

    @Override
    public RangeBroadcast setDataListLong(List<Long> dataList) {
        return this;
    }

    @Override
    public RangeBroadcast setDataListString(List<String> dataList) {
        return this;
    }

    private DisableRangeBroadcast() {
    }

    public static DisableRangeBroadcast me() {
        return Holder.ME;
    }

    private static class Holder {
        static final DisableRangeBroadcast ME = new DisableRangeBroadcast();
    }
}
