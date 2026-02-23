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

import com.iohao.net.common.kit.CollKit;
import com.iohao.net.extension.room.operation.OperationService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.TreeMap;

/**
 * Default in-memory {@link Room} implementation.
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @since 21.8
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimpleRoom implements Room {
    /** key: seat, value: userId */
    final Map<Integer, Long> playerSeatMap = new TreeMap<>();
    final Map<Long, Player> playerMap = CollKit.ofConcurrentHashMap();
    final Map<Long, Player> realPlayerMap = CollKit.ofConcurrentHashMap();
    final Map<Long, Player> robotMap = CollKit.ofConcurrentHashMap();
    OperationService operationService;
    long roomId;
    int spaceSize;
}
