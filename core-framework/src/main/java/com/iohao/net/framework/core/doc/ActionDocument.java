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

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregated documentation model for a single {@link ActionDoc}, containing the
 * generated member-constant documents and method-level documents used for
 * client SDK code generation.
 *
 * @author 渔民小镇
 * @date 2024-06-26
 * @since 21.11
 */
@FieldDefaults(level = AccessLevel.PUBLIC)
public final class ActionDocument {
    final ActionDoc actionDoc;
    final TypeMappingDocument typeMappingDocument;

    List<ActionMemberCmdDocument> actionMemberCmdDocumentList = new ArrayList<>();
    List<ActionMethodDocument> actionMethodDocumentList = new ArrayList<>();

    ActionDocument(ActionDoc actionDoc, TypeMappingDocument typeMappingDocument) {
        this.actionDoc = actionDoc;
        this.typeMappingDocument = typeMappingDocument;
    }

    void analyse() {
        actionDoc.stream().forEach(actionCommandDoc -> {
            var cmdInfo = actionCommandDoc.actionCommand.cmdInfo;
            var authentication = DocumentHelper.getDocumentAccessAuthentication();
            var cmdMerge = cmdInfo.cmdMerge();
            if (authentication.reject(cmdMerge)) {
                return;
            }

            // member
            actionMemberCmdDocumentList.add(generateMemberCmdCode(actionCommandDoc));
            // method
            var actionMethodDocument = new ActionMethodDocument(actionCommandDoc, typeMappingDocument);
            actionMethodDocumentList.add(actionMethodDocument);
        });
    }

    private ActionMemberCmdDocument generateMemberCmdCode(ActionCommandDoc actionCommandDoc) {
        var actionCommand = actionCommandDoc.actionCommand;

        var cmdInfo = actionCommand.cmdInfo;
        var cmd = cmdInfo.cmd();
        var subCmd = cmdInfo.subCmd();

        var comment = actionCommandDoc.comment;
        var actionMethodName = actionCommand.getActionMethodName();
        var memberName = "%s_%d_%d".formatted(actionMethodName, cmd, subCmd);

        return new ActionMemberCmdDocument(cmd, subCmd, memberName, comment);
    }
}
