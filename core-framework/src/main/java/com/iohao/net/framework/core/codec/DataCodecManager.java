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

import com.iohao.net.framework.communication.CommunicationType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

/**
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

    public void setDataCodec(DataCodec dataCodec) {
        if (DataCodecManager.internalDataCodec == DataCodecManager.dataCodec) {
            DataCodecManager.internalDataCodec = dataCodec;
        }

        DataCodecManager.dataCodec = dataCodec;
    }

    public byte[] encode(Object data) {
        return dataCodec.encode(data);
    }

    public <T> T decode(byte[] data, Class<T> paramClazz) {
        return dataCodec.decode(data, paramClazz);
    }

    public DataCodec getDataCodec(CommunicationType communicationType) {
        return communicationType == CommunicationType.USER_REQUEST ? dataCodec : internalDataCodec;
    }
}
