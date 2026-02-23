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
package com.iohao.net.framework.protocol;

import com.iohao.net.framework.core.exception.ErrorInformation;

import java.util.List;

/**
 * Aggregated response collected from multiple logic servers.
 * <p>
 * Holds a list of individual {@link Response} objects together with a
 * top-level error code and message that indicate the overall outcome of
 * the collect operation.
 *
 * @author 渔民小镇
 * @date 2025-09-16
 * @since 25.1
 */
public interface ResponseCollect {
    /**
     * Get the list of individual responses collected from logic servers.
     *
     * @return the response list
     */
    List<Response> getResponseList();

    /**
     * Get the overall error code. 0 indicates success; any other value indicates an error.
     *
     * @return the error code
     */
    int getErrorCode();

    /**
     * Set the overall error code. 0 indicates success; any other value indicates an error.
     *
     * @param errorCode the error code
     */
    void setErrorCode(int errorCode);

    /**
     * Get the human-readable error message.
     *
     * @return the error message, or {@code null} on success
     */
    String getErrorMessage();

    /**
     * Set the human-readable error message.
     *
     * @param errorMessage the error message
     */
    void setErrorMessage(String errorMessage);

    /**
     * Populate the error code and message from the given {@link ErrorInformation}.
     *
     * @param error the error information source
     */
    default void setError(ErrorInformation error) {
        this.setErrorCode(error.getCode());
        this.setErrorMessage(error.getMessage());
    }

    /**
     * Check whether the collect operation completed successfully.
     *
     * @return {@code true} if the error code is 0
     */
    default boolean isSuccess() {
        return getErrorCode() == 0;
    }
}
