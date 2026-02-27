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
package com.iohao.net.extension.client.user;

import com.iohao.net.common.kit.*;
import com.iohao.net.common.kit.concurrent.*;
import com.iohao.net.extension.client.command.*;
import com.iohao.net.extension.client.kit.*;
import com.iohao.net.framework.core.*;
import com.iohao.net.framework.core.kit.*;
import com.iohao.net.framework.i18n.*;
import com.iohao.net.framework.toy.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import lombok.*;
import lombok.extern.slf4j.*;

/**
 * Simulated command manager.
 *
 * @author 渔民小镇
 * @date 2023-07-08
 */
@Slf4j
public class ClientUserInputCommands {
    final AtomicBoolean starting = new AtomicBoolean();
    @Getter
    final ClientUserChannel clientUserChannel;

    Map<String, InputCommand> inputCommandMap = new LinkedHashMap<>();

    public ClientUserInputCommands(ClientUserChannel clientUserChannel) {
        this.clientUserChannel = clientUserChannel;
    }

    private void addCommand(InputCommand inputCommand) {

        Objects.requireNonNull(inputCommand);
        String inputName = inputCommand.getInputName();

        inputCommandMap.put(inputName, inputCommand);
    }

    public String toInputName(CmdInfo cmdInfo) {
        return CmdKit.toSimpleString(cmdInfo);
    }

    public InputCommand ofCommand(CmdInfo cmdInfo) {
        InputCommand inputCommand = new InputCommand(cmdInfo);
        addCommand(inputCommand);
        return inputCommand;
    }

    public InputCommand getInputCommand(String inputName) {
        return inputCommandMap.get(inputName);
    }

    public InputCommand getInputCommand(CmdInfo cmdInfo) {
        String inputName = toInputName(cmdInfo);
        return inputCommandMap.get(inputName);
    }

    public RequestCommand ofRequestCommand(CmdInfo cmdInfo) {
        InputCommand inputCommand = this.getInputCommand(cmdInfo);

        return new RequestCommand()
                .setClientUserChannel(this.clientUserChannel)
                .setTitle(inputCommand.getTitle())
                .setCmdMerge(inputCommand.getCmdInfo().cmdMerge())
                .setRequestData(inputCommand.getRequestData())
                .setCallback(inputCommand.getCallback())
                ;
    }

    void request(String inputName) {
        InputCommand inputCommand = this.getInputCommand(inputName);
        if (inputCommand == null) {
            var clientInputCommandNotExist = Bundle.getMessage(MessageKey.clientInputCommandNotExist);
            System.err.printf(clientInputCommandNotExist + "%n", inputName);
            return;
        }

        IonetBanner.println1(inputCommand);

        try {
            clientUserChannel.request(inputCommand);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    public void help() {
        IonetBanner.printlnMsg("---------- cmd help ----------");
        inputCommandMap.values().stream()
                .sorted(Comparator.comparingInt(command -> command.getCmdInfo().cmdMerge()))
                .forEach(inputCommand -> IonetBanner.printlnMsg(inputCommand.toString()));
    }

    public void listenHelp() {
        IonetBanner.printlnMsg("---------- Broadcast help ----------");
        clientUserChannel.getListenMap().values().stream()
                .sorted(Comparator.comparingInt(command -> command.getCmdInfo().cmdMerge()))
                .forEach(IonetBanner::println1);
    }

    public void start() {
        if (starting.get()) {
            return;
        }

        if (!starting.compareAndSet(false, true)) {
            return;
        }

        TaskKit.execute(this::extracted);
    }

    private void extracted() {

        if (ClientUserConfigs.closeScanner) {
            return;
        }

        String input = "";
        String lastInput = "+";

        while (!input.equalsIgnoreCase("q")) {
            var clientInput = Bundle.getMessage(MessageKey.clientInput);
            IonetBanner.printlnMsg(clientInput);

            try {
                input = ScannerKit.nextLine();
                input = input.trim();
            } catch (Exception e) {
                log.info("Under stress testing, it's recommended to set ScannerKit.closeScanner to true to disable console input!");
                log.error(e.getMessage(), e);
                break;
            }

            if (StrKit.isEmpty(input)) {
                continue;
            }

            switch (input) {
                case "help", "." -> {
                    help();
                    continue;
                }
                case ".." -> {
                    listenHelp();
                    continue;
                }
                case "..." -> {
                    help();
                    listenHelp();
                    continue;
                }
                case "q" -> {
                    IonetBanner.printlnMsg("bey!");
                    System.exit(-1);
                    continue;
                }

                // Repeat the last command
                case "+" -> input = lastInput;
                default -> lastInput = input;
            }

            // Initiate a simulated request
            request(input);
        }
    }
}
