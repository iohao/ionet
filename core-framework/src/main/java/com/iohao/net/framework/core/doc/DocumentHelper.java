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

import com.iohao.net.framework.CoreGlobalConfig;
import com.iohao.net.framework.core.exception.ActionErrorEnum;
import com.iohao.net.framework.core.exception.ErrorInformation;
import com.iohao.net.common.kit.CollKit;
import com.iohao.net.common.kit.MoreKit;
import com.iohao.net.common.kit.StrKit;
import com.thoughtworks.qdox.model.JavaClass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.ToIntFunction;

/**
 * DocumentHelper
 * <p>
 * for example
 * <pre>{@code
 *     // 添加自定义的文档生成器
 *     DocumentHelper.addDocumentGenerate(new YourDocumentGenerate());
 *     // 添加枚举错误码 class，用于生成错误码相关信息
 *     DocumentHelper.addErrorCodeClass(YourErrorCode.class);
 *     // 生成文档
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
     * action 文档相关信息
     * <pre>
     *      key : action controller
     *      value : action 文档
     *  </pre>
     */
    private final Map<Class<?>, ActionDoc> actionDocMap = CollKit.ofConcurrentHashMap();
    /** 错误码枚举类信息，用于生成错误码相关信息 */
    private final Set<Class<? extends ErrorInformation>> errorCodeClassSet = CollKit.ofConcurrentSet();
    private final Set<DocumentGenerate> documentGenerateSet = CollKit.ofConcurrentSet();
    private final List<BroadcastDocument> broadcastDocumentList = new CopyOnWriteArrayList<>();

    /** true 生成文档 */
    private boolean generateDoc = true;
    private boolean once = true;
    /** 文档路由访问权限控制 */
    @Getter
    @Setter
    private DocumentAccessAuthentication documentAccessAuthentication = cmdMerge -> false;

    /**
     * 当为 false 时，将不会生成文档
     *
     * @param generateDoc generateDoc
     */
    public void setGenerateDoc(boolean generateDoc) {
        DocumentHelper.generateDoc = generateDoc;
    }

    /**
     * 对接文档生成
     */
    public void generateDocument() {
        if (!DocumentHelper.generateDoc || !once) {
            return;
        }

        // 只生成一次
        once = false;

        if (!CoreGlobalConfig.setting.parseDoc) {
            throw new RuntimeException("CoreGlobalConfig.setting.parseDoc must be true!");
        }

        // 文档解析
        Document document = analyse();

        // 文档生成
        documentGenerateSet.forEach(documentGenerate -> documentGenerate.generate(document));
    }

    private Document analyse() {
        // 添加文本文档解析器
        DocumentHelper.addDocumentGenerate(new TextDocumentGenerate());

        Document document = new Document();

        // ------- 错误码解析 -------
        // 框架默认提供的错误码
        DocumentHelper.addErrorCodeClass(ActionErrorEnum.class);

        document.errorCodeDocumentList = DocumentHelper.errorCodeClassSet
                .stream()
                .flatMap(clazz -> DocumentAnalyseKit.analyseErrorCodeDocument(clazz).stream())
                .sorted(Comparator.comparingInt(o -> o.value))
                .toList();

        // ------- 广播解析 -------
        document.broadcastDocumentList = DocumentHelper.broadcastDocumentList;
        document.broadcastDocumentList.sort(Comparator.comparingInt(BroadcastDocument::getCmdMerge));
        document.broadcastDocumentList
                .stream()
                .filter(broadcastDocument -> Objects.nonNull(broadcastDocument.dataClass))
                .filter(broadcastDocument -> StrKit.isEmpty(broadcastDocument.dataDescription))
                .forEach(broadcastDocument -> {
                    // 广播业务数据解析，使用类信息的文档注释
                    Class<?> dataClass = broadcastDocument.dataClass;
                    JavaClass javaClass = DocumentAnalyseKit.analyseJavaClass(dataClass).javaClass();
                    broadcastDocument.dataDescription = javaClass.getComment();
                });

        // ------- action 解析 -------
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
     * 添加文档生成器，相同类型只能添加一个
     *
     * @param documentGenerate 文档生成接口
     */
    public void addDocumentGenerate(DocumentGenerate documentGenerate) {
        documentGenerateSet.add(documentGenerate);
    }

    /**
     * 添加枚举错误码 class
     *
     * @param clazz 枚举错误码 class
     */
    public void addErrorCodeClass(Class<? extends ErrorInformation> clazz) {
        DocumentHelper.errorCodeClassSet.add(clazz);
    }

    /**
     * 添加广播文档
     *
     * @param broadcastDocument broadcastDocument
     */
    public void addBroadcastDocument(BroadcastDocument broadcastDocument) {
        DocumentHelper.broadcastDocumentList.add(broadcastDocument);
    }

    /**
     * 添加广播文档
     *
     * @param broadcastDocumentBuilder broadcastDocumentBuilder
     */
    public void addBroadcastDocument(BroadcastDocumentBuilder broadcastDocumentBuilder) {
        addBroadcastDocument(broadcastDocumentBuilder.build());
    }

    /**
     * 获取 ActionDoc，如果 ActionDoc 不存在则创建
     *
     * @param cmd             主路由
     * @param controllerClazz action class
     * @return 一定不为 null
     */
    public ActionDoc ofActionDoc(int cmd, Class<?> controllerClazz) {
        ActionDoc actionDocRegion = DocumentHelper.actionDocMap.get(controllerClazz);

        if (Objects.isNull(actionDocRegion)) {
            return MoreKit.putIfAbsent(actionDocMap, controllerClazz, new ActionDoc(cmd, controllerClazz));
        }

        return actionDocRegion;
    }
}
