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
package com.iohao.net.framework.core.doc;

import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.common.kit.CollKit;
import com.iohao.net.common.kit.StrKit;
import com.iohao.net.common.kit.source.SourceClass;
import com.iohao.net.common.kit.source.SourceDocTag;
import com.iohao.net.common.kit.source.SourceMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper around a {@link SourceClass} that indexes its methods by name
 * and provides factory methods for creating {@link ActionCommandDoc} from reflection
 * {@link Method} objects.
 *
 * @author 渔民小镇
 * @date 2022-01-28
 */
public final class JavaClassDocInfo {
    final SourceClass sourceClass;
    Map<String, SourceMethod> sourceMethodMap = new HashMap<>();

    public JavaClassDocInfo(SourceClass sourceClass) {
        this.sourceClass = sourceClass;

        List<SourceMethod> methods = sourceClass.getMethods();
        for (SourceMethod method : methods) {
            sourceMethodMap.put(method.getName(), method);
        }
    }

    public ActionCommandDoc createActionCommandDoc(Method method) {
        SourceMethod sourceMethod = sourceMethodMap.get(method.getName());
        int subCmd = method.getAnnotation(ActionMethod.class).value();

        ActionCommandDoc actionCommandDoc = new ActionCommandDoc();
        actionCommandDoc.subCmd = subCmd;
        actionCommandDoc.classComment = this.sourceClass.getComment();
        actionCommandDoc.classLineNumber = this.sourceClass.getLineNumber();
        actionCommandDoc.comment = sourceMethod.getComment();
        actionCommandDoc.lineNumber = sourceMethod.getLineNumber();

        if (actionCommandDoc.classComment == null) {
            actionCommandDoc.classComment = "";
        }

        if (actionCommandDoc.comment == null) {
            actionCommandDoc.comment = "";
        }

        methodParamReturnComment(actionCommandDoc, sourceMethod);

        return actionCommandDoc;
    }

    private void methodParamReturnComment(ActionCommandDoc actionCommandDoc, SourceMethod sourceMethod) {
        List<SourceDocTag> tags = sourceMethod.getTags();
        if (CollKit.isEmpty(tags)) {
            return;
        }

        for (SourceDocTag tag : tags) {
            String name = tag.name();
            String value = tag.value();

            if (StrKit.isEmpty(value) || value.contains("flowContext")) {
                continue;
            }

            int paramIndex = value.indexOf(" ");
            if ("param".equals(name) && paramIndex != -1) {
                actionCommandDoc.methodParamComment = value.substring(paramIndex).trim();
            } else if ("return".equals(name)) {
                actionCommandDoc.methodReturnComment = value;
            }
        }
    }

    public String getComment() {
        return this.sourceClass.getComment();
    }
}
