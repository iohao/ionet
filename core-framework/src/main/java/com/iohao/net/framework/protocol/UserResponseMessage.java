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

import com.iohao.net.framework.core.codec.DataCodecManager;
import com.iohao.net.framework.protocol.wrapper.ByteValueList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * UserResponseMessage
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
@Getter
@Setter
@ToString(callSuper = true)
public final class UserResponseMessage extends BarMessage implements Response {
    @Override
    public <T> T getValue(Class<T> clazz) {
        var data = this.getData();
        return DataCodecManager.decode(data, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> listValue(Class<? extends T> clazz) {
        var codec = DataCodecManager.getInternalDataCodec();
        return (List<T>) this.getValue(ByteValueList.class)
                .values
                .stream()
                .map(v -> DataCodecManager.decode(v, clazz))
                .toList();
    }

    public static UserResponseMessage of() {
        var responseMessage = new UserResponseMessage();
        responseMessage.setCmdCode(CmdCodeConst.BIZ);
        return responseMessage;
    }
}