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

import com.iohao.net.framework.core.exception.ActionErrorEnum;
import com.iohao.net.framework.core.exception.MessageException;
import com.iohao.net.framework.core.flow.ActionMethodExceptionProcess;
import lombok.extern.slf4j.Slf4j;

/**
 * Exception handling for ActionMethod
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
@Slf4j
public final class DefaultActionMethodExceptionProcess implements ActionMethodExceptionProcess {
    @Override
    public MessageException processException(final Throwable e) {

        if (e instanceof MessageException messageException) {
            return messageException;
        }

        // 到这里，一般不是用户自定义的错误，很可能是开发者引入的第三方包或自身未捕获的错误等情况
        log.error(e.getMessage(), e);

        return new MessageException(ActionErrorEnum.systemOtherErrCode);
    }


    private DefaultActionMethodExceptionProcess() {
    }

    public static DefaultActionMethodExceptionProcess me() {
        return Holder.ME;
    }

    private static class Holder {
        static final DefaultActionMethodExceptionProcess ME = new DefaultActionMethodExceptionProcess();
    }
}
