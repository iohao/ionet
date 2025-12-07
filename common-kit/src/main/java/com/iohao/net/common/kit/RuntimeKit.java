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

/**
 * RuntimeKit
 *
 * @author 渔民小镇
 * @date 2024-05-01
 * @since 21.7
 */
@UtilityClass
public class RuntimeKit {
    public int availableProcessors = Runtime.getRuntime().availableProcessors();

    /**
     * The number is a power of 2 that is not greater than Runtime.getRuntime().availableProcessors().
     * When the value of availableProcessors is 4, 8, 12, 16, or 32, the corresponding number is 4, 8, 8, 16, or 32.
     */
    public int availableProcessors2n = availableProcessors2n();

    static int availableProcessors2n() {
        int n = RuntimeKit.availableProcessors;
        n |= (n >> 1);
        n |= (n >> 2);
        n |= (n >> 4);
        n |= (n >> 8);
        n |= (n >> 16);
        return (n + 1) >> 1;
    }
}
