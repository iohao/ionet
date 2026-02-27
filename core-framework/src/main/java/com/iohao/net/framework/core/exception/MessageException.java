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

import java.io.*;
import lombok.*;

/**
 * Exception carrying an error code and message, thrown during action method processing
 * to signal business errors to the client.
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public class MessageException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4977523514509693190L;

    @Getter
    final int errorCode;
    ErrorInformation errorInformation;

    /**
     * Create with an error code and message.
     *
     * @param errorCode the error code to return to the client
     * @param message   the error message describing the failure
     */
    public MessageException(int errorCode, String message) {

        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Create from an {@link ErrorInformation} instance.
     *
     * @param errorInformation the error information containing code and message
     */
    public MessageException(ErrorInformation errorInformation) {
        this(errorInformation.getCode(), errorInformation.getMessage());
        this.errorInformation = errorInformation;
    }

    /**
     * Get the error information, lazily creating an internal instance if not set.
     *
     * @return the error information
     */
    public ErrorInformation getErrorInformation() {
        return this.errorInformation == null
                ? this.errorInformation = new InternalError(errorCode, getMessage())
                : this.errorInformation;
    }

    private record InternalError(int code, String message) implements ErrorInformation {
        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public int getCode() {
            return code;
        }
    }
}

