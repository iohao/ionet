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

/**
 * Interface for types that carry a unique operation code identifier.
 * <p>
 * Each implementation holds an integer code that is auto-assigned via a global atomic counter.
 *
 * @author 渔民小镇
 * @date 2025-01-08
 * @since 21.23
 */
public interface OperationCode {
    /**
     * Return the operation code assigned to this instance.
     *
     * @return the unique operation code
     */
    int getOperationCode();

    /**
     * Atomically get the current global code value and increment it for the next caller.
     *
     * @return the next available operation code
     */
    static int getAndIncrementCode() {
        return OperationCodeKit.codeAtomic.getAndIncrement();
    }
}
