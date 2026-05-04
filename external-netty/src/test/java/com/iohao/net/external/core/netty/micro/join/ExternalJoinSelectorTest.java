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
package com.iohao.net.external.core.netty.micro.join;

import com.iohao.net.external.core.*;
import com.iohao.net.external.core.config.*;
import com.iohao.net.external.core.micro.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests built-in external transport selector registration.
 *
 * @author 渔民小镇
 * @date 2026-05-04
 * @since 25.4
 */
class ExternalJoinSelectorTest {

    @BeforeAll
    static void loadExternalNettySelectors() throws ClassNotFoundException {
        Class.forName(ExternalServerBuilder.class.getName());
    }

    @Test
    void externalNettyRegistersTcpAndWebSocketSelectors() {
        assertInstanceOf(TcpExternalJoinSelector.class,
                ExternalJoinSelectors.getExternalJoinSelector(ExternalJoinEnum.TCP));
        assertInstanceOf(WebSocketExternalJoinSelector.class,
                ExternalJoinSelectors.getExternalJoinSelector(ExternalJoinEnum.WEBSOCKET));
    }

    @Test
    void udpRequiresCustomSelector() {
        var exception = assertThrows(RuntimeException.class,
                () -> ExternalJoinSelectors.getExternalJoinSelector(ExternalJoinEnum.UDP));

        assertTrue(exception.getMessage().contains("UDP"));
        assertTrue(exception.getMessage().contains("custom selector"));
    }
}
