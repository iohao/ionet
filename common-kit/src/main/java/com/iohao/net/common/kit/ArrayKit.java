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

import java.util.*;
import java.util.stream.*;
import lombok.experimental.*;

/**
 * Array manipulation utilities.
 *
 * @author 渔民小镇
 * @date 2022-01-14
 */
@UtilityClass
public class ArrayKit {

    /**
     * Create a shallow copy of an int array.
     *
     * @param cards the source array to copy
     * @return a new array containing the same elements
     */
    public int[] copy(int[] cards) {
        int length = cards.length;
        int[] copyCards = new int[length];
        System.arraycopy(cards, 0, copyCards, 0, length);
        return copyCards;
    }

    /**
     * Join the string representations of array elements with a delimiter.
     *
     * @param array     the array of objects to join
     * @param delimiter the separator placed between each element
     * @return a single string of all elements separated by the delimiter
     */
    public String join(Object[] array, CharSequence delimiter) {
        return Arrays.stream(array)
                .map(Object::toString)
                .collect(Collectors.joining(delimiter));
    }

    /**
     * Check whether an Object array is non-null and contains at least one element.
     *
     * @param array the array to check, may be null
     * @return {@code true} if the array is non-null and non-empty
     */
    public boolean notEmpty(Object[] array) {
        return !isEmpty(array);
    }

    /**
     * Check whether a byte array is non-null and contains at least one element.
     *
     * @param array the array to check, may be null
     * @return {@code true} if the array is non-null and non-empty
     */
    public boolean notEmpty(byte[] array) {
        return !isEmpty(array);
    }

    /**
     * Check whether an int array is non-null and contains at least one element.
     *
     * @param array the array to check, may be null
     * @return {@code true} if the array is non-null and non-empty
     */
    public boolean notEmpty(int[] array) {
        return !isEmpty(array);
    }

    /**
     * Check whether a long array is non-null and contains at least one element.
     *
     * @param array the array to check, may be null
     * @return {@code true} if the array is non-null and non-empty
     */
    public boolean notEmpty(long[] array) {
        return !isEmpty(array);
    }

    /**
     * Check whether a byte array is null or empty.
     *
     * @param array the array to check, may be null
     * @return {@code true} if the array is null or has zero length
     */
    public boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Check whether an int array is null or empty.
     *
     * @param array the array to check, may be null
     * @return {@code true} if the array is null or has zero length
     */
    public boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Check whether a long array is null or empty.
     *
     * @param array the array to check, may be null
     * @return {@code true} if the array is null or has zero length
     */
    public boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Check whether an Object array is null or empty.
     *
     * @param array the array to check, may be null
     * @return {@code true} if the array is null or has zero length
     */
    public boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Convert a collection of Long values to a primitive long array.
     *
     * @param dataCollection the collection of Long values, may be null or empty
     * @return a primitive long array, or an empty array if the collection is null or empty
     */
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

    /**
     * Convert a collection of Integer values to a primitive int array.
     *
     * @param dataCollection the collection of Integer values, may be null or empty
     * @return a primitive int array, or an empty array if the collection is null or empty
     */
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

    /**
     * Convert a primitive long array to a mutable list of Long values.
     *
     * @param dataArray the primitive long array, may be null or empty
     * @return a new mutable list containing the boxed values, or an empty list if the array is null or empty
     */
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

    /**
     * Convert a primitive int array to a mutable list of Integer values.
     *
     * @param dataArray the primitive int array, may be null or empty
     * @return a new mutable list containing the boxed values, or an empty list if the array is null or empty
     */
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

    /**
     * Convert a primitive int array to a mutable set of Integer values.
     *
     * @param dataArray the primitive int array, may be null or empty
     * @return a new mutable set containing the boxed values, or an empty set if the array is null or empty
     */
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
