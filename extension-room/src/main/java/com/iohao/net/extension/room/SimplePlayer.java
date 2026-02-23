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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Basic mutable {@link Player} implementation for room-based games.
 *
 * @author 渔民小镇
 * @date 2024-05-12
 * @since 21.8
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class SimplePlayer implements Player {
    /** userId user id */
    long userId;
    /** Room id */
    long roomId;
    /** User's position (seat) */
    int seat;
    /** true - is ready */
    boolean ready;
    /** true - is robot */
    boolean robot;
    boolean maybeRobot;
}
