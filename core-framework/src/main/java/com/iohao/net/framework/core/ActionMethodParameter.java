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

import com.iohao.net.framework.annotations.ValidatedGroup;
import com.iohao.net.framework.core.flow.parser.MethodParser;
import com.iohao.net.framework.core.flow.parser.MethodParsers;
import com.iohao.net.common.kit.CommonConst;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;

/**
 * ActionMethodParameter
 *
 * @author 渔民小镇
 * @date 2025-09-09
 * @since 25.1
 */
@FieldDefaults(level = AccessLevel.PUBLIC)
public final class ActionMethodParameter implements ActualParameter {
    final String name;
    final Parameter parameter;
    final Class<?> parameterClass;
    /** parameterTypeClass or list actualType */
    final Class<?> actualTypeArgumentClass;
    final boolean list;

    /** parameterTypeClass or protocolWrapperTypeClass */
    final Class<?> actualClass;

    /** JSR380 validatorGroups */
    final Class<?>[] validatorGroup;
    /** true : enable JSR380 */
    boolean validator;

    public ActionMethodParameter(Parameter p) {
        this.parameter = p;
        this.name = p.getName();
        this.parameterClass = p.getType();

        if (List.class.isAssignableFrom(this.parameterClass)) {
            ParameterizedType genericReturnType = (ParameterizedType) p.getParameterizedType();
            this.actualTypeArgumentClass = (Class<?>) genericReturnType.getActualTypeArguments()[0];
            this.list = true;
        } else {
            this.actualTypeArgumentClass = this.parameterClass;
            this.list = false;
        }

        MethodParser methodParser = MethodParsers.getMethodParser(this);
        this.actualClass = methodParser.getActualClazz(this);

        var validatedAnn = this.parameter.getAnnotation(ValidatedGroup.class);
        this.validatorGroup = Objects.isNull(validatedAnn) ? CommonConst.emptyClasses : validatedAnn.value();
    }

    @Override
    public String toString() {
        return this.toString(false);
    }

    public String toString(boolean fullName) {
        if (this.list) {
            String simpleNameParamClazz = this.parameterClass.getSimpleName();
            String simpleNameActualClazz = fullName
                    ? actualTypeArgumentClass.getName()
                    : actualTypeArgumentClass.getSimpleName();

            return String.format("%s<%s> %s", simpleNameParamClazz, simpleNameActualClazz, this.name);
        }

        String simpleNameActualClazz = fullName
                ? this.parameterClass.getName()
                : this.parameterClass.getSimpleName();

        return String.format("%s %s", simpleNameActualClazz, this.name);
    }

    @Override
    public boolean isList() {
        return list;
    }

    @Override
    public Class<?> getActualTypeArgumentClass() {
        return actualTypeArgumentClass;
    }
}
