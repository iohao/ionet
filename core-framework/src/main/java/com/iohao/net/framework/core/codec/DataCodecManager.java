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
package com.iohao.net.framework.core.codec;

import com.iohao.net.framework.communication.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Central manager for the active {@link DataCodec} instances.
 * <p>
 * Holds two codec references: a primary codec used for user-facing requests and an internal
 * codec used for inter-server communication. By default both point to {@link ProtoDataCodec}.
 * Calling {@link #setDataCodec(DataCodec)} updates the primary codec and, if the internal
 * codec has not been explicitly overridden, keeps them in sync.
 *
 * @author 渔民小镇
 * @date 2025-09-28
 * @since 25.1
 */
@UtilityClass
public final class DataCodecManager {
    @Getter
    DataCodec dataCodec = new ProtoDataCodec();
    @Getter
    @Setter
    DataCodec internalDataCodec = dataCodec;

    /**
     * Set the primary data codec. If the internal codec has not been explicitly changed,
     * it is updated to match the new primary codec.
     *
     * @param dataCodec the new primary codec
     */
    public void setDataCodec(DataCodec dataCodec) {
        if (DataCodecManager.internalDataCodec == DataCodecManager.dataCodec) {
            DataCodecManager.internalDataCodec = dataCodec;
        }

        DataCodecManager.dataCodec = dataCodec;
    }

    /**
     * Encode an object using the primary codec.
     *
     * @param data the object to encode
     * @return the encoded byte array
     */
    public byte[] encode(Object data) {
        return dataCodec.encode(data);
    }

    /**
     * Decode a byte array into the specified type using the primary codec.
     *
     * @param data       the byte array to decode
     * @param paramClazz the target class
     * @param <T>        the target type
     * @return the decoded object
     */
    public <T> T decode(byte[] data, Class<T> paramClazz) {
        return dataCodec.decode(data, paramClazz);
    }

    /**
     * Return the appropriate codec for the given communication type.
     * User requests use the primary codec; internal communication uses the internal codec.
     *
     * @param communicationType the communication type
     * @return the corresponding DataCodec
     */
    public DataCodec getDataCodec(CommunicationType communicationType) {
        return communicationType == CommunicationType.USER_REQUEST ? dataCodec : internalDataCodec;
    }
}
