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

import lombok.experimental.*;

/**
 * Runtime environment utilities.
 *
 * @author 渔民小镇
 * @date 2024-05-01
 * @since 21.7
 */
@UtilityClass
public class RuntimeKit {
    /** Number of available processors reported by the runtime. */
    public int availableProcessors = Runtime.getRuntime().availableProcessors();

    /**
     * The largest power of 2 that does not exceed {@link #availableProcessors}.
     * <p>
     * For example, when {@code availableProcessors} is 4, 8, 12, 16, or 32,
     * the corresponding value is 4, 8, 8, 16, or 32.
     */
    public int availableProcessors2n = availableProcessors2n();

    /**
     * Round down {@link #availableProcessors} to the nearest power of 2.
     * <p>
     * Uses bit-smearing to fill all bits below the highest set bit,
     * then shifts right by 1 to obtain the largest power of 2 &le; n.
     *
     * @return the largest power of 2 not exceeding the available processor count
     */
    static int availableProcessors2n() {
        int n = RuntimeKit.availableProcessors;
        // Smear the highest set bit into all lower bits
        n |= (n >> 1);
        n |= (n >> 2);
        n |= (n >> 4);
        n |= (n >> 8);
        n |= (n >> 16);
        // n is now (next power of 2) - 1; shift right to get the floor power of 2
        return (n + 1) >> 1;
    }
}
