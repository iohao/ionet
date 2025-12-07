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

import com.iohao.net.common.kit.LocaleKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * ActionErrorEnum
 *
 * @author 渔民小镇
 * @date 2022-01-14
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ActionErrorEnum implements ErrorInformation {
    systemOtherErrCode(-1000, "系统其它错误"),
    validateErrCode(-1001, "参数验错误"),
    cmdInfoErrorCode(-1002, "路由错误"),
    idleErrorCode(-1003, "心跳超时相关"),
    verifyIdentity(-1004, "请先登录"),
    classNotExist(-1005, "class 不存在"),
    dataNotExist(-1006, "数据不存在"),
    forcedOffline(-1007, "强制用户下线"),
    findBindingLogicServerNotExist(-1008, "绑定的逻辑服不存在"),
    internalCommunicationError(-1009, "internalCommunicationError"),
    enterpriseFunction(-1010, "enterpriseFunction"),
    ;

    final int code;
    final String message;

    ActionErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return LocaleKit.isChina() ? message : name();
    }
}
