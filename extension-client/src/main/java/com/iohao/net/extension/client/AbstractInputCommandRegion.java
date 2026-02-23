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
import com.iohao.net.extension.client.command.CallbackDelegate;
import com.iohao.net.extension.client.command.InputCommand;
import com.iohao.net.extension.client.command.ListenCommand;
import com.iohao.net.extension.client.command.RequestCommand;
import com.iohao.net.extension.client.user.ClientUser;
import com.iohao.net.extension.client.user.ClientUserChannel;
import com.iohao.net.extension.client.user.ClientUserInputCommands;
import lombok.Getter;

/**
 * Base implementation for grouping simulated client input commands by command region.
 *
 * @author 渔民小镇
 * @date 2023-07-10
 */
public abstract class AbstractInputCommandRegion implements InputCommandRegion {
    InputCommandCreate inputCommandCreate = new InputCommandCreate();
    @Getter
    ClientUser clientUser;

    public void setCmd(int cmd) {
        String simpleName = this.getClass().getSimpleName();
        setCmd(cmd, simpleName);
    }

    public void setCmd(int cmd, String cmdName) {
        inputCommandCreate.cmd = cmd;
        inputCommandCreate.cmdName = cmdName;
    }

    public void setUserId(long userId) {
        if (this.clientUser.getUserId() == 0) {
            this.clientUser.setUserId(userId);
            this.clientUser.callbackInputCommandRegion();
        }
    }

    public long getUserId() {
        return clientUser.getUserId();
    }

    @Override
    public void setClientUser(ClientUser clientUser) {
        this.clientUser = clientUser;

        ClientUserInputCommands clientUserInputCommands = clientUser.getClientUserInputCommands();
        inputCommandCreate.setClientUserInputCommands(clientUserInputCommands);
    }

    /**
     * Creates a simulated command
     *
     * @param subCmd Sub-route (sub-command)
     * @return InputCommand
     */
    protected InputCommand ofCommand(int subCmd) {
        return this.inputCommandCreate.ofInputCommand(subCmd);
    }

    protected InputCommand ofCommand(int subCmd, String title) {
        return this.inputCommandCreate.ofInputCommand(subCmd).setTitle(title);
    }

    protected void ofListen(CallbackDelegate callback, int subCmd, String title) {
        this.ofListen(subCmd)
                .setCallback(callback)
                .setTitle(title);
    }

    protected void ofListen(CallbackDelegate callback, CmdInfo cmd, String title) {
        this.ofListen(cmd.subCmd())
                .setCallback(callback)
                .setTitle(title);
    }

    private ListenCommand ofListen(int subCmd) {
        CmdInfo cmdInfo = inputCommandCreate.ofCmdInfo(subCmd);
        ListenCommand listenCommand = new ListenCommand(cmdInfo);

        ClientUserChannel clientUserChannel = this.clientUser.getClientUserChannel();
        clientUserChannel.addListen(listenCommand);

        return listenCommand;
    }

    /**
     * Creates a request command execution
     *
     * @param subCmd Sub-route (sub-command)
     * @return RequestCommand
     */
    public RequestCommand ofRequestCommand(int subCmd) {
        CmdInfo cmdInfo = this.inputCommandCreate.ofCmdInfo(subCmd);
        return this.ofRequestCommand(cmdInfo);
    }

    /**
     * Creates a request command execution
     *
     * @param cmdInfo Route information
     * @return RequestCommand
     */
    public RequestCommand ofRequestCommand(CmdInfo cmdInfo) {
        ClientUserInputCommands clientUserInputCommands = this.inputCommandCreate.clientUserInputCommands;
        return clientUserInputCommands.ofRequestCommand(cmdInfo);
    }

    public void executeCommand(int subCmd) {
        this.ofRequestCommand(subCmd).execute();
    }
}
