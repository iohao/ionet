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
package com.iohao.net.extension.spring;

import com.iohao.net.framework.core.ActionCommand;
import com.iohao.net.framework.core.ActionFactoryBean;
import com.iohao.net.framework.core.DependencyInjectionPart;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * ActionFactoryBeanForSpring
 *
 * @author 渔民小镇
 * @date 2022-03-22
 */
@SuppressWarnings("all")
public class ActionFactoryBeanForSpring<T> implements ActionFactoryBean<T>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public T getBean(ActionCommand actionCommand) {
        Class<?> actionControllerClazz = actionCommand.actionControllerClass;
        return (T) this.applicationContext.getBean(actionControllerClazz);
    }

    @Override
    public T getBean(Class<?> actionControllerClazz) {
        return (T) this.applicationContext.getBean(actionControllerClazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Objects.requireNonNull(applicationContext);

        initDependencyInjectionPart();

        this.applicationContext = applicationContext;
    }

    private void initDependencyInjectionPart() {
        var dependencyInjectionPart = DependencyInjectionPart.me();
        dependencyInjectionPart.injection = true;
        dependencyInjectionPart.annotationClass = Component.class;
        dependencyInjectionPart.actionFactoryBean = this;
    }
}
