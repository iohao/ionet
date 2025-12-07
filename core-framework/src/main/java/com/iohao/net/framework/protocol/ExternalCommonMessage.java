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
 *
 * @author 渔民小镇
 * @date 2025-09-10
 * @since 25.1
 */
@Getter
@Setter
sealed class ExternalCommonMessage permits ExternalRequestMessage, ExternalResponseMessage {
    long futureId;
    byte[] payload;
    int externalServerId;
}

interface ExternalPayloadSetting {
    void setPayload(byte[] payload);

    default void setPayload(int payload) {
        this.setPayload(ByteKit.toBytes(payload));
    }

    default void setPayload(long payload) {
        this.setPayload(ByteKit.toBytes(payload));
    }

    default void setPayload(boolean payload) {
        this.setPayload(ByteKit.toBytes(payload));
    }

    default void setPayload(String payload) {
        this.setPayload(ByteKit.toBytes(payload));
    }
}

interface ExternalPayloadGetting {
    byte[] getPayload();

    default int getPayloadAsInt() {
        return ByteKit.getInt(this.getPayload());
    }

    default long getPayloadAsLong() {
        return ByteKit.getLong(this.getPayload());
    }

    default boolean getPayloadAsBool() {
        return ByteKit.getBoolean(this.getPayload());
    }

    default String getPayloadAsString() {
        return ByteKit.getString(this.getPayload());
    }
}