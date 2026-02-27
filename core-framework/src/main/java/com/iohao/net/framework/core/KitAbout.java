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
import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.framework.core.codec.DataCodecManager;
import com.iohao.net.framework.core.doc.ActionCommandDoc;
import com.iohao.net.framework.core.doc.ActionCommandDocKit;
import com.iohao.net.framework.core.doc.DocumentHelper;
import com.iohao.net.framework.core.doc.JavaClassDocInfo;
import com.iohao.net.framework.core.flow.ActionMethodInOut;
import com.iohao.net.framework.core.flow.FlowContext;
import com.iohao.net.framework.core.kit.CmdKit;
import com.iohao.net.framework.core.kit.ValidatorKit;
import com.iohao.net.framework.core.runner.Runners;
import com.iohao.net.framework.i18n.Bundle;
import com.iohao.net.framework.i18n.MessageKey;
import com.iohao.net.framework.toy.IonetBanner;
import com.iohao.net.common.kit.ArrayKit;
import com.iohao.net.common.kit.CollKit;
import com.iohao.net.common.kit.StrKit;
import com.iohao.net.common.kit.concurrent.TaskKit;
import com.iohao.net.common.kit.exception.ThrowKit;
import jakarta.validation.Valid;
import org.fusesource.jansi.Ansi;

import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility for parsing Java source-level documentation (Javadoc) from {@code @ActionController}
 * classes and attaching it to the corresponding {@link ActionCommandDoc} metadata.
 *
 * @author 渔民小镇
 * @date 2022-12-09
 */
class ActionCommandDocParserKit {
    static final Map<Integer, ActionCommandDoc> actionCommandDocMap = CollKit.ofConcurrentHashMap();
    static final ActionCommandDoc emptyActionCommandDoc = new ActionCommandDoc();
    static final Set<Class<?>> processedSet = CollKit.ofConcurrentSet();

    static void parseDoc(ActionCommandParser actionCommandParser, Set<Class<?>> classSet) {
        if (!CoreGlobalConfig.setting.parseDoc) {
            return;
        }

        var actionCommandRegions = actionCommandParser.getActionCommandRegions();
        // java source
        Map<String, JavaClassDocInfo> javaClassDocInfoMap = ActionCommandDocKit.getJavaClassDocInfoMap(classSet);

        ActionCommandParserKit.streamActionController(classSet).forEach(controllerClass -> {
            if (processedSet.contains(controllerClass)) {
                return;
            } else {
                processedSet.add(controllerClass);
            }

            // java source
            var javaClassDocInfo = javaClassDocInfoMap.get(controllerClass.getName());

            var cmd = controllerClass.getAnnotation(ActionController.class).value();
            var actionCommandRegion = actionCommandRegions.getActionCommandRegion(cmd);
            actionCommandRegion.javaClassDocInfo = javaClassDocInfo;

            var actionDoc = DocumentHelper.ofActionDoc(cmd, controllerClass);
            actionDoc.javaClassDocInfo = javaClassDocInfo;

            ActionCommandParserKit.streamActionMethod(controllerClass).forEach(method -> {
                var actionCommandDoc = javaClassDocInfo != null
                        ? javaClassDocInfo.createActionCommandDoc(method)
                        : new ActionCommandDoc();

                int subCmd = method.getAnnotation(ActionMethod.class).value();
                int cmdMerge = CmdKit.merge(cmd, subCmd);
                actionCommandDocMap.put(cmdMerge, actionCommandDoc);
                actionDoc.addActionCommandDoc(actionCommandDoc);
            });
        });
    }

    static ActionCommandDoc getActionCommandDoc(int cmd, int subCmd) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        return actionCommandDocMap.getOrDefault(cmdMerge, emptyActionCommandDoc);
    }
}

/** Intermediate holder for parsed action method parameters during command building. */
class ActionCommandBuilderData {
    ActionMethodParameter[] actionMethodParameters;
    ActionMethodParameter dataParameter;
}

/**
 * Internal utilities for scanning {@code @ActionController} / {@code @ActionMethod} annotated
 * classes, building {@link ActionCommand} metadata, and validating command IDs.
 */
final class ActionCommandParserKit {
    static Stream<Class<?>> streamActionController(Set<Class<?>> classSet) {
        return classSet
                .parallelStream()
                .filter(clazz -> Objects.nonNull(clazz.getAnnotation(ActionController.class)));
    }

    static Stream<Method> streamActionMethod(Class<?> actionClass) {
        return Arrays
                .stream(actionClass.getDeclaredMethods())
                .parallel()
                .filter(method -> Objects.nonNull(method.getAnnotation(ActionMethod.class)))
                .filter(method -> !Modifier.isStatic(method.getModifiers()));
    }

    static MethodType ofMethodType(Method method, Class<?> returnTypeClazz) {
        var parameters = method.getParameters();

        if (parameters.length == 0) {
            return MethodType.methodType(returnTypeClazz);
        }

        Class<?>[] classes = new Class[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            classes[i] = parameters[i].getType();
        }

        return MethodType.methodType(returnTypeClazz, classes);
    }


    private record ParameterPosition(int index, boolean bizData) {
    }

    static ActionParameterPosition parseParameterPosition(Method method, ActionCommandBuilderData builderData) {
        var parameters = method.getParameters();

        if (ArrayKit.isEmpty(parameters)) {
            return ActionParameterPosition.none;
        }

        if (parameters.length > 2) {
            ThrowKit.ofIllegalArgumentException("The method supports a maximum of two parameters: business parameters and FlowContext.");
        }

        var positions = new ParameterPosition[parameters.length];
        var actionMethodParameters = new ActionMethodParameter[parameters.length];
        builderData.actionMethodParameters = actionMethodParameters;

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            var parameterClazz = parameter.getType();
            var actionMethodParameter = new ActionMethodParameter(parameter);
            actionMethodParameters[i] = actionMethodParameter;

            if (FlowContext.class.isAssignableFrom(parameterClazz)) {
                positions[i] = new ParameterPosition(i, false);
                continue;
            } else {
                positions[i] = new ParameterPosition(i, true);
                builderData.dataParameter = actionMethodParameter;
            }

            // JSR380
            var setting = CoreGlobalConfig.setting;
            if (setting.validator) {
                if (setting.validatorAutoCall) {
                    TaskKit.executeVirtual(() -> actionMethodParameter.validator = ValidatorKit.isValidator(parameter.getType()));
                } else {
                    if (parameter.getAnnotation(Valid.class) != null) {
                        TaskKit.executeVirtual(() -> actionMethodParameter.validator = ValidatorKit.isValidator(parameter.getType()));
                    }
                }
            }
        }

        if (positions.length == 1) {
            ParameterPosition position = positions[0];
            if (position.bizData) {
                return ActionParameterPosition.data;
            } else {
                return ActionParameterPosition.flowContext;
            }
        }

        var position1 = positions[0];
        var position2 = positions[1];
        if (position1.bizData == position2.bizData) {
            ThrowKit.ofIllegalArgumentException("The method supports a maximum of two parameters: business parameters and FlowContext.");
        }

        if (position1.bizData && !position2.bizData) {
            return ActionParameterPosition.dataAndFlowContext;
        } else {
            return ActionParameterPosition.flowContextAndData;
        }
    }

    static void checkDuplicateRoute(Class<?> controllerClass, int subCmd, ActionCommandRegion actionCommandRegion) {

        if (actionCommandRegion.containsKey(subCmd)) {
            String message = String.format("%s already exists. see: %s",
                    CmdInfo.of(actionCommandRegion.cmd, subCmd), controllerClass
            );

            IonetBanner.ofRuntimeException(message);
        }
    }

    static boolean deliveryContainer(Class<?> controllerClazz) {
        if (DependencyInjectionPart.me().injection) {
            return DependencyInjectionPart.me().deliveryContainer(controllerClazz);
        }

        return false;
    }

    static Object ofActionInstance(Class<?> controllerClazz) {
        boolean deliveryContainer = ActionCommandParserKit.deliveryContainer(controllerClazz);

        var actionInstance = deliveryContainer
                ? DependencyInjectionPart.me().getBean(controllerClazz)
                : newInstance(controllerClazz);

        Objects.requireNonNull(actionInstance);

        return actionInstance;
    }

    private static Object newInstance(Class<?> controllerClazz) {
        try {
            Constructor<?> constructor = controllerClazz.getConstructor();
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            ThrowKit.ofRuntimeException(e);
            return null;
        }
    }

    static void checkedSubCmd(int subCmd) {
        var setting = CoreGlobalConfig.setting;
        if (subCmd >= setting.subCmdMaxLen) {
            var info = Bundle.getMessage(MessageKey.cmdMergeLimit).formatted(
                    "subCmd", setting.subCmdMaxLen, subCmd
            );

            IonetBanner.ofRuntimeException(info);
        }
    }

    static void checkedCmd(int cmd) {
        var setting = CoreGlobalConfig.setting;
        if (cmd >= setting.cmdMaxLen) {
            /*
             * %s exceeds the maximum default value.
             * Please set the capacity manually if necessary.
             * Default maximum capacity %d, current capacity %d
             */
            var info = Bundle.getMessage(MessageKey.cmdMergeLimit).formatted(
                    "cmd", setting.cmdMaxLen, cmd
            );

            IonetBanner.ofRuntimeException(info);
        }

    }
}

/**
 * Console printer that renders a human-readable summary of the {@link BarSkeleton} configuration
 * including registered actions, in/out plugins, data codecs, and runners.
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
class PrintActionKit {
    static void print(BarSkeleton barSkeleton) {
        var setting = CoreGlobalConfig.setting;
        if (!setting.print) {
            return;
        }

        if (setting.printInout) {
            PrintActionKit.printInout(barSkeleton.inOuts);
        }

        if (setting.printDataCodec) {
            PrintActionKit.printDataCodec();
        }

        if (setting.printRunners) {
            extractedRunners(barSkeleton);
        }

        if (setting.printAction) {
            PrintActionKit.printActionCommand(barSkeleton.actionCommandRegions.actionCommands, setting.printActionShort);
        }

        IonetBanner.printLine();
    }

    private static void extractedRunners(BarSkeleton barSkeleton) {
        Runners runners = barSkeleton.runners;
        List<String> nameList = runners.listRunnerName();
        String title = "@|CYAN ======================== Runners ========================= |@";
        IonetBanner.println1(Ansi.ansi().render(title));

        var printActionKitClose = Bundle.getMessage(MessageKey.printActionKitPrintClose);
        IonetBanner.printlnMsg(printActionKitClose + " BarSkeletonBuilder.setting.printRunners");

        for (String name : nameList) {
            String info = String.format("@|BLUE %s |@", name);
            IonetBanner.println1(Ansi.ansi().render(info));
        }
    }

    static void printInout(ActionMethodInOut[] inOuts) {
        if (ArrayKit.isEmpty(inOuts)) {
            return;
        }

        var businessFrameworkPlugin = Bundle.getMessage(MessageKey.businessFrameworkPlugin);
        printTitle(businessFrameworkPlugin);

        var printActionKitClose = Bundle.getMessage(MessageKey.printActionKitPrintClose);
        IonetBanner.printlnMsg(printActionKitClose + " BarSkeletonBuilder.setting.printInout");

        for (ActionMethodInOut inOut : inOuts) {
            String info = String.format("@|BLUE %s |@", inOut.getClass());
            IonetBanner.println1(Ansi.ansi().render(info));
        }
    }

    private static void printTitle(String title) {
        String formatted = "@|CYAN ======================== %s ========================= |@".formatted(title);
        IonetBanner.println1(Ansi.ansi().render(formatted));
    }

    static void printActionCommand(ActionCommand[][] behaviors, boolean shortName) {
        printTitle("action");

        var printActionKitClose = Bundle.getMessage(MessageKey.printActionKitPrintClose);
        IonetBanner.printlnMsg(printActionKitClose + " BarSkeletonBuilder.setting.printAction");

        var printActionKitPrintFull = Bundle.getMessage(MessageKey.printActionKitPrintFull);
        IonetBanner.printlnMsg(printActionKitPrintFull + " BarSkeletonBuilder.setting.printActionShort");

        var cmdName = Bundle.getMessage(MessageKey.cmdName);
        var width = 7;
        var cmdNameFormat = "%-" + width + "s";

        for (int cmd = 0; cmd < behaviors.length; cmd++) {
            ActionCommand[] subBehaviors = behaviors[cmd];

            if (Objects.isNull(subBehaviors)) {
                continue;
            }

            for (int subCmd = 0; subCmd < subBehaviors.length; subCmd++) {
                ActionCommand subBehavior = subBehaviors[subCmd];

                if (Objects.isNull(subBehavior)) {
                    continue;
                }

                var actionMethodParameters = subBehavior.actionMethodParameters;
                String paramInfo = "";
                String paramInfoShort = "";

                if (ArrayKit.notEmpty(actionMethodParameters)) {
                    paramInfoShort = ArrayKit.join(actionMethodParameters, ", ");

                    paramInfo = Arrays.stream(actionMethodParameters)
                            .map(theParamInfo -> theParamInfo.toString(true))
                            .collect(Collectors.joining(", "));
                }

                Map<String, Object> params = new HashMap<>();
                params.put("actionName", subBehavior.actionControllerClass.getName());
                params.put("methodName", subBehavior.getActionMethodName());
                params.put("paramInfo", paramInfo);
                params.put("paramInfoShort", paramInfoShort);
                params.put("actionNameShort", subBehavior.actionControllerClass.getSimpleName());

                shortName(params, shortName);

                ActionMethodReturn actionMethodReturn = subBehavior.actionMethodReturn;
                params.put("returnTypeClazz", actionMethodReturn.toString());
                params.put("returnTypeClazzShort", actionMethodReturn.toString(false));

                checkReturnType(actionMethodReturn.returnTypeClass);

                shortName(params, shortName);

                params.put("actionSimpleName", subBehavior.actionControllerClass.getSimpleName());
                params.put("lineNumber", subBehavior.actionCommandDoc.lineNumber);

                CmdInfo cmdInfo = CmdInfo.of(cmd, subCmd);
                String cmdSimpleString = String.format(cmdNameFormat, CmdKit.toSimpleString(cmdInfo));
                params.put("cmdSimpleString", cmdSimpleString);

                String routeCell = Color.green.format(cmdName + ": {cmdSimpleString}", params);
                String actionCell = Color.white.wrap("-- action ");
                String methodNameCell = Color.blue.format("{methodName}", params);
                String paramInfoCell = Color.green.format("{paramInfo}", params);

                String returnCell = Color.defaults.wrap("return");
                String returnValueCell = Color.magenta.format("{returnTypeClazz}", params);

                params.put("routeCell", routeCell);
                params.put("actionCell", actionCell);
                params.put("methodNameCell", methodNameCell);
                params.put("returnCell", returnCell);
                params.put("returnValueCell", returnValueCell);
                params.put("paramInfoCell", paramInfoCell);

                String lineTemplate = "{routeCell}{actionCell} {methodNameCell}({paramInfoCell}) -- return {returnValueCell} -- see.({actionSimpleName}.java:{lineNumber})";
                String text = StrKit.format(lineTemplate, params);
                IonetBanner.println1(Ansi.ansi().render(text));
            }
        }
    }

    static void printDataCodec() {
        var printActionKitDataCodec = Bundle.getMessage(MessageKey.printActionKitDataCodec);
        printTitle(printActionKitDataCodec);

        var printActionKitClose = Bundle.getMessage(MessageKey.printActionKitPrintClose);
        IonetBanner.printlnMsg(printActionKitClose + " BarSkeletonBuilder.setting.printDataCodec");

        var codec = DataCodecManager.getDataCodec();
        String info = String.format("@|BLUE %s - %s |@", codec.codecName(), codec.getClass().getName());
        IonetBanner.println1(Ansi.ansi().render(info));

    }

    private static void shortName(Map<String, Object> params, boolean shortName) {
        if (!shortName) {
            return;
        }

        params.put("paramInfo", params.get("paramInfoShort"));
        params.put("actionName", params.get("actionNameShort"));
        params.put("returnTypeClazz", params.get("returnTypeClazzShort"));
        params.put("actualTypeArgumentClazz", params.get("actualTypeArgumentClazzShort"));
    }

    private static void checkReturnType(final Class<?> returnTypeClazz) {
        if (Set.class.isAssignableFrom(returnTypeClazz) || Map.class.isAssignableFrom(returnTypeClazz)) {
            // No logic for unsupported parameter types for now; just report it.
            // Consider supporting them in the future if needed.
            // Action return values and parameters do not support set, map and basic types!
            var printActionKitCheckReturnType = Bundle.getMessage(MessageKey.printActionKitCheckReturnType);
            ThrowKit.ofRuntimeException(printActionKitCheckReturnType);
        }
    }

    private static class Color {
        String start;
        static final Color red = new Color("@|red");
        static final Color white = new Color("@|white");
        static final Color blue = new Color("@|blue");
        static final Color green = new Color("@|green");
        static final Color defaults = new Color("@|default");
        static final Color magenta = new Color("@|magenta");

        public Color(String start) {
            this.start = start;
        }

        String wrap(String str) {
            return start + " " + str + "|@";
        }

        String format(String template, Map<String, Object> params) {
            String str = StrKit.format(template, params);
            str = wrap(str);
            return str;
        }
    }
}
