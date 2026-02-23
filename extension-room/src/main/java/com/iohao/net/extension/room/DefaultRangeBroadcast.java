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

import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.communication.CommunicationKit;
import com.iohao.net.framework.communication.RangeBroadcast;
import com.iohao.net.framework.protocol.BroadcastUserListMessage;
import com.iohao.net.common.kit.ArrayKit;
import com.iohao.net.common.kit.exception.ThrowKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import com.iohao.net.common.kit.CollKit;

import java.util.Set;

/// Broadcasts to the specified list of users.
/// ```java
/// static void main(){
///// data can be: int, long, boolean, String, Object
///     var data = 1;
///     new DefaultRangeBroadcast(CmdInfo.of(1, 1))
///             .addUserId(1L)
///             .addUserId(2L)
///             .addUserId(Set.of(3L, 4L))
///             .setData(data)
///             .execute();
///
///// data list, supports the following List<int, long, boolean, String, Object>
///     new DefaultRangeBroadcast(CmdInfo.of(1, 1))
///             .addUserId(1L)
///             .setDataListInt(List.of(1))
///             .setDataListLong(List.of(1L))
///             .setDataListBool(List.of(true))
///             .setDataListString(List.of("hello"))
///             .setData(List.of(new Student()))
///             .execute();
///}
///```
///
/// @author 渔民小镇
/// @date 2024-04-23
/// @since 21.8
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
class DefaultRangeBroadcast implements RangeBroadcast {
    @Getter(AccessLevel.PROTECTED)
    final Set<Long> userIdSet = CollKit.ofConcurrentSet();
    boolean doBroadcast = true;
    /** When true, the user id set must contain at least one target user. */
    boolean checkEmptyUser = false;
    byte[] data;
    final CmdInfo cmdInfo;
    Object originalData;

    DefaultRangeBroadcast(CmdInfo cmdInfo) {
        this.cmdInfo = cmdInfo;
    }

    public RangeBroadcast enableEmptyUserCheck() {
        this.checkEmptyUser = false;
        return this;
    }

    @Override
    public Set<Long> listUserId() {
        return this.userIdSet;
    }

    @Override
    public RangeBroadcast setData(byte[] data) {
        this.data = data;
        return this;
    }

    @Override
    public void setOriginal(Object originalData) {
        if (this.originalData == null) {
            this.originalData = originalData;
        }
    }

    public final void execute() {
        this.logic();

        if (!this.doBroadcast) {
            return;
        }

        this.trick();

        this.broadcast();
    }

    protected void logic() {
    }

    protected void trick() {
    }

    protected void disableBroadcast() {
        this.doBroadcast = false;
    }

    protected void broadcast() {
        boolean emptyUser = this.userIdSet.isEmpty();
        if (checkEmptyUser && emptyUser) {
            ThrowKit.ofRuntimeException("Please add a message sender");
        }

        if (emptyUser) {
            return;
        }

        var userIds = ArrayKit.toArrayLong(userIdSet);

        var message = new BroadcastUserListMessage();
        message.setUserIds(userIds);
        message.setCmdMerge(cmdInfo.cmdMerge());
        message.setOriginalData(originalData);
        message.setData(data);

        CommunicationKit.getCommunicationAggregation().broadcast(message);
    }
}
