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
package com.iohao.net.common;

import java.util.*;
import java.util.function.*;

/**
 * Drains publisher queues with an optional fairness limit.
 *
 * @author 渔民小镇
 * @date 2026-05-01
 * @since 25.4
 */
final class PublisherDrainKit {
    private PublisherDrainKit() {
    }

    static boolean drain(Queue<Object> queue, int drainLimit, Consumer<Object> consumer) {
        var drained = 0;
        Object message;
        while (canDrain(drained, drainLimit) && (message = queue.poll()) != null) {
            drained++;
            consumer.accept(message);
        }

        return drained > 0;
    }

    private static boolean canDrain(int drained, int drainLimit) {
        return drainLimit <= 0 || drained < drainLimit;
    }
}
