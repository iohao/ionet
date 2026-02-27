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
package com.iohao.net.common.kit.exception;

import lombok.experimental.*;
import lombok.extern.slf4j.*;

/**
 * Exception throwing utilities for internal use.
 *
 * @author 渔民小镇
 * @date 2024-08-01
 * @since 21.14
 */
@Slf4j
@UtilityClass
public class ThrowKit {
    /**
     * Throw a {@link CommonIllegalArgumentException} with the given message.
     *
     * @param message the detail message
     * @throws CommonIllegalArgumentException always
     */
    public void ofIllegalArgumentException(String message) throws CommonIllegalArgumentException {
        throw new CommonIllegalArgumentException(message);
    }

    /**
     * Throw a {@link CommonIllegalArgumentException} with the given message and cause.
     *
     * @param message the detail message
     * @param e       the cause
     * @throws CommonIllegalArgumentException always
     */
    public void ofIllegalArgumentException(String message, Exception e) throws CommonIllegalArgumentException {
        throw new CommonIllegalArgumentException(message, e);
    }

    /**
     * Throw a {@link CommonRuntimeException} with the given message.
     *
     * @param message the detail message
     * @throws CommonRuntimeException always
     */
    public void ofRuntimeException(String message) throws CommonRuntimeException {
        throw new CommonRuntimeException(message);
    }

    /**
     * Throw a {@link CommonRuntimeException} wrapping the given throwable.
     *
     * @param e the throwable to wrap
     * @throws CommonRuntimeException always
     */
    public void ofRuntimeException(Throwable e) throws CommonRuntimeException {
        throw new CommonRuntimeException(e.getMessage(), e);
    }

    /**
     * Throw a {@link NullPointerException} with the given message.
     *
     * @param message the detail message
     * @throws NullPointerException always
     */
    public void ofNullPointerException(String message) throws NullPointerException {
        throw new NullPointerException(message);
    }
}
