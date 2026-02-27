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

import java.util.*;

/**
 * Extension interface for room management related operations
 * <p>
 * Built-in default implementation
 * <pre>{@code
 * RoomService roomService = RoomService.of();
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @since 21.8
 */
@SuppressWarnings("unchecked")
public interface RoomService {
    /**
     * Room map
     * <pre>
     * key : roomId
     * value : room
     * </pre>
     *
     * @return Room map
     */
    Map<Long, Room> getRoomMap();

    /**
     * User to room map
     * <pre>
     * key : userId
     * value : roomId
     * </pre>
     *
     * @return User to room map
     */
    Map<Long, Long> getUserRoomMap();

    /**
     * Find room by userId
     *
     * @param userId userId
     * @param <T>    Room
     * @return Room
     */
    default <T extends Room> T getRoomByUserId(long userId) {
        // Get roomId via userId
        Long roomId = this.getUserRoomMap().get(userId);

        if (roomId == null) {
            return null;
        }

        // Get room via roomId
        return getRoom(roomId);
    }

    /**
     * Find room by roomId
     *
     * @param roomId roomId
     * @param <T>    Room
     * @return Room
     */
    default <T extends Room> T getRoom(long roomId) {
        return (T) this.getRoomMap().get(roomId);
    }

    /**
     * Find room Optional by roomId
     *
     * @param roomId roomId
     * @param <T>    Room
     * @return Optional Room
     */
    default <T extends Room> Optional<T> optionalRoom(long roomId) {
        return Optional.ofNullable(this.getRoom(roomId));
    }

    /**
     * Find room Optional by userId
     *
     * @param userId userId
     * @param <T>    Room
     * @return Optional Room
     */
    default <T extends Room> Optional<T> optionalRoomByUserId(long userId) {
        return Optional.ofNullable(this.getRoomByUserId(userId));
    }

    /**
     * Add room
     *
     * @param room The room
     */
    default void addRoom(Room room) {
        long roomId = room.getRoomId();
        this.getRoomMap().put(roomId, room);
    }

    /**
     * Remove room
     *
     * @param room The room
     */
    default void removeRoom(Room room) {
        long roomId = room.getRoomId();
        this.getRoomMap().remove(roomId);
        // Remove the association between all players' userId and roomId
        room.listPlayerId().forEach(userId -> this.getUserRoomMap().remove(userId));
    }

    /**
     * Add user to the room and associate userId with roomId
     *
     * @param room   The room
     * @param player The user
     */
    default void addPlayer(Room room, Player player) {
        room.addPlayer(player);
        this.getUserRoomMap().put(player.getUserId(), room.getRoomId());
    }

    /**
     * Remove user from the room and delete the association between userId and roomId
     *
     * @param room   The room
     * @param player The user
     */
    default void removePlayer(Room room, Player player) {
        room.removePlayer(player);
        this.getUserRoomMap().remove(player.getUserId());
    }

    /**
     * Remove user from the room and delete the association between userId and roomId
     *
     * @param room   The room
     * @param userId userId
     */
    default void removePlayer(Room room, long userId) {
        room.ifPlayerExist(userId, player -> this.removePlayer(room, player));
    }

    /**
     * Get room list
     *
     * @param <T> Room
     * @return Room list
     */
    default <T extends Room> Collection<T> listRoom() {
        return (Collection<T>) this.getRoomMap().values();
    }

    /**
     * Create a RoomService object instance (built-in default implementation)
     *
     * @return RoomService
     */
    static RoomService of() {
        return new SimpleRoomService();
    }
}