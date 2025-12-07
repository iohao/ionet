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

import com.iohao.net.framework.core.ActionCommand;
import com.iohao.net.framework.core.ActionMethodParameter;
import com.iohao.net.framework.core.ActionMethodReturn;
import com.iohao.net.framework.core.kit.CmdKit;
import com.iohao.net.framework.core.flow.parser.MethodParsers;
import com.iohao.net.framework.protocol.wrapper.ByteValueList;
import com.iohao.net.common.kit.StrKit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.*;

/**
 * @author 渔民小镇
 * @date 2022-01-29
 */
@FieldDefaults(level = AccessLevel.PACKAGE)
final class DocInfo {
    String actionSimpleName;
    String classComment;
    Map<Integer, BroadcastDocument> broadcastDocumentMap;

    final List<Map<String, String>> subBehaviorList = new ArrayList<>();

    void setHead(ActionCommand subBehavior) {
        ActionCommandDoc actionCommandDoc = subBehavior.actionCommandDoc;
        this.actionSimpleName = subBehavior.actionControllerClass.getSimpleName();
        this.classComment = actionCommandDoc.classComment;
    }

    void add(ActionCommand subBehavior) {
        Map<String, String> paramMap = new HashMap<>(16);
        subBehaviorList.add(paramMap);

        ActionCommandDoc actionCommandDoc = subBehavior.actionCommandDoc;

        int cmd = subBehavior.cmdInfo.cmd();
        int subCmd = subBehavior.cmdInfo.subCmd();
        var actionMethodReturnInfo = subBehavior.actionMethodReturn;

        paramMap.put("cmd", String.valueOf(cmd));
        paramMap.put("subCmd", String.valueOf(subCmd));
        paramMap.put("actionSimpleName", subBehavior.actionControllerClass.getSimpleName());
        paramMap.put("methodName", subBehavior.getActionMethodName());
        paramMap.put("methodComment", actionCommandDoc.comment);
        paramMap.put("methodParam", "");
        paramMap.put("returnTypeClazz", returnToString(actionMethodReturnInfo));
        paramMap.put("lineNumber", String.valueOf(actionCommandDoc.lineNumber));

        if (subBehavior.hasDataParameter()) {
            var dataParam = subBehavior.dataParameter;
            String str = this.paramInfoToString(dataParam);
            paramMap.put("methodParam", str);
        }

        paramMap.put("methodParamComment", actionCommandDoc.methodParamComment);
        paramMap.put("methodReturnComment", actionCommandDoc.methodReturnComment);
    }

    private String paramInfoToString(ActionMethodParameter actionMethodParameter) {
        Class<?> actualClazz = actionMethodParameter.actualClass;
        boolean isCustomList = actionMethodParameter.isList() && !MethodParsers.containsKey(actualClazz);
        return paramResultInfoToString(actualClazz, isCustomList);
    }

    private String returnToString(ActionMethodReturn actionMethodReturn) {
        Class<?> actualClazz = actionMethodReturn.actualClazz;
        boolean isCustomList = actionMethodReturn.isList() && !MethodParsers.containsKey(actualClazz);
        return paramResultInfoToString(actualClazz, isCustomList);
    }

    private String paramResultInfoToString(Class<?> actualClazz, boolean isCustomList) {
        if (isCustomList) {
            /*
             * 因为是生成对接文档，所以不能使用 List<xxx> 来表示，而是使用 ByteValueList<xxx> 来表示。
             * 因为 ByteValueList 是一个类似 IntValueList、LongValueList 这样的包装类
             */
            String simpleName = ByteValueList.class.getSimpleName();
            String simpleNameActualClazz = actualClazz.getSimpleName();
            return String.format("%s<%s>", simpleName, simpleNameActualClazz);
        }

        return actualClazz.getSimpleName();
    }

    String render() {
        if (this.subBehaviorList.isEmpty()) {
            return "";
        }

        String separator = System.lineSeparator();

        List<String> lineList = new ArrayList<>();

        String templateHead = "==================== %s %s ====================";
        lineList.add(String.format(templateHead, this.actionSimpleName, this.classComment));

        String subActionCommandTemplate =
                "route: {cmd} - {subCmd}  --- 【{methodComment}】 --- 【{actionSimpleName}:{lineNumber}】【{methodName}】";

        for (Map<String, String> paramMap : subBehaviorList) {

            String format = StrKit.format(subActionCommandTemplate, paramMap);
            lineList.add(format);

            // 方法参数
            if (StrKit.isNotEmpty(paramMap.get("methodParam"))) {
                format = StrKit.format("    methodParam: {methodParam} {methodParamComment}", paramMap);
                lineList.add(format);
            }

            // 方法返回值
            if (StrKit.isNotEmpty(paramMap.get("returnTypeClazz"))) {
                format = StrKit.format("    returnType: {returnTypeClazz} {methodReturnComment}", paramMap);
                lineList.add(format);
            }

            int cmd = Integer.parseInt(paramMap.get("cmd"));
            int subCmd = Integer.parseInt(paramMap.get("subCmd"));
            int merge = CmdKit.merge(cmd, subCmd);
            var broadcastDocument = this.broadcastDocumentMap.remove(merge);
            if (Objects.nonNull(broadcastDocument)) {
                String dataClassName = broadcastDocument.dataClassName;
                String description = broadcastDocument.methodDescription;
                String dataDescription = broadcastDocument.dataDescription;
                format = String.format("    Broadcast: %s %s，(%s)", dataClassName, dataDescription, description);
                lineList.add(format);
            }

            lineList.add(" ");
        }

        return String.join(separator, lineList);
    }
}
