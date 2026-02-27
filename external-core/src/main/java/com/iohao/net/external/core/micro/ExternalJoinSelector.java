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
package com.iohao.net.external.core.micro;

import com.iohao.net.external.core.*;
import com.iohao.net.external.core.config.*;

/**
 * Transport-type strategy used to apply connection-specific bootstrap defaults.
 * <pre>
 * Connection types: tcp, websocket, udp, kcp
 *
 * The purpose of the Selector is to initialize the properties and codecs of the relevant implementation class based on the current connection type (implementation class):
 * 1. getCodecPipeline: Codec-related Pipeline
 * 2. defaultSetting: Some default settings for the corresponding connection type
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-05-29
 */
public interface ExternalJoinSelector {
    /**
     * Get the transport type handled by this selector.
     *
     * @return transport type
     */
    ExternalJoinEnum getExternalJoinEnum();

    /**
     * Apply transport-specific default settings to the builder state.
     *
     * @param builderSetting mutable external server builder state
     */
    void defaultSetting(ExternalServerBuilderSetting builderSetting);
}
