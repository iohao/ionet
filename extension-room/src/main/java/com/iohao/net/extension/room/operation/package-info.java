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
/**
 * Extension Module - Board Games, Room-Based Games - Extension of Gameplay Operations within a Room.
 * <p>
 * Gameplay Operation Business - Design Patterns: Strategy Pattern + Flyweight Pattern
 * <pre>
 * Strategy Pattern:
 * Define an interface and write two implementation classes that implement this interface,
 * allowing the use of one interface and then selecting the appropriate implementation class as needed.
 * Flyweight Pattern:
 * Maintains instances of the gameplay interface implementation class {@link com.iohao.net.extension.room.operation.OperationHandler}.
 *
 * Centralizes the management of the state of many "virtual" objects, reducing the number of runtime
 * object instances and saving memory.
 * </pre>
 * Usage Example - Configuring Gameplay Operations
 * <pre>{@code
 * // Create OperationFactory object (framework's built-in implementation)
 * OperationFactory factory = OperationFactory.of();
 *
 * // Configure gameplay operation
 * factory.mappingUser(1, new ShootOperationHandler());
 *
 * // If there are more operations, they can be configured continuously.
 * // Here we use pseudocode for a Mahjong example, configuring Chi, Peng, and Gang operations.
 * factory.mappingUser(10, new ChiOperationHandler());
 * factory.mappingUser(11, new PengOperationHandler());
 * factory.mappingUser(12, new GangOperationHandler());
 * }</pre>
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @since 21.8
 */
package com.iohao.net.extension.room.operation;