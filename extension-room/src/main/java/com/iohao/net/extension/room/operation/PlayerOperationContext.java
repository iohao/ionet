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
package com.iohao.net.extension.room.operation;

import com.iohao.net.extension.room.*;
import com.iohao.net.framework.core.flow.*;

/**
 * Context - Interface for Player Gameplay Operation Context
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @see OperationHandler
 * @since 21.8
 */
public interface PlayerOperationContext {
    /**
     * get room
     *
     * @param <T> Room
     * @return room
     */
    <T extends Room> T getRoom();

    /**
     * get command data. The data required for specific gameplay operations, usually customized by developers
     * according to the game business
     *
     * @param <T> t
     * @return operation data
     */
    <T> T getCommand();

    /**
     * The FlowContext of the current player
     *
     * @return FlowContext
     */
    FlowContext getFlowContext();

    /**
     * get the userId of the current operating player
     *
     * @return userId
     */
    default long getUserId() {
        return this.getFlowContext().getUserId();
    }

    /**
     * get the current operating player
     *
     * @param <T> Player
     * @return the current operating player
     */
    default <T extends Player> T getPlayer() {
        Room room = this.getRoom();
        long userId = this.getUserId();
        return room.getPlayerById(userId);
    }

    /**
     * get room id
     *
     * @return room id
     */
    default long getRoomId() {
        return this.getRoom().getRoomId();
    }
}
