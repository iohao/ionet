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

import com.iohao.net.framework.i18n.Bundle;
import com.iohao.net.framework.i18n.MessageKey;
import com.iohao.net.common.kit.FileKit;
import com.iohao.net.common.kit.StrKit;
import com.iohao.net.common.kit.time.TimeFormatKit;
import lombok.Setter;

import java.io.File;
import java.util.*;

/**
 * {@link DocumentGenerate} implementation that produces a plain-text documentation file
 * containing action routes, broadcast routes, and error codes.
 * <p>
 * The output is written to {@link #path} (defaults to {@code doc_temp.txt} in the working directory).
 *
 * @author 渔民小镇
 * @date 2024-06-25
 */
public final class TextDocumentGenerate implements DocumentGenerate {
    private final StringJoiner docContentJoiner = new StringJoiner(System.lineSeparator());
    @Setter
    String path = System.getProperty("user.dir") + File.separator + "doc_temp.txt";

    @Override
    public void generate(Document document) {
        this.documentURLDescription();

        Map<Integer, BroadcastDocument> broadcastDocumentMap = new TreeMap<>();
        document.broadcastDocumentList.forEach(broadcastDocument -> {
            // map put
            broadcastDocumentMap.put(broadcastDocument.getCmdMerge(), broadcastDocument);
        });

        // generate document - action
        document.actionDocList.forEach(actionDoc -> {
            var docInfo = new DocInfo();

            actionDoc.stream()
                    .map(doc -> doc.actionCommand)
                    .filter(Objects::nonNull)
                    .filter(actionCommand -> {
                        var cmdInfo = actionCommand.cmdInfo;
                        var authentication = DocumentHelper.getDocumentAccessAuthentication();
                        var cmdMerge = cmdInfo.cmdMerge();
                        // route access permission control
                        return !authentication.reject(cmdMerge);
                    }).forEach(subBehavior -> {
                        docInfo.setHead(subBehavior);
                        docInfo.add(subBehavior);
                    });

            if (docInfo.subBehaviorList.isEmpty()) {
                return;
            }

            docInfo.broadcastDocumentMap = broadcastDocumentMap;
            String render = docInfo.render();
            this.docContentJoiner.add(render);
        });

        // broadcast document
        extractedBroadcastDoc(broadcastDocumentMap);

        // error code document
        extractedErrorCode(document);

        String docText = this.docContentJoiner.toString();
        FileKit.writeUtf8String(docText, path);
    }

    private void documentURLDescription() {
        String title = Bundle.getMessage(MessageKey.textDocumentTitle);

        String formatted = """
                ==================== %s ====================
                https://iohao.github.io/ionet/docs/examples/code_generate
                """.formatted(title);

        this.docContentJoiner.add("generate %s".formatted(TimeFormatKit.format()));
        this.docContentJoiner.add(formatted);
    }

    private void extractedBroadcastDoc(Map<Integer, BroadcastDocument> broadcastDocumentMap) {

        var broadcastDocumentList = broadcastDocumentMap.values();
        if (broadcastDocumentList.isEmpty()) {
            return;
        }

        String title = Bundle.getMessage(MessageKey.textDocumentBroadcastTitle);
        this.docContentJoiner.add("==================== %s ====================".formatted(title));


        for (BroadcastDocument broadcastDocument : broadcastDocumentList) {

            String template = "{textDocumentCmd}: {cmd} - {subCmd}  --- {textDocumentBroadcast}: {dataClass} {dataDescription}";

            if (StrKit.isNotEmpty(broadcastDocument.methodDescription)) {
                template = "{textDocumentCmd}: {cmd} - {subCmd}  --- {textDocumentBroadcast}: {dataClass} {dataDescription}，({description})";
            }

            var paramMap = toMap(broadcastDocument);

            String format = StrKit.format(template, paramMap);
            this.docContentJoiner.add(format);
        }

        this.docContentJoiner.add("");
    }

    private HashMap<Object, Object> toMap(BroadcastDocument broadcastDocument) {
        String textDocumentCmd = Bundle.getMessage(MessageKey.textDocumentCmd);
        String textDocumentBroadcast = Bundle.getMessage(MessageKey.textDocumentBroadcast);

        var map = new HashMap<>();
        map.put("cmd", broadcastDocument.getCmd());
        map.put("subCmd", broadcastDocument.getSubCmd());
        map.put("dataClass", broadcastDocument.dataClassName);
        map.put("description", broadcastDocument.methodDescription);
        map.put("dataDescription", broadcastDocument.dataDescription);
        map.put("textDocumentCmd", textDocumentCmd);
        map.put("textDocumentBroadcast", textDocumentBroadcast);

        return map;
    }

    private void extractedErrorCode(Document document) {
        String title = Bundle.getMessage(MessageKey.textDocumentErrorCodeTitle);

        this.docContentJoiner.add("==================== %s ====================".formatted(title));

        for (ErrorCodeDocument errorCodeDocument : document.errorCodeDocumentList) {
            String format = "%s : %s : %s".formatted(errorCodeDocument.value,
                    errorCodeDocument.description,
                    errorCodeDocument.name
            );

            this.docContentJoiner.add(format);
        }
    }
}
