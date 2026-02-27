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

import com.iohao.net.common.kit.*;
import com.iohao.net.framework.core.*;
import java.util.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Documentation model for a single action method, capturing parameter types, return types,
 * comments, and type-mapping information used for client SDK code generation.
 *
 * @author 渔民小镇
 * @date 2024-06-26
 */
@Getter
@FieldDefaults(level = AccessLevel.PUBLIC)
public final class ActionMethodDocument {
    final ActionCommandDoc actionCommandDoc;
    final TypeMappingDocument typeMappingDocument;
    /** Controller class simple name. */
    final String actionSimpleName;
    /** Action method name (PascalCase). */
    String actionMethodName;
    /** Javadoc comment of the method. */
    final String methodComment;

    /** true if the method has a business data parameter. */
    final boolean hasBizData;
    /** Parameter name. */
    String bizDataName;
    /** Parameter type class. */
    Class<?> bizDataTypeClazz;
    /** Mapped parameter type name. */
    String bizDataType;
    /** Parameter comment. */
    String bizDataComment;
    /** true if the parameter is a List type. */
    boolean bizDataTypeIsList;
    /** true if the parameter type is a built-in protocol fragment. */
    boolean internalBizDataType;
    /** Actual (generic) type name of the parameter. */
    String actualTypeName;

    /** Route member constant name. */
    String memberCmdName;
    /** SDK API method name. */
    String sdkMethodName;

    boolean isVoid;
    /** Return value comment. */
    String returnComment;
    String returnDataName;
    /** Return value type class. */
    Class<?> returnTypeClazz;
    /** Actual (generic) type name of the return value. */
    String returnDataActualTypeName;

    boolean returnDataIsList;
    /** true if the return type is a built-in protocol fragment. */
    boolean returnDataTypeIsInternal;
    /** SDK result getter method name. */
    String resultMethodTypeName;
    /** SDK result list getter method name. */
    String resultMethodListTypeName;

    /**
     * Create a new method document from the given command doc and type mapping.
     *
     * @param actionCommandDoc     the command-level documentation
     * @param typeMappingDocument  the type mapping configuration
     */
    public ActionMethodDocument(ActionCommandDoc actionCommandDoc, TypeMappingDocument typeMappingDocument) {
        this.actionCommandDoc = actionCommandDoc;
        this.typeMappingDocument = typeMappingDocument;

        var actionCommand = actionCommandDoc.actionCommand;
        this.actionSimpleName = actionCommand.actionControllerClass.getSimpleName();

        // method name
        var documentMethod = actionCommand.getAnnotation(DocumentMethod.class);
        if (Objects.nonNull(documentMethod)) {
            this.actionMethodName = StrKit.firstCharToUpperCase(documentMethod.value());
        } else {
            this.actionMethodName = StrKit.firstCharToUpperCase(actionCommand.getActionMethodName());
        }

        // method comment
        this.methodComment = this.actionCommandDoc.comment;

        CmdInfo cmdInfo = actionCommand.cmdInfo;
        this.memberCmdName = "%s_%d_%d".formatted(actionCommand.getActionMethodName(), cmdInfo.cmd(), cmdInfo.subCmd());

        // --------- return value info ---------
        extractedReturnInfo(actionCommand);

        // --------- parameter info ---------
        var bizParam = actionCommand.dataParameter;
        this.hasBizData = Objects.nonNull(bizParam);
        if (this.hasBizData) {
            extractedParamInfo(bizParam, actionCommandDoc);
        }
    }

    private void extractedReturnInfo(ActionCommand actionCommand) {
        // return value comment
        this.returnComment = actionCommandDoc.methodReturnComment;

        var returnInfo = actionCommand.actionMethodReturn;
        this.isVoid = returnInfo.isVoid();
        this.returnDataIsList = returnInfo.isList();

        this.returnTypeClazz = returnInfo.getActualTypeArgumentClass();
        var typeMappingRecord = typeMappingDocument.getTypeMappingRecord(returnTypeClazz);
        this.returnDataName = typeMappingRecord.getParamTypeName();
        this.returnDataTypeIsInternal = typeMappingRecord.isInternalType();
        this.resultMethodTypeName = typeMappingRecord.getResultMethodTypeName();
        this.resultMethodListTypeName = typeMappingRecord.getResultMethodListTypeName();

        this.returnDataActualTypeName = typeMappingRecord.getParamTypeName();
    }

    private void extractedParamInfo(ActionMethodParameter actionMethodParameter, ActionCommandDoc actionCommandDoc) {
        this.bizDataTypeClazz = actionMethodParameter.getActualTypeArgumentClass();
        // method parameter type
        var typeMappingRecord = this.typeMappingDocument.getTypeMappingRecord(bizDataTypeClazz);
        this.bizDataTypeIsList = actionMethodParameter.isList();
        this.internalBizDataType = typeMappingRecord.isInternalType();

        // sdk method name
        this.sdkMethodName = typeMappingRecord.getOfMethodTypeName(this.bizDataTypeIsList);

        this.bizDataType = typeMappingRecord.getParamTypeName(this.bizDataTypeIsList);
        this.bizDataName = actionMethodParameter.name;
        this.bizDataComment = actionCommandDoc.methodParamComment;

        this.actualTypeName = typeMappingRecord.getParamTypeName();
    }
}