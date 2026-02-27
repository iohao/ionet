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
import lombok.*;
import lombok.experimental.*;

/**
 * Static facade for the active {@link SbeCodec} implementation.
 *
 * @author 渔民小镇
 * @date 2025-09-06
 * @since 25.1
 */
@UtilityClass
public final class SbeKit {
    @Setter
    SbeCodec sbeCodec = new DefaultSbeCodec();

    /**
     * Encodes user identity fields with the active {@link #sbeCodec}.
     *
     * @param message source model
     * @param userIdentity target SBE encoder
     */
    public void encoderUserIdentity(UserIdentity message, UserIdentityMessageEncoder userIdentity) {
        sbeCodec.encoderUserIdentity(message, userIdentity);
    }

    /**
     * Decodes user identity fields with the active {@link #sbeCodec}.
     *
     * @param message target model
     * @param userIdentity source SBE decoder
     */
    public void decoderUserIdentity(UserIdentity message, UserIdentityMessageDecoder userIdentity) {
        sbeCodec.decoderUserIdentity(message, userIdentity);
    }

    /**
     * Encodes common remote-message fields with the active {@link #sbeCodec}.
     *
     * @param message source model
     * @param common target SBE encoder
     */
    public void encoderMessageCommon(RemoteMessage message, CommonMessageEncoder common) {
        sbeCodec.encoderMessageCommon(message, common);
    }

    /**
     * Decodes common remote-message fields with the active {@link #sbeCodec}.
     *
     * @param message target model
     * @param common source SBE decoder
     */
    public void decoderMessageCommon(RemoteMessage message, CommonMessageDecoder common) {
        sbeCodec.decoderMessageCommon(message, common);
    }
}
