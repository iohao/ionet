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
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.random.RandomGenerator;

/**
 * RandomKit
 *
 * @author 渔民小镇
 * @date 2022-07-14
 */
@UtilityClass
public class RandomKit {
    final RandomGenerator generator = RandomGenerator.getDefault();

    /**
     * Gets a random number within the specified range [0, limit)
     *
     * @param limit The limit of the random number range, exclusive.
     * @return The random number.
     */
    public int randomInt(int limit) {
        return generator.nextInt(limit);
    }

    /**
     * Gets a random number within the specified range
     *
     * @param min Minimum number (inclusive)
     * @param max Maximum number (exclusive)
     * @return The random number
     */
    public int randomInt(int min, int max) {
        return generator.nextInt(min, max);
    }

    /**
     * Gets a random number within the specified range [0, limit)
     *
     * @param limit The limit of the random number range, exclusive.
     * @return The random number
     * @since 21.23
     */
    public long randomLong(long limit) {
        return generator.nextLong(limit);
    }

    /**
     * Gets a random number within the specified range
     *
     * @param min Minimum number (inclusive)
     * @param max Maximum number (exclusive)
     * @return The random number
     * @since 21.23
     */
    public long randomLong(long min, long max) {
        return generator.nextLong(min, max);
    }

    /**
     * Gets a random number within the specified range
     *
     * @param start Starting value (inclusive)
     * @param end   Ending value (inclusive)
     * @return The random number
     */
    public int random(int start, int end) {
        return start + generator.nextInt(end - start + 1);
    }

    /**
     * Gets a random number within the specified range (0 ~ end)
     *
     * @param end Ending value (inclusive)
     * @return The random number
     */
    public int random(int end) {
        return generator.nextInt(end + 1);
    }

    /**
     * Gets a random number within the specified range
     *
     * @param start Starting value (inclusive)
     * @param end   Ending value (inclusive)
     * @return The random number
     * @since 21.23
     */
    public long random(long start, long end) {
        return start + generator.nextLong(end - start + 1);
    }

    /**
     * Gets a random number within the specified range (0 ~ end)
     *
     * @param end Ending value (inclusive)
     * @return The random number
     * @since 21.23
     */
    public long random(long end) {
        return generator.nextLong(end + 1);
    }

    /**
     * Randomly generates a boolean value
     *
     * @return The boolean value
     */
    public boolean randomBoolean() {
        return generator.nextBoolean();
    }

    public <T> T randomEle(List<T> list) {
        int size = list.size();
        return size == 1
                ? list.getFirst()
                : list.get(randomInt(size));
    }

    public <T> T randomEle(@NonNull T[] array) {
        return array.length == 1
                ? array[0]
                : array[randomInt(array.length)];
    }

    public int randomEle(int[] array) {
        return array.length == 1
                ? array[0]
                : array[randomInt(array.length)];
    }

    public double randomDouble() {
        return generator.nextDouble();
    }
}