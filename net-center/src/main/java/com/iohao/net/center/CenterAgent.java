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
package com.iohao.net.center;

import com.iohao.net.framework.*;
import io.aeron.*;
import io.aeron.logbuffer.*;
import lombok.extern.slf4j.*;
import org.agrona.concurrent.*;

/**
 * Agrona agent wrapper for the center server polling loop.
 *
 * @author 渔民小镇
 * @date 2025-08-24
 * @since 25.1
 */
@Slf4j
final class CenterAgent implements Agent {
    final FragmentHandler fragmentHandler;
    CenterConnectionManager connectionManager;

    CenterAgent(CenterServerSetting setting) {
        this.connectionManager = setting.connectionManager();
        var adapter = new CenterAdapter(setting);
        if (CoreGlobalConfig.enableFragmentAssembler) {
            this.fragmentHandler = new FragmentAssembler(adapter);
        } else {
            this.fragmentHandler = adapter;
        }
    }

    @Override
    public void onStart() {
    }

    @Override
    public int doWork() {
        return this.connectionManager.poll(fragmentHandler);
    }

    @Override
    public String roleName() {
        return "Center";
    }
}
