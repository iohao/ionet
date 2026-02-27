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

import com.iohao.net.common.kit.concurrent.executor.*;
import com.iohao.net.framework.*;
import com.iohao.net.framework.core.flow.*;
import com.iohao.net.framework.protocol.*;
import java.util.*;

/**
 * Lightweight {@link DefaultFlowContext} used when no real client request is available,
 * for example during runner execution or internal framework operations.
 * <p>
 * Initializes with a dummy {@link UserRequestMessage}, a zero-valued {@link CmdInfo},
 * and a virtual-thread executor.
 *
 * @author 渔民小镇
 * @date 2025-10-05
 * @since 25.1
 */
class EmptyFlowContext extends DefaultFlowContext {
    Server server;

    EmptyFlowContext() {
        Request request = new UserRequestMessage();
        this.setRequest(request);
        this.setCmdInfo(CmdInfo.of(0));

        var threadExecutor = ExecutorRegionKit.getUserVirtualThreadExecutor(0);
        this.setCurrentThreadExecutor(threadExecutor);

        this.server = Server.recordBuilder()
                .setNetId(CoreGlobalConfig.getNetId())
                .setPayloadMap(Collections.emptyMap())
                .build();
    }

    @Override
    public Server getServer() {
        return this.server;
    }
}
