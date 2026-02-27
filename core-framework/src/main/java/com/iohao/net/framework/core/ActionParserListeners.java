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
package com.iohao.net.framework.core;


import com.iohao.net.common.kit.*;
import com.iohao.net.framework.core.codec.*;
import java.util.*;
import lombok.*;
import org.jspecify.annotations.NonNull;
import lombok.experimental.*;
import org.jspecify.annotations.*;

/**
 * ActionParserListeners
 *
 * @author 渔民小镇
 * @date 2024-04-30
 * @since 21.7
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ActionParserListeners {
    final Map<Class<?>, ActionParserListener> map = CollKit.ofConcurrentHashMap();

    void addActionParserListener(@NonNull ActionParserListener listener) {
        this.map.putIfAbsent(listener.getClass(), listener);
    }

    void onActionCommand(ActionParserContext context) {
        this.map.forEach((clazz, listener) -> listener.onActionCommand(context));
    }

    void onAfter(BarSkeleton barSkeleton) {
        this.map.forEach((clazz, listener) -> listener.onAfter(barSkeleton));
    }

    public ActionParserListeners() {
        // Listener - Pre-create protocol proxy class
        if (DataCodecManager.getDataCodec() instanceof ProtoDataCodec) {
            this.addActionParserListener(new ProtobufActionParserListener());
            this.addActionParserListener(new ProtobufCheckActionParserListener());
        }
    }
}
