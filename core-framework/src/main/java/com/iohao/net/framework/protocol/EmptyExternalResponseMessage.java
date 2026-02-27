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

import lombok.*;

/**
 * Lightweight external response that carries only error information and no payload.
 * <p>
 * Used when the external server needs to signal success or failure without
 * returning any business data (e.g. acknowledgement-only responses).
 *
 * @author 渔民小镇
 * @date 2025-09-18
 * @since 25.1
 */
@Setter
@Getter
public final class EmptyExternalResponseMessage implements CommonResponse, FutureMessage {
    /** Correlation id used to match this response to its originating request. */
    long futureId;

    /** Error code; 0 indicates success, non-zero indicates an error. */
    int errorCode;
    /** Human-readable error message; {@code null} when the response is successful. */
    String errorMessage;

    /**
     * Create an empty response pre-populated with the given future id.
     *
     * @param futureId the correlation id of the originating request
     * @return a new {@link EmptyExternalResponseMessage} instance
     */
    public static EmptyExternalResponseMessage of(long futureId) {
        var message = new EmptyExternalResponseMessage();
        message.futureId = futureId;
        return message;
    }
}
