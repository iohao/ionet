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
package com.iohao.net.external.core.netty.hook;

import com.iohao.net.common.kit.*;
import com.iohao.net.external.core.message.*;
import com.iohao.net.external.core.session.*;
import com.iohao.net.framework.core.exception.*;
import io.netty.handler.timeout.*;
import lombok.extern.slf4j.*;

/**
 * TCP/WebSocket heartbeat hook
 *
 * @author 渔民小镇
 * @date 2023-02-18
 */
@Slf4j(topic = IonetLogName.CommonStdout)
public final class DefaultSocketIdleHook implements SocketIdleHook {

    @Override
    public boolean callback(UserSession userSession, IdleStateEvent event) {
        IdleState state = event.state();
        if (state == IdleState.READER_IDLE) {
            log.debug("READER_IDLE");
        } else if (state == IdleState.WRITER_IDLE) {
            log.debug("WRITER_IDLE");
        } else if (state == IdleState.ALL_IDLE) {
            log.debug("ALL_IDLE");
        }

        var message = CommunicationMessageKit.createCommunicationMessage();
        message.setError(ActionErrorEnum.idleErrorCode);
        message.setErrorMessage(ActionErrorEnum.idleErrorCode.getMessage() + " : " + state.name());

        userSession.writeAndFlush(message);

        // Return true means notifying the framework to close the current user (player) connection
        return true;
    }
}
