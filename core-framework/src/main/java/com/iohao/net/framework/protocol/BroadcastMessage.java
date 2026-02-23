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

import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.core.kit.CmdKit;
import lombok.Getter;
import lombok.Setter;

/**
 * Sealed base class for all broadcast message types sent from logic servers to external servers.
 * <p>
 * Carries the merged command route key ({@code cmdMerge}), serialized payload data, and a
 * transient reference to the original unserialized data object. Permitted subclasses define
 * the broadcast scope: {@link BroadcastUserMessage} (single user), {@link BroadcastUserListMessage}
 * (explicit user list), and {@link BroadcastMulticastMessage} (all connected users).
 *
 * @author 渔民小镇
 * @date 2025-09-04
 * @since 25.1
 */
@Getter
@Setter
public abstract sealed class BroadcastMessage
        permits
        BroadcastUserMessage,
        BroadcastUserListMessage,
        BroadcastMulticastMessage {

    int cmdMerge;
    byte[] data;

    transient Object originalData;

    /**
     * Set the command route from a {@link CmdInfo} descriptor.
     *
     * @param cmdInfo the command info containing the merged route key
     */
    public void setCmdInfo(CmdInfo cmdInfo) {
        this.cmdMerge = cmdInfo.cmdMerge();
    }

    @Override
    public String toString() {
        return "BroadcastMessage{" +
                CmdKit.toSimpleString(CmdInfo.of(this.cmdMerge)) +
                ", originalData=" + originalData +
                '}';
    }
}
