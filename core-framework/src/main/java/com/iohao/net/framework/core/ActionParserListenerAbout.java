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

import com.baidu.bjf.remoting.protobuf.annotation.*;
import com.iohao.net.common.kit.*;
import com.iohao.net.framework.i18n.*;
import com.iohao.net.framework.protocol.wrapper.*;
import java.util.*;
import java.util.function.*;
import lombok.*;
import lombok.experimental.*;
import lombok.extern.slf4j.*;

/**
 * Eagerly creates protobuf proxy classes for action method parameter and return types
 * annotated with {@code @ProtobufClass}.
 * <p>
 * Built-in wrapper types (e.g. {@link IntValue}, {@link StringValue}) are pre-created
 * in the static initializer; user-defined protobuf types discovered during parsing are
 * collected and created in {@link #onAfter(BarSkeleton)}.
 *
 * @author 渔民小镇
 * @date 2024-05-01
 * @since 21.7
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
final class ProtobufActionParserListener implements ActionParserListener {
    static final Set<Class<?>> protoSet = CollKit.ofConcurrentSet();

    static {
        // create a protobuf proxy class
        ProtoKit.create(ByteValueList.class);

        ProtoKit.create(IntValue.class);
        ProtoKit.create(IntValueList.class);

        ProtoKit.create(BoolValue.class);
        ProtoKit.create(BoolValueList.class);

        ProtoKit.create(LongValue.class);
        ProtoKit.create(LongValueList.class);

        ProtoKit.create(StringValue.class);
        ProtoKit.create(StringValueList.class);
    }

    /** {@inheritDoc} */
    @Override
    public void onActionCommand(ActionParserContext context) {
        Predicate<Class<?>> protobufClassPredicate = c -> Objects.nonNull(c.getAnnotation(ProtobufClass.class));
        collect(context, protobufClassPredicate, protoSet);
    }

    /**
     * Collect protobuf types from the action command's parameter and return type that match the predicate.
     *
     * @param context                  the current parser context
     * @param protobufClassPredicate   predicate to test whether a class qualifies
     * @param protoSet                 target set to collect matching classes into
     */
    static void collect(ActionParserContext context, Predicate<Class<?>> protobufClassPredicate, Set<Class<?>> protoSet) {
        var actionCommand = context.actionCommand;

        Optional.ofNullable(actionCommand.dataParameter)
                .map(ActionMethodParameter::getActualTypeArgumentClass)
                .filter(WrapperKit::notSupport)
                .filter(protobufClassPredicate)
                .ifPresent(protoSet::add);

        Optional
                .ofNullable(actionCommand.actionMethodReturn)
                .filter(actionMethodReturnInfo -> !actionMethodReturnInfo.isVoid())
                .map(ActionMethodReturn::getActualTypeArgumentClass)
                .filter(WrapperKit::notSupport)
                .filter(protobufClassPredicate)
                .ifPresent(protoSet::add);
    }

    /** {@inheritDoc} */
    @Override
    public void onAfter(BarSkeleton barSkeleton) {
        protoSet.forEach(ProtoKit::create);
    }
}

/**
 * Validation listener that detects action parameter/return types missing the {@code @ProtobufClass}
 * annotation and logs an error for each offending class after parsing completes.
 */
@Slf4j
final class ProtobufCheckActionParserListener implements ActionParserListener {
    static final Set<Class<?>> protoSet = CollKit.ofConcurrentSet();

    @Override
    public void onActionCommand(ActionParserContext context) {
        Predicate<Class<?>> protobufClassPredicate = c -> c.getAnnotation(ProtobufClass.class) == null;
        ProtobufActionParserListener.collect(context, protobufClassPredicate, protoSet);
    }

    @Override
    public void onAfter(BarSkeleton barSkeleton) {
        if (protoSet.isEmpty()) {
            return;
        }

        log.error(Bundle.getMessage(MessageKey.protobufAnnotationCheck));
        for (Class<?> protoClass : protoSet) {
            log.error(protoClass.toString());
        }
    }
}