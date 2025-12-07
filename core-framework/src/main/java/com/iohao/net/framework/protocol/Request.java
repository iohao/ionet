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

/**
 *
 * @author 渔民小镇
 * @date 2025-09-15
 * @since 25.1
 */
public interface Request extends RemoteMessage, UserIdentity {
    int getHopCount();

    void setHopCount(int hopCount);

    void setBindingLogicServerIds(int[] bindingLogicServerIds);

    /**
     * The IDs of multiple game logic servers bound to the player
     * <pre>
     *     All requests related to this game logic server will be routed to the bound game logic server for processing.
     *     Even if multiple game logic servers of the same type are running, requests will still be directed to the originally bound server.
     * </pre>
     *
     * @return bindingLogicServerIds
     */
    int[] getBindingLogicServerIds();

    void setAttachment(byte[] attachment);

    /**
     * Extended field. Developers can use this field to extend meta-information for special business needs.
     * The data in this field will be included with every request.
     *
     * @return AttachmentData
     */
    byte[] getAttachment();

    default void setStick(int stick) {
    }

    default int getStick() {
        return 0;
    }
}
