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

import com.baidu.bjf.remoting.protobuf.*;
import com.baidu.bjf.remoting.protobuf.annotation.*;
import com.iohao.net.framework.protocol.*;
import java.util.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Default external transport message used between clients and the external server.
 * <a href="https://iohao.github.io/ionet/docs/manual/external_message">Document</a>
 *
 * @author 渔民小镇
 * @date 2023-02-21
 */
@Getter
@Setter
@ProtobufClass
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ExternalMessage extends AbstractCommunicationMessage {
    /** Command type: {@code 0} heartbeat, {@code 1} business request. */
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    int cmdCode;
    /** Protocol-level feature switches, such as encryption or signature validation. */
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    int protocolSwitch;
    /** Merged route command (high 16 bits main cmd, low 16 bits sub cmd). */
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    int cmdMerge;
    /** Response or error code, where {@code 0} means success. */
    @Protobuf(fieldType = FieldType.SINT32, order = 4)
    int errorCode;
    /** Validation or failure details, usually an error/exception message. */
    @Protobuf(fieldType = FieldType.STRING, order = 5)
    String errorMessage;
    /** Encoded business payload. */
    @Protobuf(fieldType = FieldType.BYTES, order = 6)
    byte[] data;
    /** Client message identifier echoed back by the server response. */
    @Protobuf(fieldType = FieldType.INT32, order = 7)
    int msgId;

    @Override
    public String toString() {
        return "ExternalMessage{" +
                "cmdCode=" + cmdCode +
                ", protocolSwitch=" + protocolSwitch +
                ", cmdMerge=" + cmdMerge +
                ", errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", msgId=" + msgId +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
