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
package com.iohao.net.common.kit;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数组相关工具
 *
 * @author 渔民小镇
 * @date 2022-01-14
 */
@UtilityClass
public class ArrayKit {

    public int[] copy(int[] cards) {
        int length = cards.length;
        int[] copyCards = new int[length];
        System.arraycopy(cards, 0, copyCards, 0, length);
        return copyCards;
    }

    public String join(Object[] array, CharSequence delimiter) {
        return Arrays.stream(array)
                .map(Object::toString)
                .collect(Collectors.joining(delimiter));
    }

    public boolean notEmpty(Object[] array) {
        return !isEmpty(array);
    }

    public boolean notEmpty(byte[] array) {
        return !isEmpty(array);
    }

    public boolean notEmpty(int[] array) {
        return !isEmpty(array);
    }

    public boolean notEmpty(long[] array) {
        return !isEmpty(array);
    }

    public boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    public boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public long[] toArrayLong(Collection<Long> dataCollection) {
        if (CollKit.isEmpty(dataCollection)) {
            return CommonConst.emptyLongs;
        }

        var dataArray = new long[dataCollection.size()];

        int i = 0;
        for (var value : dataCollection) {
            dataArray[i++] = value;
        }

        return dataArray;
    }

    public int[] toArrayInt(Collection<Integer> dataCollection) {
        if (CollKit.isEmpty(dataCollection)) {
            return CommonConst.emptyInts;
        }

        var dataArray = new int[dataCollection.size()];

        int i = 0;
        for (var value : dataCollection) {
            dataArray[i++] = value;
        }

        return dataArray;
    }

    public List<Long> toList(long[] dataArray) {
        if (isEmpty(dataArray)) {
            return new ArrayList<>();
        }

        List<Long> list = new ArrayList<>(dataArray.length);
        for (var value : dataArray) {
            list.add(value);
        }

        return list;
    }

    public List<Integer> toList(int[] dataArray) {
        if (isEmpty(dataArray)) {
            return new ArrayList<>();
        }

        List<Integer> list = new ArrayList<>(dataArray.length);
        for (var value : dataArray) {
            list.add(value);
        }

        return list;
    }

    public Set<Integer> toSet(int[] dataArray) {
        if (isEmpty(dataArray)) {
            return new HashSet<>();
        }

        Set<Integer> set = new HashSet<>();
        for (int value : dataArray) {
            set.add(value);
        }

        return set;
    }
}
