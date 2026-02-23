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
package com.iohao.net.extension.client;

import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.core.kit.CmdKit;
import com.iohao.net.framework.protocol.wrapper.IntValue;
import com.iohao.net.framework.protocol.wrapper.LongValue;
import com.iohao.net.framework.protocol.wrapper.StringValue;
import com.iohao.net.common.kit.exception.ThrowKit;
import com.iohao.net.extension.client.command.InputCommand;
import com.iohao.net.extension.client.command.RequestDataDelegate;
import com.iohao.net.extension.client.kit.AssertKit;
import com.iohao.net.extension.client.kit.ClientUserConfigs;
import com.iohao.net.extension.client.kit.ScannerKit;
import com.iohao.net.extension.client.user.ClientUserInputCommands;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Helper for creating simulated input commands within a module command region.
 *
 * @author 渔民小镇
 * @date 2023-07-09
 */
@Slf4j
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PUBLIC)
public class InputCommandCreate {
    int cmd = -1;
    /** When true, only one {@link InputCommand} can exist for the same route. */
    boolean uniqueInputCommand = ClientUserConfigs.uniqueInputCommand;
    /** Prefix used in the module command description. */
    String cmdName = "";

    ClientUserInputCommands clientUserInputCommands;

    public CmdInfo ofCmdInfo(int subCmd) {
        AssertKit.assertTrueThrow(cmd < 0, "cmd must be >= 0");
        return CmdInfo.of(cmd, subCmd);
    }

    public InputCommand getInputCommand(int subCmd) {
        CmdInfo cmdInfo = ofCmdInfo(subCmd);
        InputCommand inputCommand = clientUserInputCommands.getInputCommand(cmdInfo);
        Objects.requireNonNull(inputCommand, "No request configuration found for the route");
        return inputCommand;
    }

    /**
     * Creates a simulated command.
     *
     * @param subCmd sub-command route
     * @return InputCommand
     */
    public InputCommand ofInputCommand(int subCmd) {
        return ofInputCommand(subCmd, null);
    }

    private InputCommand ofInputCommand(int subCmd, RequestDataDelegate requestData) {

        CmdInfo cmdInfo = ofCmdInfo(subCmd);

        // Uniqueness check: verify whether a command with the same route already exists.
        extractedChecked(cmdInfo);

        return clientUserInputCommands.ofCommand(cmdInfo)
                .setCmdName(this.cmdName)
                .setRequestData(requestData);
    }

    private void extractedChecked(CmdInfo cmdInfo) {
        if (uniqueInputCommand) {
            var inputName = CmdKit.toSimpleString(cmdInfo);
            InputCommand inputCommand = clientUserInputCommands.getInputCommand(inputName);
            if (Objects.nonNull(inputCommand)) {
                // Duplicate route command exists.
                ThrowKit.ofRuntimeException("There are duplicate routing commands : " + cmdInfo);
            }
        }
    }

    /**
     * Creates a simulated command that reads a {@code long} request parameter from the console.
     *
     * @param subCmd sub-command route
     * @return InputCommand
     */
    public InputCommand ofInputCommandLong(int subCmd) {
        RequestDataDelegate requestData = nextParamLong("parameter");
        return ofInputCommand(subCmd, requestData);
    }

    /**
     * Creates a simulated command that reads a target userId ({@code long}) from the console.
     *
     * @param subCmd sub-command route
     * @return InputCommand
     */
    public InputCommand ofInputCommandUserId(int subCmd) {
        RequestDataDelegate requestData = nextParamLong("target userId");
        return ofInputCommand(subCmd, requestData);
    }

    public RequestDataDelegate nextParamLong(String paramTips) {
        return () -> {
            log.info("Please input {} | parameter type : long.class", paramTips);

            long longValue = ScannerKit.nextLong();
            return LongValue.of(longValue);
        };
    }

    public InputCommand ofInputCommandInt(int subCmd) {
        RequestDataDelegate requestData = nextParamInt("parameter");
        return ofInputCommand(subCmd, requestData);
    }

    public RequestDataDelegate nextParamInt(String paramTips) {
        return () -> {
            String info = "Please input {} | parameter type : int.class";
            log.info(info, paramTips);

            int intValue = ScannerKit.nextInt();
            return IntValue.of(intValue);
        };
    }

    public InputCommand ofInputCommandString(int subCmd) {
        RequestDataDelegate requestData = nextParamString("parameter");
        return ofInputCommand(subCmd, requestData);
    }

    public RequestDataDelegate nextParamString(String paramTips) {
        return () -> {
            String info = "Please input {} | parameter type : String.class";
            log.info(info, paramTips);
            String s = ScannerKit.nextLine();
            Objects.requireNonNull(s);

            return StringValue.of(s);
        };
    }
}
