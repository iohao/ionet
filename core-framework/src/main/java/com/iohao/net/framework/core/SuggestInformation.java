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
package com.iohao.net.framework.core;

import com.iohao.net.framework.IonetVersion;
import com.iohao.net.framework.i18n.Bundle;
import com.iohao.net.framework.i18n.MessageKey;
import com.iohao.net.common.kit.StrKit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * SuggestInformation
 *
 * @author 渔民小镇
 * @date 2025-10-13
 * @since 25.1
 */
@FieldDefaults(level = AccessLevel.PUBLIC)
public final class SuggestInformation {
    final Map<String, Object> paramMap = new HashMap<>();
    final ActionCommand command;

    public SuggestInformation(ActionCommand command) {
        this.command = command;
        paramMap.put("codeSuggestTitle", Bundle.getMessage(MessageKey.codeSuggestTitle));
        paramMap.put("IonetVersion", IonetVersion.VERSION);
        paramMap.put("lineNumber", command.actionCommandDoc.lineNumber);
        paramMap.put("className", command.actionControllerClass.getSimpleName());
        paramMap.put("actionMethodName", command.getActionMethodName());
        paramMap.put("paramInfo", Objects.requireNonNullElse(command.dataParameter, ""));

        var cmdInfo = command.cmdInfo;
        paramMap.put("cmdInfo", "[%d-%d]".formatted(cmdInfo.cmd(), cmdInfo.subCmd()));

        var actionMethodReturn = command.actionMethodReturn;
        paramMap.put("returnInfo", actionMethodReturn.toString());
    }

    public void see(String text) {
        var template = """
                ┏━━ {codeSuggestTitle}.({className}.java:{lineNumber}) ━━ [{returnInfo} {actionMethodName}({paramInfo})] ━━ {cmdInfo} ━━━━
                ┣ {text}
                ┗━━ [ionet:{IonetVersion}] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                """;

        paramMap.put("text", text);
        var message = StrKit.format(template, paramMap);
        System.out.println(message);
    }
}
