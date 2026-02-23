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
package com.iohao.net.framework.core.runner;

import com.iohao.net.framework.communication.eventbus.EventBusSettingKit;

/**
 * Framework-internal runner that registers the EventBus validation runner (if configured)
 * into the {@link Runners#runnerBeforeList} so it executes before user-defined runners.
 *
 * @author 渔民小镇
 * @date 2023-04-23
 */
final class InternalRunner {

    InternalRunner(Runners runners) {
        Runner checkedEventBusRunner = EventBusSettingKit.setting.getCheckedEventBusRunner();
        if (checkedEventBusRunner != null) {
            runners.runnerBeforeList.add(checkedEventBusRunner);
        }
    }
}
