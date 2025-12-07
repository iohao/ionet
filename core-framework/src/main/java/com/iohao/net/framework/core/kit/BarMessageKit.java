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
package com.iohao.net.framework.core.kit;

import com.iohao.net.framework.protocol.*;
import lombok.experimental.UtilityClass;

/**
 *
 * @author 渔民小镇
 * @date 2022-06-07
 */
@UtilityClass
public class BarMessageKit {
    private void employCommon(Request request, Response message) {
        message.setFutureId(request.getFutureId());
        message.setUserId(request.getUserId());
        message.setVerifyIdentity(request.isVerifyIdentity());

        message.setCmdMerge(request.getCmdMerge());
        message.setTraceId(request.getTraceId());

        message.setExternalServerId(request.getExternalServerId());
        message.setLogicServerId(request.getLogicServerId());
        message.setSourceServerId(request.getSourceServerId());

        message.setNetId(request.getNetId());
    }

    public void employ(Request request, ResponseMessage message) {
        employCommon(request, message);
    }

    public void employ(Request request, RequestMessage message) {
        message.setUserId(request.getUserId());
        message.setVerifyIdentity(request.isVerifyIdentity());

        message.setTraceId(request.getTraceId());
        message.setHopCount(request.getHopCount() + 1);

        message.setExternalServerId(request.getExternalServerId());
        message.setAttachment(request.getAttachment());
        message.setBindingLogicServerIds(request.getBindingLogicServerIds());
    }

    public void employ(Request request, UserResponseMessage message) {
        employCommon(request, message);

        if (request instanceof UserRequestMessage req) {
            message.setMsgId(req.getMsgId());
            message.setCacheCondition(req.getCacheCondition());
        }
    }
}
