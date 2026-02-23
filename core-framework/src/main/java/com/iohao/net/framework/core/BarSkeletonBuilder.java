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

import com.iohao.net.framework.CoreGlobalConfig;
import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.core.doc.BroadcastDocumentBuilder;
import com.iohao.net.framework.core.doc.DocumentHelper;
import com.iohao.net.framework.core.enhance.BarSkeletonBuilderEnhances;
import com.iohao.net.framework.core.flow.*;
import com.iohao.net.framework.core.flow.internal.DefaultActionAfter;
import com.iohao.net.framework.core.flow.internal.DefaultActionMethodExceptionProcess;
import com.iohao.net.framework.core.flow.internal.DefaultActionMethodInvoke;
import com.iohao.net.framework.core.runner.Runner;
import com.iohao.net.framework.core.runner.Runners;
import com.iohao.net.common.kit.ClassScanner;
import com.iohao.net.common.kit.concurrent.executor.ExecutorRegion;
import com.iohao.net.common.kit.concurrent.executor.ExecutorRegionKit;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.function.Predicate;

/**
 * Builder for constructing a {@link BarSkeleton} instance. Configures action controllers,
 * interceptors, flow executors, and other components.
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
@Setter
public final class BarSkeletonBuilder {
    final Runners runners = new Runners();
    final Set<Class<?>> actionControllerClassSet = new HashSet<>();
    final List<Class<?>> scanClassList = new ArrayList<>();
    final List<ActionMethodInOut> inOutList = new LinkedList<>();
    final ActionParserListeners actionParserListeners = new ActionParserListeners();

    FlowExecutor flowExecutor;
    ActionFactoryBean<Object> actionFactoryBean;
    ActionAfter actionAfter;
    ActionMethodExceptionProcess actionMethodExceptionProcess;
    ActionMethodInvoke actionMethodInvoke;

    FlowContextFactory flowContextFactory;
    ExecutorRegion executorRegion;
    ActionCommandParser actionCommandParser;

    BarSkeletonBuilder() {
    }

    /**
     * Build and return a fully configured {@link BarSkeleton} instance.
     *
     * @return a new {@link BarSkeleton} configured with the settings from this builder
     */
    public BarSkeleton build() {
        this.scan();
        this.defaultSetting();

        // --------------- internalBuilder ---------------
        var builder = BarSkeleton.internalBuilder()
                .setActionFactoryBean(this.actionFactoryBean)
                .setActionMethodInvoke(this.actionMethodInvoke)
                .setActionMethodExceptionProcess(this.actionMethodExceptionProcess)
                .setActionAfter(this.actionAfter)
                .setFlowContextFactory(this.flowContextFactory)
                .setExecutorRegion(this.executorRegion)
                .setRunners(this.runners)
                .setFlowExecutor(flowExecutor)
                .setInOuts(inOutList.toArray(new ActionMethodInOut[0]));

        // --------------- ActionCommandParser ---------------
        actionCommandParser.buildAction(this.actionControllerClassSet);
        var actionCommandRegions = actionCommandParser.getActionCommandRegions();
        // Convert all actions into a two-dimensional array of actions.
        actionCommandRegions.initActionCommandArray();

        builder.setActionCommandRegions(actionCommandRegions);
        builder.setActionCommands(actionCommandRegions.actionCommands);

        var barSkeleton = builder.build();

        // --------------- ActionParserListeners ---------------
        barSkeleton.actionCommandRegions.regionMap.forEach((_, actionCommandRegion) -> {
            // action command, actionMethod
            actionCommandRegion.subActionCommandMap.forEach((_, command) -> {
                var context = new ActionParserContext();
                context.barSkeleton = barSkeleton;
                context.actionCommand = command;

                actionParserListeners.onActionCommand(context);
            });
        });

        actionParserListeners.onAfter(barSkeleton);

        // --------------- end ---------------
        PrintActionKit.print(barSkeleton);
        this.runners.setBarSkeleton(barSkeleton);

        return barSkeleton;
    }

    private void defaultSetting() {
        if (this.executorRegion == null) {
            this.executorRegion = ExecutorRegionKit.getExecutorRegion();
        }

        if (this.flowExecutor == null) {
            this.flowExecutor = CoreGlobalConfig.setting.flowExecutor;
        }

        if (this.actionFactoryBean == null) {
            this.actionFactoryBean = new DefaultActionFactoryBean<>();
        }

        if (this.actionMethodExceptionProcess == null) {
            this.actionMethodExceptionProcess = DefaultActionMethodExceptionProcess.me();
        }

        if (this.actionMethodInvoke == null) {
            this.actionMethodInvoke = DefaultActionMethodInvoke.me();
        }

        if (this.flowContextFactory == null) {
            this.flowContextFactory = CoreGlobalConfig.setting.flowContextFactory;
        }

        if (this.actionAfter == null) {
            this.actionAfter = DefaultActionAfter.me();
        }

        if (this.actionCommandParser == null) {
            this.actionCommandParser = new DefaultActionCommandParser();
        }
    }

    /**
     * Scan the package of the given class for {@code @ActionController} annotated classes.
     * Scans the package and all sub-packages of the specified class.
     *
     * @param actionControllerClass a class whose package will be scanned for action controllers
     */
    public void scanActionPackage(@NonNull Class<?> actionControllerClass) {
        this.scanClassList.add(actionControllerClass);
    }

    /**
     * Register an action controller class to be scanned for {@code @ActionMethod} routes.
     *
     * @param actionControllerClass the action controller class to register
     */
    public void addActionController(@NonNull Class<?> actionControllerClass) {
        actionControllerClassSet.add(actionControllerClass);
    }

    private void scan() {
        BarSkeletonBuilderEnhances.enhance(this);
        Predicate<Class<?>> predicateFilter = clazz -> Objects.nonNull(clazz.getAnnotation(ActionController.class));

        for (Class<?> actionClazz : scanClassList) {
            String packagePath = actionClazz.getPackageName();
            ClassScanner classScanner = new ClassScanner(packagePath, predicateFilter);
            List<Class<?>> classList = classScanner.listScan();

            actionControllerClassSet.addAll(classList);
        }
    }

    /**
     * Add a broadcast document definition for documentation generation.
     *
     * @param builder the broadcast document builder
     * @return this builder for chaining
     */
    public BarSkeletonBuilder addBroadcastDocument(@NonNull BroadcastDocumentBuilder builder) {
        DocumentHelper.addBroadcastDocument(builder.build());
        return this;
    }

    /**
     * Add an interceptor to the action method processing pipeline.
     *
     * @param inOut the interceptor to add
     */
    public void addInOut(@NonNull ActionMethodInOut inOut) {
        inOutList.add(inOut);
    }

    /**
     * Add a runner to be executed during server startup.
     *
     * @param runner the runner to add
     */
    public void addRunner(@NonNull Runner runner) {
        this.runners.addRunner(runner);
    }

    /**
     * Add a listener that is notified when action commands are parsed.
     *
     * @param listener the action parser listener to add
     */
    public void addActionParserListener(@NonNull ActionParserListener listener) {
        this.actionParserListeners.addActionParserListener(listener);
    }
}