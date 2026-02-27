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
package com.iohao.net.common;

import com.iohao.net.framework.protocol.*;
import com.iohao.net.sbe.*;

/**
 * Default {@link SbeCodec} that maps framework message fields to SBE generated codecs.
 *
 * @author 渔民小镇
 * @date 2025-10-10
 * @since 25.1
 */
public class DefaultSbeCodec implements SbeCodec {
    @Override
    public void encoderUserIdentity(UserIdentity message, UserIdentityMessageEncoder userIdentity) {
        userIdentity.userId(message.getUserId())
                .verifyIdentity(message.verifyIdentity());
    }

    @Override
    public void decoderUserIdentity(UserIdentity message, UserIdentityMessageDecoder userIdentity) {
        message.setUserId(userIdentity.userId());
        message.setVerifyIdentity(userIdentity.verifyIdentity() == 1);
    }

    @Override
    public void encoderMessageCommon(RemoteMessage message, CommonMessageEncoder common) {
        common.futureId(message.getFutureId())
                .cmdMerge(message.getCmdMerge())
                .netId(message.getNetId())
                .externalServerId(message.getExternalServerId())
                .logicServerId(message.getLogicServerId())
                .sourceServerId(message.getSourceServerId())
                .nanoTime(message.getNanoTime())
        ;
    }

    @Override
    public void decoderMessageCommon(RemoteMessage message, CommonMessageDecoder common) {
        message.setFutureId(common.futureId());
        message.setCmdMerge(common.cmdMerge());
        message.setNetId(common.netId());
        message.setExternalServerId(common.externalServerId());
        message.setLogicServerId(common.logicServerId());
        message.setSourceServerId(common.sourceServerId());
        message.setNanoTime(common.nanoTime());
    }
}
