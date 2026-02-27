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
package com.iohao.net.external.core.message;

import com.iohao.net.framework.protocol.*;
import lombok.experimental.*;

/**
 * Static facade for the configured {@link CommunicationMessageCodec}.
 *
 * @author 渔民小镇
 * @date 2023-12-15
 */
@UtilityClass
public class CommunicationMessageKit {
    /** Active codec used by external transports. */
    public CommunicationMessageCodec communicationMessageCodec = new DefaultCommunicationMessageCodec();

    /**
     * Create a new communication message via the active codec.
     *
     * @return message instance
     */
    public CommunicationMessage createCommunicationMessage() {
        return communicationMessageCodec.createCommunicationMessage();
    }

    /**
     * Encode a communication message to bytes.
     *
     * @param message message to encode
     * @return serialized bytes
     */
    public byte[] encode(CommunicationMessage message) {
        return communicationMessageCodec.encode(message);
    }

    /**
     * Decode an external message from bytes.
     *
     * @param bytes serialized bytes
     * @return decoded communication message
     */
    public CommunicationMessage decode(byte[] bytes) {
        return communicationMessageCodec.decode(bytes);
    }
}
