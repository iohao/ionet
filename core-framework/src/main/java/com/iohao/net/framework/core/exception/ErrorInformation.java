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
package com.iohao.net.framework.core.exception;

import java.util.*;
import org.jspecify.annotations.*;

/**
 * Contract for error information that can be used to throw {@link MessageException}s
 * with assertion-style convenience methods.
 * <p>
 * Implementations provide a numeric {@link #getCode() code} and a human-readable
 * {@link #getMessage() message}. The default methods offer a rich set of assertion
 * helpers that throw {@link MessageException} when the condition is not met.
 *
 * @author 渔民小镇
 * @date 2022-01-14
 */
public interface ErrorInformation {
    /**
     * Return the human-readable error message.
     *
     * @return the error message
     */
    String getMessage();

    /**
     * Return the numeric error code.
     *
     * @return the error code
     */
    int getCode();

    /**
     * Throws an exception if the asserted value is true
     *
     * @param v1 The assertion value
     * @throws MessageException e
     */
    default void assertTrueThrows(boolean v1) throws MessageException {
        if (v1) {
            throw new MessageException(this);
        }
    }

    /**
     * Throws an exception if the asserted value is true
     *
     * @param v1  The assertion value
     * @param msg Custom message
     * @throws MessageException e
     */
    default void assertTrueThrows(boolean v1, String msg) throws MessageException {
        if (v1) {
            int code = this.getCode();
            throw new MessageException(code, msg);
        }
    }

    /**
     * Asserted value cannot be null; throws an exception if it is null
     *
     * @param value The assertion value
     * @param msg   Custom message
     * @throws MessageException e
     */
    default void assertNonNull(Object value, String msg) throws MessageException {
        assertTrue(Objects.nonNull(value), msg);
    }

    /**
     * Asserted value cannot be null; throws an exception if it is null
     *
     * @param value The assertion value
     * @throws MessageException e
     */
    default void assertNonNull(Object value) throws MessageException {
        assertTrue(Objects.nonNull(value));
    }

    /**
     * Throws an exception if the asserted value is null
     *
     * @param value The assertion value
     * @throws MessageException e
     */
    default void assertNullThrows(Object value) throws MessageException {
        assertTrueThrows(value == null);
    }

    /**
     * Asserts that the assertion value is null, otherwise throws an exception
     *
     * @param value The assertion value
     * @param msg   Custom message
     * @throws MessageException e
     */
    default void assertNullThrows(Object value, String msg) throws MessageException {
        assertTrueThrows(value == null, msg);
    }

    /**
     * Asserts that the value must be true, throws an exception if the assertion is false
     *
     * @param v1 The assertion value
     * @throws MessageException e
     */
    default void assertTrue(boolean v1) throws MessageException {
        if (v1) {
            return;
        }

        throw new MessageException(this);
    }


    /**
     * Asserts that the value must be false, throws an exception if the assertion is true
     *
     * @param v1 The assertion value
     * @throws MessageException e
     */
    default void assertFalse(boolean v1) throws MessageException {
        this.assertTrue(!v1);
    }

    /**
     * Asserts that the value must be false, throws an exception if the assertion is true
     *
     * @param v1  The assertion value
     * @param msg Custom message
     * @throws MessageException e
     */
    default void assertFalse(boolean v1, String msg) throws MessageException {
        this.assertTrue(!v1, msg);
    }


    /**
     * Asserts that the value must be true, throws an exception if the assertion is false
     *
     * @param v1  The assertion value
     * @param msg Custom message
     * @throws MessageException e
     */
    default void assertTrue(boolean v1, String msg) throws MessageException {
        if (v1) {
            return;
        }

        var code = this.getCode();
        throw new MessageException(code, msg);
    }

    /**
     * Parameter cannot be null; throws an exception if the parameter is null
     *
     * @param value value
     * @param <T>   T
     * @return T
     */
    @NonNull
    default <T> T require(T value) {
        if (value != null) {
            return value;
        }

        throw new MessageException(this);
    }
}
