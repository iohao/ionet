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

import com.iohao.net.framework.*;
import com.iohao.net.framework.annotations.*;
import com.iohao.net.framework.core.doc.*;
import java.lang.invoke.*;
import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;

/**
 * Action command object parser
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
@Slf4j
final class DefaultActionCommandParser implements ActionCommandParser {
    @Getter
    final ActionCommandRegions actionCommandRegions = new ActionCommandRegions();

    @Override
    public void buildAction(Set<Class<?>> classSet) {
        if (CoreGlobalConfig.setting.parseDoc) {
            ActionCommandDocParserKit.parseDoc(this, classSet);
        }

        var lookup = MethodHandles.lookup();
        ActionCommandParserKit.streamActionController(classSet).forEach(controllerClazz -> {
            Object actionControllerInstance = ActionCommandParserKit.ofActionInstance(controllerClazz);
            int cmd = controllerClazz.getAnnotation(ActionController.class).value();
            ActionCommandParserKit.checkedCmd(cmd);
            // subCmd map
            var actionCommandRegion = this.actionCommandRegions.getActionCommandRegion(cmd);
            actionCommandRegion.actionControllerClazz = controllerClazz;
            // true means it's handed over to a container for management, like Spring.
            boolean deliveryContainer = ActionCommandParserKit.deliveryContainer(controllerClazz);

            var privateLookup = getLookup(controllerClazz, lookup);

            ActionCommandParserKit.streamActionMethod(controllerClazz).forEach(method -> {

                var subCmd = method.getAnnotation(ActionMethod.class).value();
                ActionCommandParserKit.checkedSubCmd(subCmd);

                var methodName = method.getName();
                var actionCommandDoc = ActionCommandDocParserKit.getActionCommandDoc(cmd, subCmd);
                var actionMethodReturn = new ActionMethodReturn(method);

                var builder = ActionCommand.builder()
                        .setCmdInfo(CmdInfo.of(cmd, subCmd))
                        .setActionCommandDoc(actionCommandDoc)
                        .setDeliveryContainer(deliveryContainer)
                        // controller
                        .setActionControllerClass(controllerClazz)
                        .setActionController(actionControllerInstance)
                        // method, return
                        .setMethod(method)
                        .setActionMethodReturn(actionMethodReturn);

                ActionCommandParserKit.checkDuplicateRoute(controllerClazz, subCmd, actionCommandRegion);

                var builderData = new ActionCommandBuilderData();
                var actionParameterPosition = ActionCommandParserKit.parseParameterPosition(method, builderData);
                if (builderData.actionMethodParameters != null) {
                    builder.setActionMethodParameters(builderData.actionMethodParameters);
                }

                if (builderData.dataParameter != null) {
                    builder.setDataParameter(builderData.dataParameter);
                }

                builder.setActionParameterPosition(actionParameterPosition);

                try {
                    var methodType = ActionCommandParserKit.ofMethodType(method, actionMethodReturn.returnTypeClass);
                    var methodHandle = privateLookup.findVirtual(controllerClazz, methodName, methodType);

                    builder.setMethodHandle(methodHandle);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }

                var command = builder.build();
                actionCommandRegion.add(command);

                var actionDoc = DocumentHelper.ofActionDoc(cmd, controllerClazz);
                actionDoc.addActionCommand(command);

                codeSuggest(command);
            });
        });
    }

    private void codeSuggest(ActionCommand command) {
        var codeSuggest = CoreGlobalConfig.setting.codeSuggest;
        if (codeSuggest != null) {
            codeSuggest.inspect(new SuggestInformation(command));
        }
    }

    private MethodHandles.Lookup getLookup(Class<?> controllerClazz, MethodHandles.Lookup lookup) {
        MethodHandles.Lookup privateLookup;
        try {
            privateLookup = MethodHandles.privateLookupIn(controllerClazz, lookup);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return privateLookup;
    }
}
