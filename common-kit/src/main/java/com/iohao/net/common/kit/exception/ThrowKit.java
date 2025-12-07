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

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 实验性工具，仅限内部使用
 *
 * @author 渔民小镇
 * @date 2024-08-01
 * @since 21.14
 */
@Slf4j
@UtilityClass
public class ThrowKit {
    public void ofIllegalArgumentException(String message) throws CommonIllegalArgumentException {
        throw new CommonIllegalArgumentException(message);
    }

    public void ofIllegalArgumentException(String message, Exception e) throws CommonIllegalArgumentException {
        throw new CommonIllegalArgumentException(message, e);
    }

    public void ofRuntimeException(String message) throws CommonRuntimeException {
        throw new CommonRuntimeException(message);
    }

    public void ofRuntimeException(Throwable e) throws CommonRuntimeException {
        throw new CommonRuntimeException(e.getMessage(), e);
    }

    public void ofNullPointerException(String message) throws NullPointerException {
        throw new NullPointerException(message);
    }
}
