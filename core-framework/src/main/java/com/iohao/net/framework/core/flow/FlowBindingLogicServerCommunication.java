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
package com.iohao.net.framework.core.flow;

import com.iohao.net.common.kit.exception.*;
import com.iohao.net.framework.annotations.*;
import com.iohao.net.framework.protocol.*;

/**
 * Flow-level communication for binding or unbinding a user session to specific logic servers.
 * This is an enterprise feature; default implementations throw {@link EnterpriseSupportException}.
 *
 * @author 渔民小镇
 * @date 2025-10-09
 * @since 25.1
 */
@Enterprise
public interface FlowBindingLogicServerCommunication extends FlowExternalCommunication {

    /**
     * Bind or unbind the current user session to the specified logic servers.
     *
     * @param operation      the binding operation (bind or unbind)
     * @param logicServerIds the IDs of the logic servers to bind or unbind
     * @return the response indicating success or failure
     */
    default CommonResponse bindingLogicServer(BindingEnum operation, int[] logicServerIds) {
        throw new EnterpriseSupportException();
    }

    /**
     * Clear all logic server bindings for the current user session.
     *
     * @return the response indicating success or failure
     */
    default CommonResponse clearBindingLogicServer() {
        throw new EnterpriseSupportException();
    }
}
