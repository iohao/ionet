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

import com.iohao.net.framework.core.exception.ActionErrorEnum;
import com.iohao.net.framework.core.exception.ErrorInformation;

/**
 * SettingUserIdResult
 *
 * @param errorCode    errorCode
 * @param errorMessage If success is true, the exception is null.
 * @author 渔民小镇
 * @date 2024-10-18
 * @since 21.19
 */
public record SettingUserIdResult(int errorCode, String errorMessage) {

    public static final SettingUserIdResult SUCCESS = new SettingUserIdResult(0, null);
    public static final SettingUserIdResult ERROR = ofError(ActionErrorEnum.internalCommunicationError);

    public boolean success() {
        return errorCode == 0;
    }

    public static SettingUserIdResult ofError(String errorMessage) {
        return new SettingUserIdResult(ActionErrorEnum.validateErrCode.getCode(), errorMessage);
    }

    public static SettingUserIdResult ofError(ErrorInformation errorInformation) {
        return new SettingUserIdResult(errorInformation.getCode(), errorInformation.getMessage());
    }
}
