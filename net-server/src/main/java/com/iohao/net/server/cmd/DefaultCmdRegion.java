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
package com.iohao.net.server.cmd;

import com.iohao.net.common.kit.*;
import com.iohao.net.framework.core.kit.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Default {@link CmdRegion} implementation backed by lock-free server-id snapshots.
 *
 * @author 渔民小镇
 * @date 2023-04-30
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class DefaultCmdRegion implements CmdRegion {
    final int cmdMerge;
    final AtomicReference<int[]> serverIdsRef = new AtomicReference<>(new int[0]);

    public DefaultCmdRegion(int cmdMerge) {
        this.cmdMerge = cmdMerge;
    }

    @Override
    public void addServerId(int serverId) {
        while (true) {
            int[] serverIds = this.serverIdsRef.get();
            if (contains(serverIds, serverId)) {
                return;
            }

            int[] newServerIds = Arrays.copyOf(serverIds, serverIds.length + 1);
            newServerIds[serverIds.length] = serverId;
            if (this.serverIdsRef.compareAndSet(serverIds, newServerIds)) {
                return;
            }
        }
    }

    @Override
    public void removeByServerId(int serverId) {
        while (true) {
            int[] serverIds = this.serverIdsRef.get();
            int index = indexOf(serverIds, serverId);
            if (index == -1) {
                return;
            }

            int[] newServerIds = new int[serverIds.length - 1];
            System.arraycopy(serverIds, 0, newServerIds, 0, index);
            System.arraycopy(serverIds, index + 1, newServerIds, index, serverIds.length - index - 1);
            if (this.serverIdsRef.compareAndSet(serverIds, newServerIds)) {
                return;
            }
        }
    }

    private boolean contains(int[] serverIds, int serverId) {
        return indexOf(serverIds, serverId) != -1;
    }

    private int indexOf(int[] serverIds, int serverId) {
        for (int i = 0; i < serverIds.length; i++) {
            if (serverIds[i] == serverId) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int endpointLogicServerId(int[] logicServerIds) {
        int[] serverIds = this.serverIdsRef.get();
        for (int logicServerId : logicServerIds) {
            for (int serverId : serverIds) {
                if (serverId == logicServerId) {
                    return logicServerId;
                }
            }
        }

        return 0;
    }

    @Override
    public boolean hasServerId() {
        return this.serverIdsRef.get().length > 0;
    }

    @Override
    public int getAnyServerId() {
        int[] serverIds = this.serverIdsRef.get();
        int size = serverIds.length;
        if (size == 0) {
            return 0;
        }

        return size == 1
                ? serverIds[0]
                : serverIds[RandomKit.randomInt(size)];
    }

    @Override
    public String toString() {
        int[] serverIds = this.serverIdsRef.get();
        return "CmdRegion {" +
                CmdKit.toString(cmdMerge) +
                " -- serverIds : " + Arrays.toString(serverIds) +
                '}';
    }
}
