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
package com.iohao.net.server.balanced;

import com.iohao.net.common.kit.CommonConst;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Round-robin selector backed by an array snapshot.
 *
 * @author 渔民小镇
 * @date 2024-10-19
 * @since 21.19
 */
@SuppressWarnings("all")
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class RingElementSelector<T> implements ElementSelector<T> {
    final Object[] elements;
    final int size;
    final AtomicLong counter = new AtomicLong();

    public RingElementSelector(List<T> list) {
        this.size = list.size();

        if (size == 0) {
            this.elements = CommonConst.emptyObjects;
            return;
        }

        this.elements = new Object[this.size];
        list.toArray(this.elements);
    }

    @Override
    public T next() {
        if (size == 0) {
            return null;
        }

        return (T) elements[(int) (counter.getAndIncrement() % size)];
    }

    @Override
    public T get() {
        T next = next();

        if (Objects.isNull(next)) {
            throw new NullPointerException("RingElementSelector next is null, likely due to an empty element list.");
        }

        return next;
    }
}
