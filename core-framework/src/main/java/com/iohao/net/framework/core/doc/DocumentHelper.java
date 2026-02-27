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
import com.iohao.net.common.kit.source.*;
import com.iohao.net.framework.*;
import com.iohao.net.framework.core.exception.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Central helper for collecting action documentation, broadcast documents, error codes,
 * and triggering document generation.
 * <p>
 * Usage example:
 * <pre>{@code
 *     // Add a custom document generator
 *     DocumentHelper.addDocumentGenerate(new YourDocumentGenerate());
 *     // Add error code enum class for error code documentation
 *     DocumentHelper.addErrorCodeClass(YourErrorCode.class);
 *     // Generate documents
 *     DocumentHelper.generateDocument();
 * }</pre>
 *
 * @author 渔民小镇
 * @date 2024-07-05
 * @see DocumentGenerate
 */
@UtilityClass
public class DocumentHelper {
    /**
     * Action documentation map.
     * <pre>
     *      key : action controller class
     *      value : action doc
     *  </pre>
     */
    private final Map<Class<?>, ActionDoc> actionDocMap = CollKit.ofConcurrentHashMap();
    /** Error code enum classes used for generating error code documentation. */
    private final Set<Class<? extends ErrorInformation>> errorCodeClassSet = CollKit.ofConcurrentSet();
    private final Set<DocumentGenerate> documentGenerateSet = CollKit.ofConcurrentSet();
    private final List<BroadcastDocument> broadcastDocumentList = new CopyOnWriteArrayList<>();

    /** true to enable document generation. */
    private boolean generateDoc = true;
    private boolean once = true;
    /** Access authentication control for document routes. */
    @Getter
    @Setter
    private DocumentAccessAuthentication documentAccessAuthentication = cmdMerge -> false;

    /**
     * Enable or disable document generation.
     *
     * @param generateDoc false to suppress document generation
     */
    public void setGenerateDoc(boolean generateDoc) {
        DocumentHelper.generateDoc = generateDoc;
    }

    /**
     * Trigger document generation (idempotent -- only runs once).
     */
    public void generateDocument() {
        if (!DocumentHelper.generateDoc || !once) {
            return;
        }

        // generate only once
        once = false;

        if (!CoreGlobalConfig.setting.parseDoc) {
            throw new RuntimeException("CoreGlobalConfig.setting.parseDoc must be true!");
        }

        // analyse documents
        Document document = analyse();

        // generate documents
        documentGenerateSet.forEach(documentGenerate -> documentGenerate.generate(document));
    }

    private Document analyse() {
        // add text document generator
        DocumentHelper.addDocumentGenerate(new TextDocumentGenerate());

        Document document = new Document();

        // ------- error code analysis -------
        // built-in error codes provided by the framework
        DocumentHelper.addErrorCodeClass(ActionErrorEnum.class);

        document.errorCodeDocumentList = DocumentHelper.errorCodeClassSet
                .stream()
                .flatMap(clazz -> DocumentAnalyseKit.analyseErrorCodeDocument(clazz).stream())
                .sorted(Comparator.comparingInt(o -> o.value))
                .toList();

        // ------- broadcast analysis -------
        document.broadcastDocumentList = DocumentHelper.broadcastDocumentList;
        document.broadcastDocumentList.sort(Comparator.comparingInt(BroadcastDocument::getCmdMerge));
        document.broadcastDocumentList
                .stream()
                .filter(broadcastDocument -> Objects.nonNull(broadcastDocument.dataClass))
                .filter(broadcastDocument -> StrKit.isEmpty(broadcastDocument.dataDescription))
                .forEach(broadcastDocument -> {
                    // resolve broadcast data description from class-level Javadoc comment
                    Class<?> dataClass = broadcastDocument.dataClass;
                    SourceClass sourceClass = DocumentAnalyseKit.analyseJavaClass(dataClass).sourceClass();
                    broadcastDocument.dataDescription = sourceClass.getComment();
                });

        // ------- action analysis -------
        document.actionDocList = DocumentHelper.actionDocMap
                .values()
                .stream()
                .sorted(Comparator
                        .comparingInt((ToIntFunction<ActionDoc>) o -> o.cmd)
                        .thenComparing(o -> o.controllerClazz.getName())
                ).toList();

        return document;
    }

    /**
     * Register a document generator. Only one instance per type is kept.
     *
     * @param documentGenerate the document generator
     */
    public void addDocumentGenerate(DocumentGenerate documentGenerate) {
        documentGenerateSet.add(documentGenerate);
    }

    /**
     * Register an error code enum class for documentation.
     *
     * @param clazz the error code enum class
     */
    public void addErrorCodeClass(Class<? extends ErrorInformation> clazz) {
        DocumentHelper.errorCodeClassSet.add(clazz);
    }

    /**
     * Register a broadcast document.
     *
     * @param broadcastDocument the broadcast document
     */
    public void addBroadcastDocument(BroadcastDocument broadcastDocument) {
        DocumentHelper.broadcastDocumentList.add(broadcastDocument);
    }

    /**
     * Register a broadcast document from a builder.
     *
     * @param broadcastDocumentBuilder the broadcast document builder
     */
    public void addBroadcastDocument(BroadcastDocumentBuilder broadcastDocumentBuilder) {
        addBroadcastDocument(broadcastDocumentBuilder.build());
    }

    /**
     * Obtain or create an {@link ActionDoc} for the given command and controller class.
     *
     * @param cmd             the primary command ID
     * @param controllerClazz the action controller class
     * @return the existing or newly created {@link ActionDoc}, never null
     */
    public ActionDoc ofActionDoc(int cmd, Class<?> controllerClazz) {
        ActionDoc actionDocRegion = DocumentHelper.actionDocMap.get(controllerClazz);

        if (actionDocRegion == null) {
            return MoreKit.putIfAbsent(actionDocMap, controllerClazz, new ActionDoc(cmd, controllerClazz));
        }

        return actionDocRegion;
    }
}
