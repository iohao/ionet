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

import com.iohao.net.common.kit.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Built-in error codes used by the framework's action processing pipeline.
 * <p>
 * Each constant carries a numeric {@link #code} and a human-readable {@link #message}.
 * When the locale is Chinese the original message is returned; otherwise the enum
 * constant name is used.
 *
 * @author 渔民小镇
 * @date 2022-01-14
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ActionErrorEnum implements ErrorInformation {
    /** Generic system error. */
    systemOtherErrCode(-1000, "系统其它错误"),
    /** Parameter validation error. */
    validateErrCode(-1001, "参数验错误"),
    /** Routing / command info error. */
    cmdInfoErrorCode(-1002, "路由错误"),
    /** Heartbeat idle timeout. */
    idleErrorCode(-1003, "心跳超时相关"),
    /** User not logged in. */
    verifyIdentity(-1004, "请先登录"),
    /** Class does not exist. */
    classNotExist(-1005, "class 不存在"),
    /** Requested data does not exist. */
    dataNotExist(-1006, "数据不存在"),
    /** Force user offline. */
    forcedOffline(-1007, "强制用户下线"),
    /** Bound logic server not found. */
    findBindingLogicServerNotExist(-1008, "绑定的逻辑服不存在"),
    /** Internal inter-server communication error. */
    internalCommunicationError(-1009, "internalCommunicationError"),
    /** Enterprise-only feature. */
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
