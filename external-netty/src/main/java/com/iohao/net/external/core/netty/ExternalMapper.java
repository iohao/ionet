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
package com.iohao.net.external.core.netty;

import com.iohao.net.external.core.*;
import com.iohao.net.external.core.config.*;
import lombok.experimental.*;

/**
 * Convenience factory for creating Netty-based external server builders.
 *
 * @author 渔民小镇
 * @date 2025-10-17
 * @since 25.1
 */
@UtilityClass
public final class ExternalMapper {
    /** Factory used to create the concrete external server implementation. */
    public ExternalServerCreator externalServerCreator = DefaultExternalServer::new;

    /**
     * Create a builder preconfigured with the Netty external server creator.
     *
     * @return external server builder
     */
    public ExternalServerBuilder builder() {
        var builder = new ExternalServerBuilder();
        builder.setExternalServerCreator(externalServerCreator);
        return builder;
    }

    /**
     * Create a WebSocket external server builder on the given port.
     *
     * @param port bind port
     * @return external server builder
     */
    public ExternalServerBuilder builder(int port) {
        return builder(port, ExternalJoinEnum.WEBSOCKET);
    }

    /**
     * Create an external server builder on the given port and transport type.
     *
     * @param port bind port
     * @param joinEnum transport type
     * @return external server builder
     */
    public ExternalServerBuilder builder(int port, ExternalJoinEnum joinEnum) {
        var builder = builder();
        builder.setPort(port);
        builder.setJoinEnum(joinEnum);
        return builder;
    }
}
