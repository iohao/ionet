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
 * ResponseCollect
 *
 * @author 渔民小镇
 * @date 2025-09-16
 * @since 25.1
 */
public interface ResponseCollect {
    List<Response> getResponseList();

    /**
     * Code: 0 for success, others for errors.
     *
     * @return errorCode
     */
    int getErrorCode();

    /**
     * Code: 0 for success, others for errors.
     *
     * @param errorCode errorCode
     */
    void setErrorCode(int errorCode);

    String getErrorMessage();

    void setErrorMessage(String errorMessage);

    default void setError(ErrorInformation error) {
        this.setErrorCode(error.getCode());
        this.setErrorMessage(error.getMessage());
    }

    default boolean isSuccess() {
        return getErrorCode() == 0;
    }
}
