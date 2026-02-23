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
package com.iohao.net.extension.client.command;

import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.core.kit.CmdKit;
import com.iohao.net.common.kit.StrKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * Simulated client input command configuration.
 * example:
 * <pre>{@code
 *         ofCommand(DemoCmd.here).setTitle("here").setRequestData(() -> {
 *             YourMsg msg = ...
 *             return msg;
 *         }).callback(result -> {
 *              HelloReq value = result.getValue(HelloReq.class);
 *              log.info("value : {}", value);
 *          });
 *
 *         ofCommand(DemoCmd.list).setTitle("list").callback(result -> {
 *             // get list data
 *             List<HelloReq> list = result.listValue(HelloReq.class);
 *             log.info("list : {}", list);
 *         });
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-07-08
 */
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InputCommand {

    final String inputName;
    @Getter
    final CmdInfo cmdInfo;

    /** Display description for the simulated request command. */
    String title = "... ...";
    /** Prefix label displayed before the command title. */
    String cmdName = "";

    /** Request payload supplier. */
    RequestDataDelegate requestData;
    /** Response callback. */
    @Setter(AccessLevel.PRIVATE)
    CallbackDelegate callback;

    public InputCommand(CmdInfo cmdInfo) {
        this.inputName = CmdKit.toSimpleString(cmdInfo);
        this.cmdInfo = cmdInfo;
    }

    public InputCommand setRequestData(RequestDataDelegate requestData) {
        this.requestData = requestData;
        return this;
    }

    public InputCommand callback(CallbackDelegate callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public String toString() {
        int width = 7;
        String inputNameFormat = "%-" + width + "s";

        if (StrKit.isEmpty(cmdName)) {
            var format = inputNameFormat + ": %s";
            return String.format(format, inputName, title);
        }

        var format = inputNameFormat + ": [%s] - %s";
        return String.format(format, inputName, cmdName, title);
    }
}
