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

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests active server-id selection in {@link DefaultCmdRegion}.
 *
 * @author 渔民小镇
 * @date 2026-05-01
 * @since 25.4
 */
class DefaultCmdRegionTest {

    @Test
    void emptyRegionReturnsZero() {
        var region = new DefaultCmdRegion(1);

        assertEquals(0, region.getAnyServerId());
    }

    @Test
    void singleServerIdReturnsThatServerId() {
        var region = new DefaultCmdRegion(1);
        region.addServerId(1001);

        assertEquals(1001, region.getAnyServerId());
    }

    @Test
    void duplicateServerIdIsIgnored() {
        var region = new DefaultCmdRegion(1);
        region.addServerId(1001);
        region.addServerId(1001);
        region.removeByServerId(1001);

        assertFalse(region.hasServerId());
        assertEquals(0, region.getAnyServerId());
    }

    @Test
    void removedServerIdIsNotSelectedAfterExpansion() {
        var region = new DefaultCmdRegion(1);
        region.addServerId(1001);
        region.addServerId(2001);
        region.addServerId(3001);
        region.removeByServerId(2001);

        for (int i = 0; i < 1024; i++) {
            int serverId = region.getAnyServerId();
            assertTrue(serverId == 1001 || serverId == 3001);
        }
    }
}
