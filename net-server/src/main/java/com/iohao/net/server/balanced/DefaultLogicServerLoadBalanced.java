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
package com.iohao.net.server.balanced;

import com.iohao.net.common.kit.*;
import com.iohao.net.framework.protocol.*;
import java.util.*;
import lombok.extern.slf4j.*;

/**
 * Map-based logic-server load balancer indexed by tag, command merge, and server id.
 *
 * @author 渔民小镇
 * @date 2025-09-01
 * @since 25.1
 */
@Slf4j
final class DefaultLogicServerLoadBalanced implements LogicServerLoadBalanced {
    /** key : tags */
    final Map<String, Server> tagServerMap = CollKit.ofConcurrentHashMap();
    /** key : cmdMerge */
    final Map<Integer, Server> cmdServerMap = CollKit.ofConcurrentHashMap();
    /** key : serverId */
    final Map<Integer, Server> idServerMap = CollKit.ofConcurrentHashMap();

    @Override
    public Server getServerByCmdMerge(int cmdMerge) {
        return cmdServerMap.get(cmdMerge);
    }

    @Override
    public Server getServer(int serverId) {
        return this.idServerMap.get(serverId);
    }

    @Override
    public void register(Server message) {
        String tag = message.tag();
        this.tagServerMap.put(tag, message);
        this.idServerMap.put(message.id(), message);

        var cmdMerges = message.cmdMerges();
        if (cmdMerges != null) {
            for (int cmdMerge : cmdMerges) {
                this.cmdServerMap.put(cmdMerge, message);
            }
        }
    }

    @Override
    public void unregister(Server message) {
        int id = message.id();
        String tag = message.tag();

        this.tagServerMap.remove(tag);
        this.idServerMap.remove(id);


        var cmdMerges = message.cmdMerges();
        if (cmdMerges != null) {
            for (int cmdMerge : cmdMerges) {
                this.cmdServerMap.remove(cmdMerge);
            }
        }
    }
}
