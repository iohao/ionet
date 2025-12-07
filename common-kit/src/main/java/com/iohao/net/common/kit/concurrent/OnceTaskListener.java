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
package com.iohao.net.common.kit.concurrent;

import com.iohao.net.common.kit.MoreKit;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.concurrent.Executor;

/**
 * Timer listener callback, executed only 1 time.
 * <pre>
 * The {@code onUpdate} method will only be executed when {@code triggerUpdate } returns true.
 *
 * By default, triggerUpdate returns true. Developers can control the execution of the onUpdate method by controlling the return value of the triggerUpdate method.
 * </pre>
 * example
 * <pre>{@code
 * // Executed once, after 500 and 800 milliseconds
 * TaskKit.runOnce(() -> log.info("500 delayMilliseconds"), 500);
 * TaskKit.runOnce(() -> log.info("800 delayMilliseconds"), 800);
 *
 * // Executed once, after 10 seconds
 * TaskKit.runOnce(new YourOnceTaskListener(), 10, TimeUnit.SECONDS);
 *
 * // Executed once, after 1500 Milliseconds, onUpdate is executed only when theTriggerUpdate is true
 * boolean theTriggerUpdate = RandomKit.randomBoolean();
 * TaskKit.runOnce(new OnceTaskListener() {
 * @Override
 * public void onUpdate() {
 * log.info("1500 delayMilliseconds");
 * }
 *
 * @Override
 * public boolean triggerUpdate() {
 * return theTriggerUpdate;
 * }
 *
 * }, 1500, TimeUnit.MILLISECONDS);
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-06
 * @see TaskKit
 * @see IntervalTaskListener
 * @since 21
 */
public interface OnceTaskListener extends TimerTask, TaskListener {
    @Override
    default void run(Timeout timeout) throws Exception {
        Executor executor = this.getExecutor();
        MoreKit.execute(executor, this::executeFlow);
    }

    private void executeFlow() {
        try {
            if (this.triggerUpdate()) {
                this.onUpdate();
            }
        } catch (Throwable e) {
            this.onException(e);
        }
    }
}
