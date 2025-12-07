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
package com.iohao.net.framework.communication;

import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.core.codec.DataCodec;
import com.iohao.net.framework.core.codec.DataCodecManager;
import com.iohao.net.framework.protocol.RequestMessage;

import java.util.List;

/**
 * LogicCommunicationDecorator
 *
 * @author 渔民小镇
 * @date 2025-10-09
 * @since 25.1
 */
public interface LogicCommunicationDecorator {
    private DataCodec getInternalDataCodec() {
        return DataCodecManager.getInternalDataCodec();
    }

    RequestMessage ofRequestMessage(CmdInfo cmdInfo, final byte[] dataBytes);

    default RequestMessage ofRequestMessage(CmdInfo cmdInfo, int data) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encode(data));
    }

    default RequestMessage ofRequestMessage(CmdInfo cmdInfo, long data) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encode(data));
    }

    default RequestMessage ofRequestMessage(CmdInfo cmdInfo, boolean data) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encode(data));
    }

    default RequestMessage ofRequestMessage(CmdInfo cmdInfo, String data) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encode(data));
    }

    default RequestMessage ofRequestMessage(CmdInfo cmdInfo, Object data) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encode(data));
    }

    default RequestMessage ofRequestMessage(CmdInfo cmdInfo, List<?> dataList) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encodeList(dataList));
    }

    default RequestMessage ofRequestMessageListInt(CmdInfo cmdInfo, List<Integer> dataList) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encodeListInt(dataList));
    }

    default RequestMessage ofRequestMessageListLong(CmdInfo cmdInfo, List<Long> dataList) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encodeListLong(dataList));
    }

    default RequestMessage ofRequestMessageListBool(CmdInfo cmdInfo, List<Boolean> dataList) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encodeListBool(dataList));
    }

    default RequestMessage ofRequestMessageListString(CmdInfo cmdInfo, List<String> dataList) {
        var codec = this.getInternalDataCodec();
        return ofRequestMessage(cmdInfo, codec.encodeListString(dataList));
    }
}
