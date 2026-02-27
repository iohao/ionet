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
import com.iohao.net.framework.core.exception.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import lombok.experimental.*;
import lombok.extern.slf4j.*;

/**
 * Utility for analysing action documents, error code enums, and Java source files
 * to produce structured documentation models.
 *
 * @author 渔民小镇
 * @date 2024-06-26
 */
@Slf4j
@UtilityClass
public class DocumentAnalyseKit {
    /**
     * Analyse all action documents and produce a list of {@link ActionDocument} models.
     *
     * @param document             the raw document containing action doc list
     * @param typeMappingDocument  the type mapping configuration
     * @return list of analysed action documents (only those with methods)
     */
    public List<ActionDocument> analyseActionDocument(Document document, TypeMappingDocument typeMappingDocument) {
        // map data types to their corresponding values
        return document.actionDocList.stream().map(actionDoc -> {
            // generate action file
            ActionDocument actionDocument = new ActionDocument(actionDoc, typeMappingDocument);
            actionDocument.analyse();
            return actionDocument;
        }).filter(actionDocument -> {
            // keep only actions with non-empty method lists
            List<ActionMethodDocument> actionMethodDocumentList = actionDocument.actionMethodDocumentList;
            return !actionMethodDocumentList.isEmpty();
        }).toList();
    }

    List<ErrorCodeDocument> analyseErrorCodeDocument(Class<? extends ErrorInformation> clazz) {

        var analyseJavaClassRecord = analyseJavaClass(clazz);
        if (!analyseJavaClassRecord.exists) {
            // Special handling for built-in error codes -- source is unavailable after compilation
            return analyseActionErrorEnumDocument(clazz);
        }

        return analyseErrorCodeDocument(analyseJavaClassRecord.sourceClass());
    }

    private List<ErrorCodeDocument> analyseActionErrorEnumDocument(Class<? extends ErrorInformation> clazz) {

        if (!ActionErrorEnum.class.equals(clazz)) {
            return Collections.emptyList();
        }

        return Arrays.stream(ActionErrorEnum.values()).map(code -> {
            ErrorCodeDocument errorCodeDocument = new ErrorCodeDocument();
            errorCodeDocument.name = code.name();
            errorCodeDocument.value = code.getCode();
            errorCodeDocument.description = code.getMessage();

            // i18n
            if (Locale.getDefault() == Locale.US) {
                errorCodeDocument.description = code.name();
            }

            return errorCodeDocument;
        }).toList();
    }

    AnalyseJavaClassRecord analyseJavaClass(Class<?> clazz) {

        URL resource = clazz.getResource(clazz.getSimpleName() + ".class");
        String srcPath = ActionCommandDocKit.sourceFilePathFun.apply(resource).replace("class", "java");

        File file = new File(srcPath);
        // only process if source file exists in this package
        boolean exists = file.exists();

        if (!exists && !ActionErrorEnum.class.equals(clazz)) {
            log.warn("Unable to obtain source code for {}", clazz);
        }

        SourceClass sourceClass = exists
                ? SourceParserKit.parseClass(clazz.getName(), file)
                : new SourceClass(clazz.getName(), null, 0,
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList());

        return new AnalyseJavaClassRecord(exists, sourceClass);
    }

    record AnalyseJavaClassRecord(boolean exists, SourceClass sourceClass) {

    }

    private final AtomicInteger errorCodeOrdinal = new AtomicInteger(0);

    private List<ErrorCodeDocument> analyseErrorCodeDocument(SourceClass sourceClass) {
        return sourceClass.getFields().stream().map(field -> {
            List<Object> enumConstantArguments = field.getEnumConstantArguments();
            if (CollKit.isEmpty(enumConstantArguments)) {
                return null;
            }

            int errorCode = 0;
            if (enumConstantArguments.size() == 1) {
                errorCode = errorCodeOrdinal.getAndIncrement();
            } else if (enumConstantArguments.size() == 2) {
                Object enumValue = enumConstantArguments.getFirst();
                errorCode = Integer.parseInt(enumValue.toString());
            }

            String name = field.getName();

            var errorCodeDocument = new ErrorCodeDocument();
            errorCodeDocument.name = name;
            errorCodeDocument.value = errorCode;

            String description = enumConstantArguments.getLast().toString();
            // strip surrounding quotes if present
            if (description.length() >= 2
                    && description.startsWith("\"") && description.endsWith("\"")) {
                description = description.substring(1, description.length() - 1);
            }
            errorCodeDocument.description = description;

            return errorCodeDocument;
        }).filter(Objects::nonNull).toList();
    }
}
