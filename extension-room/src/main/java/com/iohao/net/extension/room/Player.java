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

/**
 * Player
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @since 21.8
 */
public interface Player {
    /**
     * get userId
     *
     * @return userId User ID
     */
    long getUserId();

    /**
     * set userId
     *
     * @param userId userId User ID
     */
    void setUserId(long userId);

    /**
     * get RoomId
     *
     * @return Room ID
     */
    long getRoomId();

    /**
     * set RoomId
     *
     * @param roomId Room ID
     */
    void setRoomId(long roomId);

    /**
     * get user's position
     *
     * @return User's position
     */
    int getSeat();

    /**
     * set user's position
     *
     * @param seat User's position
     */
    void setSeat(int seat);

    /**
     * Is ready
     *
     * @return true - ready
     */
    boolean isReady();

    /**
     * set ready status
     *
     * @param ready true - ready
     */
    void setReady(boolean ready);

    /**
     * Is Robot
     *
     * @return true This user is a Robot
     * @since 21.23
     */
    default boolean isRobot() {
        return false;
    }
}
