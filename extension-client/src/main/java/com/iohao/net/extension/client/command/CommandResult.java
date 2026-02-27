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
package com.iohao.net.extension.client.command;

import com.iohao.net.external.core.message.*;
import com.iohao.net.framework.core.*;
import com.iohao.net.framework.core.codec.*;
import com.iohao.net.framework.core.kit.*;
import com.iohao.net.framework.protocol.wrapper.*;
import java.util.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Callback result wrapper with lazy response payload decoding helpers.
 *
 * @author 渔民小镇
 * @date 2023-07-08
 */
@FieldDefaults(level = AccessLevel.PACKAGE)
public class CommandResult {
    final ExternalMessage message;
    /** Lazily decoded business object. */
    Object value;

    public CommandResult(ExternalMessage message) {
        this.message = message;
    }


    public int getMsgId() {
        return message.getMsgId();
    }

    public CmdInfo getCmdInfo() {
        return CmdInfo.of(message.getCmdMerge());
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(Class<? extends T> clazz) {
        byte[] data = this.message.getData();

        if (this.value == null) {
            this.value = DataCodecManager.decode(data, clazz);
        }

        return (T) value;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> listValue(Class<? extends T> clazz) {
        return (List<T>) this.getValue(ByteValueList.class)
                .values
                .stream()
                .map(v -> DataCodecManager.decode(v, clazz))
                .toList();
    }

    public String getString() {
        return this.getValue(StringValue.class).value;
    }

    public List<String> listString() {
        return this.getValue(StringValueList.class).values;
    }

    public int getInt() {
        return this.getValue(IntValue.class).value;
    }

    public List<Integer> listInt() {
        return this.getValue(IntValueList.class).values;
    }

    public long getLong() {
        return this.getValue(LongValue.class).value;
    }

    public List<Long> listLong() {
        return this.getValue(LongValueList.class).values;
    }

    public boolean getBoolean() {
        return this.getValue(BoolValue.class).value;
    }

    public List<Boolean> listBoolean() {
        return this.getValue(BoolValueList.class).values;
    }

    @Override
    public String toString() {

        CmdInfo cmdInfo = getCmdInfo();

        return String.format("msgId:%s - %s \n%s"
                , getMsgId()
                , CmdKit.toString(cmdInfo.cmdMerge())
                , value);
    }
}
