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
package com.iohao.net.framework.protocol;

import com.iohao.net.framework.core.*;
import com.iohao.net.framework.core.exception.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Base class for internal messages exchanged between logic servers via Aeron.
 * <p>
 * Carries the common fields shared by all internal message types: the merged command
 * route key ({@code cmdMerge}), trace identifier, server routing IDs (external, logic,
 * source), network identifier, timing information, serialized payload data, and an
 * optional error output. Subclasses extend this to add request- or response-specific fields.
 *
 * @author 渔民小镇
 * @date 2025-09-15
 * @since 25.1
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PROTECTED)
public class CommonMessage implements RemoteMessage {
    long futureId;
    int cmdMerge;
    String traceId;

    int externalServerId;
    int logicServerId;
    int sourceServerId;
    long nanoTime;

    int netId;
    byte[] data;

    ErrorInformation outputError;

    /** {@inheritDoc} */
    @Override
    public void setCmdInfo(CmdInfo cmdInfo) {
        this.cmdMerge = cmdInfo.cmdMerge();
    }

    /** {@inheritDoc} */
    @Override
    public CmdInfo getCmdInfo() {
        return CmdInfo.of(this.cmdMerge);
    }
}
