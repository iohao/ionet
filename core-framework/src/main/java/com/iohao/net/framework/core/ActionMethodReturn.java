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

import com.iohao.net.framework.core.flow.parser.MethodParser;
import com.iohao.net.framework.core.flow.parser.MethodParsers;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Metadata about an action method's return type, including generic type resolution
 * for {@link java.util.List} return types.
 *
 * @author 渔民小镇
 * @date 2025-09-09
 * @since 25.1
 */
@FieldDefaults(level = AccessLevel.PUBLIC)
public final class ActionMethodReturn implements ActualParameter {
    final Class<?> returnTypeClass;
    /** returnTypeClass or list actualType */
    final Class<?> actualTypeArgumentClass;
    final boolean list;
    /** returnTypeClass or protocolWrapperTypeClass */
    final Class<?> actualClazz;

    public ActionMethodReturn(Method method) {
        Class<?> returnTypeClass = method.getReturnType();
        this.returnTypeClass = returnTypeClass;

        if (List.class.isAssignableFrom(returnTypeClass)) {
            ParameterizedType genericReturnType = (ParameterizedType) method.getGenericReturnType();
            this.actualTypeArgumentClass = (Class<?>) genericReturnType.getActualTypeArguments()[0];
            this.list = true;
        } else {
            this.actualTypeArgumentClass = returnTypeClass;
            this.list = false;
        }

        MethodParser methodParser = MethodParsers.getMethodParser(this);
        this.actualClazz = methodParser.getActualClazz(this);
    }

    /**
     * Check if the return type is void.
     *
     * @return {@code true} if the method returns void
     */
    public boolean isVoid() {
        return Void.TYPE == this.returnTypeClass;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    /**
     * Format the return type as a string, optionally using fully qualified class names.
     *
     * @param fullName {@code true} to use fully qualified names, {@code false} for simple names
     * @return formatted return type string
     */
    public String toString(boolean fullName) {
        if (this.list) {
            String simpleNameReturnTypeClazz = this.returnTypeClass.getSimpleName();
            String simpleNameActualClazz = fullName
                    ? this.actualTypeArgumentClass.getName()
                    : this.actualTypeArgumentClass.getSimpleName();

            return String.format("%s<%s>", simpleNameReturnTypeClazz, simpleNameActualClazz);
        }

        return fullName
                ? this.actualTypeArgumentClass.getName()
                : this.actualTypeArgumentClass.getSimpleName();
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
