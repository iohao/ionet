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

import com.iohao.net.framework.CoreGlobalConfig;
import com.iohao.net.common.kit.ArrayKit;
import com.iohao.net.common.kit.exception.ThrowKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * BindingLogicServerMessage
 *
 * @author 渔民小镇
 * @date 2025-09-18
 * @since 25.1
 */
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class BindingLogicServerMessage {
    @Setter
    long futureId;
    int sourceNetId;
    @Setter
    int externalServerId;

    long[] userIds;
    int[] logicServerIds;
    BindingEnum operation;

    public BindingLogicServerMessage ofClone() {
        var message = new BindingLogicServerMessage();
        message.sourceNetId = this.sourceNetId;
        message.userIds = this.userIds;
        message.logicServerIds = this.logicServerIds;
        message.operation = this.operation;

        return message;
    }

    public static Builder builder(BindingEnum operation) {
        return new Builder(operation);
    }

    @Setter
    @Accessors(chain = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Builder {
        final Set<Long> userIdSet = new HashSet<>();
        final Set<Integer> logicServerIdSet = new HashSet<>();
        final int sourceNetId = CoreGlobalConfig.getNetId();
        final BindingEnum operation;

        int externalServerId;

        private Builder(BindingEnum operation) {
            Objects.requireNonNull(operation);
            this.operation = operation;
        }

        public BindingLogicServerMessage build() {


            if (this.userIdSet.isEmpty()) {
                ThrowKit.ofIllegalArgumentException("The userIdSet is empty!");
            }

            var message = new BindingLogicServerMessage();
            message.operation = this.operation;
            message.sourceNetId = this.sourceNetId;
            message.userIds = ArrayKit.toArrayLong(this.userIdSet);
            message.logicServerIds = ArrayKit.toArrayInt(this.logicServerIdSet);
            message.externalServerId = this.externalServerId;

            return message;
        }

        public Builder addUserId(long userId) {
            this.userIdSet.add(userId);
            return this;
        }

        public Builder addUserId(Collection<Long> userIds) {
            this.userIdSet.addAll(userIds);
            return this;
        }

        public Builder addUserId(long[] userIds) {
            for (long userId : userIds) {
                this.userIdSet.add(userId);
            }

            return this;
        }

        public Builder addLogicServerId(int logicServerId) {
            this.logicServerIdSet.add(logicServerId);
            return this;
        }

        public Builder addLogicServerId(Collection<Integer> logicServerIds) {
            this.logicServerIdSet.addAll(logicServerIds);
            return this;
        }

        public Builder addLogicServerId(int[] logicServerIds) {
            for (int logicServerId : logicServerIds) {
                this.logicServerIdSet.add(logicServerId);
            }

            return this;
        }
    }
}
