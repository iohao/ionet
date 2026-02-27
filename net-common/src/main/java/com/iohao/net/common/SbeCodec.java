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
 * Abstraction for encoding and decoding framework message models with SBE generated codecs.
 *
 * @author 渔民小镇
 * @date 2025-10-10
 * @since 25.1
 */
public interface SbeCodec {
    /**
     * Encodes user identity fields into an SBE encoder.
     *
     * @param message source model
     * @param userIdentity target SBE encoder
     */
    void encoderUserIdentity(UserIdentity message, UserIdentityMessageEncoder userIdentity);

    /**
     * Decodes user identity fields from an SBE decoder into the framework model.
     *
     * @param message target model
     * @param userIdentity source SBE decoder
     */
    void decoderUserIdentity(UserIdentity message, UserIdentityMessageDecoder userIdentity);

    /**
     * Encodes common remote-message fields into an SBE encoder.
     *
     * @param message source model
     * @param common target SBE encoder
     */
    void encoderMessageCommon(RemoteMessage message, CommonMessageEncoder common);

    /**
     * Decodes common remote-message fields from an SBE decoder into the framework model.
     *
     * @param message target model
     * @param common source SBE decoder
     */
    void decoderMessageCommon(RemoteMessage message, CommonMessageDecoder common);
}
