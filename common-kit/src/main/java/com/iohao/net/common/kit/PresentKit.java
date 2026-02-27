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

import java.util.function.*;
import lombok.experimental.*;

/**
 * Null-presence conditional execution utilities.
 *
 * @author 渔民小镇
 * @date 2023-06-01
 */
@UtilityClass
public class PresentKit {

    /**
     * If a value is null, performs the given action with the value,
     * otherwise does nothing.
     *
     * @param value  value
     * @param action the action to be performed, if a value is null
     */
    public void ifNull(Object value, Runnable action) {
        if (value == null) {
            action.run();
        }
    }

    /**
     * If a value is present, performs the given action with the value,
     * otherwise does nothing.
     *
     * @param value  value
     * @param action the action to be performed, if a value is present
     * @since 21.8
     */
    public <T> void ifPresent(T value, Consumer<T> action) {
        if (value != null) {
            action.accept(value);
        }
    }
}
