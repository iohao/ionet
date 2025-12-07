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

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.net.framework.protocol.AbstractCommunicationMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;

/**
 * ExternalMessage <a href="https://iohao.github.io/ionet/docs/manual/external_message">Document</a>
 *
 * @author 渔民小镇
 * @date 2023-02-21
 */
@Getter
@Setter
@ProtobufClass
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ExternalMessage extends AbstractCommunicationMessage {
    /** 请求命令类型: 0 心跳，1 业务 */
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    int cmdCode;
    /** 协议开关，用于一些协议级别的开关控制，比如 安全加密校验等。 */
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    int protocolSwitch;
    /** 业务路由（高16为主, 低16为子） */
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    int cmdMerge;
    /** 响应码、错误码。0 表示成功。 */
    @Protobuf(fieldType = FieldType.SINT32, order = 4)
    int errorCode;
    /** 验证信息（错误消息、异常消息） */
    @Protobuf(fieldType = FieldType.STRING, order = 5)
    String errorMessage;
    /** 业务数据 */
    @Protobuf(fieldType = FieldType.BYTES, order = 6)
    byte[] data;
    /** 消息标记号；由前端请求时设置，服务器响应时会携带上 */
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
