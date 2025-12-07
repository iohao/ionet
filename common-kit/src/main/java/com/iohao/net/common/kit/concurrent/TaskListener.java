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

/**
 * Task listener callback, used in scenarios such as: one-time delayed tasks, scheduled tasks, lightweight controllable delayed tasks, lightweight periodic persistence helper functions, and other extended scenarios.
 * <a href="https://iohao.github.io/ionet/docs/kit/task_kit">Documentation</a>
 *
 * <pre>
 * These usage scenarios share a common feature: listener callbacks. The interface provides 4 methods, as follows:
 * 1. {@link TaskListener#onUpdate()}, listener callback
 * 2. {@link TaskListener#triggerUpdate()}, whether to trigger the {@link TaskListener#onUpdate()} listener callback method
 * 3. {@link TaskListener#onException(Throwable)} , exception callback. If an exception is thrown during the execution of {@link TaskListener#triggerUpdate()} and {@link TaskListener#onUpdate()} methods, it will be caught by this method.
 * 4. {@link TaskListener#getExecutor()}, specify the executor to run the above methods, with the goal of not occupying business threads.
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-06
 * @since 21.9
 */
public interface TaskListener extends CommonTaskListener {
}
