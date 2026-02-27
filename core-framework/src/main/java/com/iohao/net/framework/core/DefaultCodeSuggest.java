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

import com.iohao.net.framework.i18n.*;
import com.iohao.net.framework.protocol.wrapper.*;
import java.util.*;

/**
 * Default {@link CodeSuggest} implementation that recommends replacing wrapper types
 * (e.g. {@link IntValue}) with their primitive or collection equivalents in action
 * method parameters and return types.
 *
 * @author 渔民小镇
 * @date 2025-10-13
 * @since 25.1
 */
public class DefaultCodeSuggest implements CodeSuggest {
    private final Map<Class<?>, String> wrapperTypeMap = Map.of(
            IntValueList.class, "List<Integer>"
            , LongValueList.class, "List<Long>"
            , BoolValueList.class, "List<Boolean>"
            , StringValueList.class, "List<String>"

            , IntValue.class, "int"
            , LongValue.class, "long"
            , BoolValue.class, "boolean"
            , StringValue.class, "String"
    );


    @Override
    public void inspect(SuggestInformation suggest) {
        inspectParameter(suggest);
        inspectReturn(suggest);
    }

    private void inspectReturn(SuggestInformation suggest) {
        var command = suggest.command;

        var actionMethodReturn = command.actionMethodReturn;
        if (actionMethodReturn.isVoid()) {
            return;
        }

        Class<?> returnTypeClazz = actionMethodReturn.returnTypeClass;
        String recommendType = wrapperTypeMap.get(returnTypeClazz);
        if (recommendType == null) {
            return;
        }

        suggest.see(Bundle.getMessage(MessageKey.codeSuggestMethodReturn).formatted(
                returnTypeClazz.getSimpleName(), recommendType
        ));
    }

    private void inspectParameter(SuggestInformation suggest) {
        var command = suggest.command;
        if (!command.hasDataParameter()) {
            return;
        }

        var dataParameter = command.dataParameter;
        var parameterClass = dataParameter.parameterClass;

        String recommendType = wrapperTypeMap.get(parameterClass);
        if (recommendType == null) {
            return;
        }

        suggest.see(Bundle.getMessage(MessageKey.codeSuggestMethodParameter).formatted(
                dataParameter.name, recommendType
        ));
    }
}
