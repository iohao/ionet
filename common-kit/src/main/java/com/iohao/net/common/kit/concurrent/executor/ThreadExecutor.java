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
package com.iohao.net.common.kit.concurrent.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Thread executor information
 *
 * @param name     Thread executor name
 * @param executor Thread executor
 * @param threadNo Number
 * @author 渔民小镇
 * @date 2023-11-30
 */
@Slf4j
public record ThreadExecutor(String name, Executor executor, int threadNo) {
    /**
     * Executes the given command at some time in the future.
     * <pre>
     * Note that developers need to catch exceptions within the command themselves (in other words,
     * do not let the Runnable throw an exception during execution), otherwise it may cause
     * executor thread corruption, making the thread unusable.
     *
     * If you are unsure whether the Runnable will throw an exception during execution, consider using the executeTry method.
     * </pre>
     *
     * @param command Command
     * @see ThreadExecutor#executeTry(Runnable)
     */
    public void execute(Runnable command) {
        this.executor.execute(command);
    }

    /**
     * Executes the given command at some time in the future.
     *
     * @param command Command
     */
    public void executeTry(Runnable command) {
        this.executor.execute(() -> {
            try {
                command.run();
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    public int getWorkQueue() {
        if (this.executor instanceof ThreadPoolExecutor threadPoolExecutor) {
            return threadPoolExecutor.getQueue().size();
        }

        return 0;
    }
}
