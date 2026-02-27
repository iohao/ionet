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

import java.lang.annotation.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Singleton bridge for third-party dependency injection frameworks (e.g. Spring).
 * <p>
 * When {@link #injection} is {@code true}, controller instances are obtained from
 * the external container via the configured {@link ActionFactoryBean} instead of
 * being instantiated directly by the framework.
 *
 * @author 渔民小镇
 * @date 2022-10-25
 */
@SuppressWarnings("all")
@FieldDefaults(level = AccessLevel.PUBLIC)
public final class DependencyInjectionPart {

    /** true means integration with a third-party framework */
    boolean injection;
    /** Container Management Tag */
    Class<? extends Annotation> annotationClass;

    /** The currently used ActionFactoryBean */
    ActionFactoryBean<?> actionFactoryBean;

    /**
     * Check whether the given controller class is managed by the external container.
     *
     * @param controllerClazz the controller class to check
     * @return {@code true} if the class carries the container annotation
     */
    public boolean deliveryContainer(Class<?> controllerClazz) {
        return controllerClazz.getAnnotation(annotationClass) != null;
    }

    /**
     * Obtain a controller bean from the external container for the given action command.
     *
     * @param actionCommand the action command whose controller is requested
     * @param <T>           the controller type
     * @return the controller instance
     */
    public <T> T getBean(ActionCommand actionCommand) {
        return (T) actionFactoryBean.getBean(actionCommand);
    }

    /**
     * Obtain a controller bean from the external container by class.
     *
     * @param actionControllerClazz the controller class
     * @param <T>                   the controller type
     * @return the controller instance
     */
    public <T> T getBean(Class<?> actionControllerClazz) {
        return (T) actionFactoryBean.getBean(actionControllerClazz);
    }

    private DependencyInjectionPart() {
    }

    /**
     * Return the singleton instance.
     *
     * @return the global {@link DependencyInjectionPart}
     */
    public static DependencyInjectionPart me() {
        return Holder.ME;
    }

    /** Lazy holder for the singleton instance. */
    private static class Holder {
        static final DependencyInjectionPart ME = new DependencyInjectionPart();
    }
}