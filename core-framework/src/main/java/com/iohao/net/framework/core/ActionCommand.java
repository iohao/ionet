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

import com.iohao.net.framework.core.doc.ActionCommandDoc;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

/**
 * ActionCommand command object, also known as action.
 * When the business framework handles business processes, that is, during the processing phase,
 * it can obtain various desired information through the action.
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
@Builder(setterPrefix = "set")
@FieldDefaults(level = AccessLevel.PUBLIC)
public final class ActionCommand {
    final CmdInfo cmdInfo;
    /** A single controller object */
    final Object actionController;
    /** The class where the method is located */
    final Class<?> actionControllerClass;

    /** The method object */
    final Method method;

    /** The array of method parameter information */
    final ActionMethodParameter[] actionMethodParameters;
    /** The return type */
    final ActionMethodReturn actionMethodReturn;

    final ActionCommandDoc actionCommandDoc;
    final ActionParameterPosition actionParameterPosition;
    final MethodHandle methodHandle;
    ActionMethodParameter dataParameter;
    /** true means it's managed by a container like Spring */
    boolean deliveryContainer;

    public boolean hasDataParameter() {
        return dataParameter != null;
    }

    public String getActionMethodName() {
        return method.getName();
    }

    /**
     * Whether the action method contains the relevant annotation.
     *
     * @param annotationClass The relevant annotation
     * @return true if it does
     * @since 21.10
     */
    public boolean containAnnotation(Class<? extends Annotation> annotationClass) {
        return this.getAnnotation(annotationClass) != null;
    }

    /**
     * Get the relevant annotation for the action method.
     *
     * @param annotationClass The relevant annotation
     * @param <T>             t
     * @return The relevant annotation
     * @since 21.10
     */
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return method.getAnnotation(annotationClass);
    }
}