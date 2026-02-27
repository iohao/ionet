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
package com.iohao.net.framework.core.flow.internal;

import com.iohao.net.framework.core.exception.*;
import com.iohao.net.framework.core.flow.*;
import lombok.extern.slf4j.*;

/**
 * Default exception processor that wraps exceptions into
 * {@link com.iohao.net.framework.core.exception.MessageException}.
 * <p>
 * If the thrown exception is already a {@link com.iohao.net.framework.core.exception.MessageException},
 * it is returned as-is. Otherwise, the exception is logged and wrapped with a generic system error code.
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
@Slf4j
public final class DefaultActionMethodExceptionProcess implements ActionMethodExceptionProcess {
    /**
     * Process the given exception and convert it to a {@link MessageException}.
     * <p>
     * If the exception is already a {@link MessageException}, it is returned directly.
     * Otherwise, the exception is logged and a new {@link MessageException} with a generic
     * system error code is returned.
     *
     * @param e the exception thrown during action method execution
     * @return a {@link MessageException} representing the error
     */
    @Override
    public MessageException processException(final Throwable e) {

        if (e instanceof MessageException messageException) {
            return messageException;
        }

        // Not a user-defined error; likely from a third-party library or an uncaught developer error
        log.error(e.getMessage(), e);

        return new MessageException(ActionErrorEnum.systemOtherErrCode);
    }


    private DefaultActionMethodExceptionProcess() {
    }

    /**
     * Return the singleton instance.
     *
     * @return the singleton {@code DefaultActionMethodExceptionProcess}
     */
    public static DefaultActionMethodExceptionProcess me() {
        return Holder.ME;
    }

    private static class Holder {
        static final DefaultActionMethodExceptionProcess ME = new DefaultActionMethodExceptionProcess();
    }
}
