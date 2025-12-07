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
import com.iohao.net.framework.core.flow.FlowContext;
import com.iohao.net.framework.communication.RangeBroadcast;
import com.iohao.net.common.kit.OperationCode;
import com.iohao.net.common.kit.PresentKit;
import com.iohao.net.common.kit.concurrent.TaskKit;
import com.iohao.net.extension.room.operation.OperationContext;
import com.iohao.net.extension.room.operation.OperationHandler;
import com.iohao.net.extension.room.operation.OperationService;
import com.iohao.net.extension.room.operation.SimpleOperationHandler;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Room
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @since 21.8
 */
@SuppressWarnings("unchecked")
public interface Room {
    /**
     * Users, including Robot.
     * key : userId
     *
     * @return Users
     */
    Map<Long, Player> getPlayerMap();

    /**
     * All real users
     * key : userId
     *
     * @return Real users
     * @since 21.23
     */
    Map<Long, Player> getRealPlayerMap();

    /**
     * All Robots
     * key : userId
     *
     * @return Robot Map
     * @since 21.23
     */
    Map<Long, Player> getRobotMap();

    /**
     * User positions. key: seat, value: userId
     *
     * @return User positions
     */
    Map<Integer, Long> getPlayerSeatMap();

    /**
     * get roomId
     *
     * @return roomId
     */
    long getRoomId();

    /**
     * set roomId
     *
     * @param roomId roomId
     */
    void setRoomId(long roomId);

    /**
     * get room space size
     *
     * @return Room space size. For example, 4 means the room can hold a maximum of 4 players.
     */
    int getSpaceSize();

    /**
     * set room space size
     *
     * @param spaceSize spaceSize
     */
    void setSpaceSize(int spaceSize);

    /**
     * Player list: All users, including Robot
     *
     * @param <T> Player
     * @return All players
     */
    default <T extends Player> Collection<T> listPlayer() {
        return (Collection<T>) this.getPlayerMap().values();
    }

    /**
     * stream players
     *
     * @return player Stream
     */
    default <T extends Player> Stream<T> streamPlayer() {
        return (Stream<T>) this.listPlayer().stream();
    }

    /**
     * Real player list: All real users (excluding Robots)
     *
     * @param <T> Player
     * @return All real players
     */
    default <T extends Player> Collection<T> listRealPlayer() {
        return (Collection<T>) this.getRealPlayerMap().values();
    }

    /**
     * stream real players
     *
     * @return player Stream
     */
    default <T extends Player> Stream<T> streamRealPlayer() {
        return (Stream<T>) listRealPlayer().stream();
    }

    /**
     * RobotList
     *
     * @param <T> Robot
     * @return RobotList
     */
    default <T extends Player> Collection<T> listRobot() {
        return (Collection<T>) this.getRobotMap().values();
    }

    /**
     * stream robots
     *
     * @return player Stream
     */
    default <T extends Player> Stream<T> streamRobot() {
        return (Stream<T>) this.listRobot().stream();
    }

    /**
     * userId Collection
     *
     * @return userId collection
     */
    default Collection<Long> listPlayerId() {
        return this.getPlayerMap().keySet();
    }

    default Collection<Long> listRealPlayerId() {
        return this.getRealPlayerMap().keySet();
    }

    /**
     * Robot UserId Collection
     *
     * @return userId collection
     */
    default Collection<Long> listRobotPlayerId() {
        return this.getRobotMap().keySet();
    }

    /**
     * Find player by userId
     *
     * @param userId userId
     * @param <T>    Player
     * @return Player
     */
    default <T extends Player> T getPlayerById(long userId) {
        return (T) this.getPlayerMap().get(userId);
    }

    /**
     * Check if the user exists in the room
     *
     * @param userId userId
     * @return true if exists
     */
    default boolean existUser(long userId) {
        return this.getPlayerMap().get(userId) != null;
    }

    /**
     * Find player by seat
     *
     * @param seat seat
     * @param <T>  Player
     * @return Player
     */
    default <T extends Player> T getPlayerBySeat(int seat) {

        var seatMap = this.getPlayerSeatMap();
        if (seatMap.containsKey(seat)) {
            long userId = seatMap.get(seat);
            return this.getPlayerById(userId);
        }

        return null;
    }

    /**
     * Add player to the room
     *
     * @param player Player
     */
    default void addPlayer(Player player) {
        player.setRoomId(this.getRoomId());
        var userId = player.getUserId();
        this.getPlayerMap().put(userId, player);
        this.getPlayerSeatMap().put(player.getSeat(), userId);

        if (player.isRobot()) {
            this.getRobotMap().put(userId, player);
        } else {
            this.getRealPlayerMap().put(userId, player);
        }
    }

    /**
     * Remove player
     *
     * @param player Player
     */
    default void removePlayer(Player player) {
        long userId = player.getUserId();
        this.getPlayerMap().remove(userId);
        this.getPlayerSeatMap().remove(player.getSeat());

        if (player.isRobot()) {
            this.getRobotMap().remove(userId);
        } else {
            this.getRealPlayerMap().remove(userId);
        }
    }

    /**
     * Executes the given action if the player is in the room, otherwise does nothing.
     *
     * @param userId userId
     * @param action The given action
     * @param <T>    t
     */
    default <T extends Player> void ifPlayerExist(long userId, Consumer<T> action) {
        T player = this.getPlayerById(userId);
        Optional.ofNullable(player).ifPresent(action);
    }

    /**
     * Executes the given action if the player is not in the room, otherwise does nothing.
     *
     * @param userId   userId
     * @param runnable The given action
     */
    default void ifPlayerNotExist(long userId, Runnable runnable) {
        var player = this.getPlayerById(userId);
        PresentKit.ifNull(player, runnable);
    }

    /**
     * Count the number of players in the room, including Robots
     *
     * @return Number of players
     */
    default int countPlayer() {
        return this.getPlayerMap().size();
    }

    /**
     * Count the number of real players in the room
     *
     * @return Number of real players
     */
    default int countRealPlayer() {
        return this.getRealPlayerMap().size();
    }

    /**
     * Count the number of Robots in the room
     *
     * @return Number of Robots
     */
    default int countRobot() {
        return this.getRobotMap().size();
    }

    /**
     * Check if the room has no players, including Robots
     *
     * @return true if the room has no players
     */
    default boolean isEmptyPlayer() {
        return this.getPlayerMap().isEmpty();
    }


    default boolean isEmptyRealPlayer() {
        return this.getRealPlayerMap().isEmpty();
    }

    /**
     * Check if the room has no Robots
     *
     * @return true if the room has no Robots
     */
    default boolean isEmptyRobot() {
        return this.getRealPlayerMap().isEmpty();
    }

    /**
     * Check if it is a Robot
     *
     * @param userId userId
     * @return true: Robot
     * @since 21.23
     */
    default boolean isRobot(long userId) {
        Player player = this.getPlayerById(userId);
        return Objects.nonNull(player) && player.isRobot();
    }

    /**
     * Check if it is a real user
     *
     * @param userId userId
     * @return true: real player
     * @since 21.23
     */
    default boolean isRealPlayer(long userId) {
        var player = this.getPlayerById(userId);
        return Objects.nonNull(player) && !player.isRobot();
    }

    /**
     * Get an empty seat number
     *
     * @return >=0 indicates there is an available seat
     */
    default int getEmptySeatNo() {
        return RoomKit.getEmptySeatNo(this);
    }

    /**
     * Check if there is still an empty seat
     *
     * @return true if there is still an empty seat
     * @since 21.23
     */
    default boolean hasSeat() {
        return this.getSpaceSize() > this.countPlayer();
    }

    /**
     * Check if all players are ready
     *
     * @return true if all players are ready
     */
    default boolean isReadyPlayers() {
        boolean notReady = this.streamPlayer().anyMatch(player -> !player.isReady());
        return !notReady;
    }

    /**
     * forEach players, the first argument is the userId
     *
     * @param action action
     */
    default void forEach(BiConsumer<Long, Player> action) {
        this.getPlayerMap().forEach(action);
    }

    /**
     * Executed in domain events, this method is thread-safe
     *
     * @param task task
     * @since 21.23
     */
    default void executeTask(Runnable task) {
        OperationContext.of(this, SimpleOperationHandler.me()).setCommand(task).send();
    }

    /**
     * Delayed execution of tasks, this method is thread-safe
     *
     * @param task              task
     * @param delayMilliseconds delayMilliseconds
     * @since 21.23
     */
    default void executeDelayTask(Runnable task, long delayMilliseconds) {
        TaskKit.runOnceMillis(() -> this.executeTask(task), delayMilliseconds);
    }

    /**
     * setOperationService
     *
     * @param operationService operationService
     * @since 21.28
     */
    default void setOperationService(OperationService operationService) {
    }

    /**
     * getOperationService
     *
     * @return OperationService
     * @since 21.28
     */
    default OperationService getOperationService() {
        return null;
    }

    /**
     * get OperationHandler by OperationCode
     *
     * @param operationCode operationCode
     * @return OperationHandler
     * @since 21.28
     */
    default OperationHandler getOperationHandler(OperationCode operationCode) {
        return this.getOperationService().getOperationHandler(operationCode);
    }

    /**
     * create OperationContext
     *
     * @param operationHandler operationHandler
     * @return OperationContext operationHandler
     * @since 21.23
     */
    default OperationContext ofOperationContext(OperationHandler operationHandler) {
        return OperationContext.of(this, operationHandler);
    }

    /**
     * execute operation
     *
     * @param operationCode operationCode
     * @since 21.28
     */
    default void operation(OperationCode operationCode) {
        var operationHandler = this.getOperationHandler(operationCode);
        var flowContext = RoomKit.ofFlowContext(0);
        this.operation(operationHandler, flowContext, null);
    }

    /**
     * execute operation
     *
     * @param operationCode operationCode
     * @param userId        userId
     * @since 21.28
     */
    default void operation(OperationCode operationCode, long userId) {
        var operationHandler = this.getOperationHandler(operationCode);
        var flowContext = RoomKit.ofFlowContext(userId);
        this.operation(operationHandler, flowContext, null);
    }

    /**
     * execute operation
     *
     * @param operationCode  operationCode
     * @param userId         userId
     * @param commandMessage commandMessage
     * @since 21.28
     */
    default void operation(OperationCode operationCode, long userId, Object commandMessage) {
        var operationHandler = this.getOperationHandler(operationCode);
        var flowContext = RoomKit.ofFlowContext(userId);
        operation(operationHandler, flowContext, commandMessage);
    }

    /**
     * execute operation
     *
     * @param operationCode operationCode
     * @param flowContext   flowContext
     * @since 21.28
     */
    default void operation(OperationCode operationCode, FlowContext flowContext) {
        OperationHandler operationHandler = this.getOperationHandler(operationCode);
        this.operation(operationHandler, flowContext, null);
    }

    /**
     * execute operation
     *
     * @param operationCode  operationCode
     * @param flowContext    flowContext
     * @param commandMessage commandMessage
     * @since 21.28
     */
    default void operation(OperationCode operationCode, FlowContext flowContext, Object commandMessage) {
        OperationHandler operationHandler = this.getOperationHandler(operationCode);
        this.operation(operationHandler, flowContext, commandMessage);
    }

    /**
     * execute operation
     *
     * @param operationHandler operationHandler
     * @param userId           userId
     * @param commandMessage   commandMessage
     * @since 21.28
     */
    default void operation(OperationHandler operationHandler, long userId, Object commandMessage) {
        var flowContext = RoomKit.ofFlowContext(userId);
        this.operation(operationHandler, flowContext, commandMessage);
    }

    /**
     * execute operation
     *
     * @param operationHandler operationHandler
     * @param flowContext      flowContext
     * @param commandMessage   commandMessage
     * @since 21.28
     */
    default void operation(OperationHandler operationHandler, FlowContext flowContext, Object commandMessage) {
        this.ofOperationContext(operationHandler)
                .setFlowContext(flowContext)
                .setCommand(commandMessage)
                .execute();
    }

    /**
     * execute operation
     *
     * @param operationHandler operationHandler
     * @param flowContext      flowContext
     * @since 25.1
     */
    default void operation(OperationHandler operationHandler, FlowContext flowContext) {
        this.operation(operationHandler, flowContext, null);
    }

    /**
     * Create a RangeBroadcast, which will add all players in the current room by default.
     *
     * @return RangeBroadcast The broadcast within the range
     */
    default RangeBroadcast ofRangeBroadcast(CmdInfo cmdInfo) {
        return this.ofEmptyRangeBroadcast(cmdInfo)
                .addUserId(this.listPlayerId());
    }

    /**
     * Create a RangeBroadcast
     *
     * @return RangeBroadcast The broadcast within the range
     */
    default RangeBroadcast ofEmptyRangeBroadcast(CmdInfo cmdInfo) {
        return new DefaultRangeBroadcast(cmdInfo);
    }
}