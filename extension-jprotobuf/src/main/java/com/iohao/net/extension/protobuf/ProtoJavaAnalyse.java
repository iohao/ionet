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
package com.iohao.net.extension.protobuf;

import com.baidu.bjf.remoting.protobuf.*;
import com.baidu.bjf.remoting.protobuf.annotation.*;
import com.iohao.net.common.kit.*;
import com.iohao.net.common.kit.source.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import lombok.extern.slf4j.*;

/**
 * Analyzes annotated Java classes and converts them into proto generation metadata.
 *
 * @author 渔民小镇
 * @date 2022-01-25
 */
@Slf4j
public class ProtoJavaAnalyse {
    static final Map<String, Map<String, SourceClass>> sourceClassCacheMap = CollKit.ofConcurrentHashMap();
    final Map<ProtoJavaRegionKey, ProtoJavaRegion> protoJavaRegionMap = CollKit.ofConcurrentHashMap();
    final Map<Class<?>, ProtoJava> protoJavaMap = CollKit.ofConcurrentHashMap();
    final Map<String, SourceClass> protoJavaSourceFileMap = CollKit.ofConcurrentHashMap();

    public Map<ProtoJavaRegionKey, ProtoJavaRegion> analyse(String protoPackagePath, String protoSourcePath) {
        return this.analyse(protoPackagePath, protoSourcePath, this.predicateFilter);
    }

    public Map<ProtoJavaRegionKey, ProtoJavaRegion> analyse(String protoPackagePath, String protoSourcePath, Predicate<Class<?>> predicateFilter) {
        var sourceClassMap = getSourceClassMap(protoSourcePath);
        Collection<SourceClass> sourceClassCollection = sourceClassMap.values();

        sourceClassCollection.parallelStream().filter(sourceClass -> {
            List<SourceAnnotation> annotations = sourceClass.getAnnotations();
            if (annotations.size() < 2) {
                return false;
            }

            long count = annotations.stream().filter(annotation -> {
                String typeName = annotation.typeName();
                return typeName.contains(ProtobufClass.class.getName())
                        || typeName.contains(ProtobufClass.class.getSimpleName())
                        || typeName.contains(ProtoFileMerge.class.getName())
                        || typeName.contains(ProtoFileMerge.class.getSimpleName());
            }).count();

            return count >= 2;
        }).forEach(sourceClass -> {
            protoJavaSourceFileMap.put(sourceClass.toString(), sourceClass);

            if (ProtoGenerateSetting.enableLog) {
                log.info("sourceClass: {}", sourceClass);
            }
        });

        ClassScanner classScanner = new ClassScanner(protoPackagePath, predicateFilter);
        List<Class<?>> classList = classScanner.listScan();

        if (classList.isEmpty()) {
            return protoJavaRegionMap;
        }

        List<ProtoJava> protoJavaList = this.convert(classList);
        for (ProtoJava protoJava : protoJavaList) {
            this.analyseField(protoJava);
        }

        return protoJavaRegionMap;
    }

    static Map<String, SourceClass> getSourceClassMap(String protoSourcePath) {
        Map<String, SourceClass> sourceClassMap = sourceClassCacheMap.get(protoSourcePath);
        if (sourceClassMap == null) {
            var parsed = SourceParserKit.parseSourceTree(new File(protoSourcePath));
            return MoreKit.putIfAbsent(sourceClassCacheMap, protoSourcePath, parsed);
        }

        return sourceClassMap;
    }

    private List<ProtoJava> convert(List<Class<?>> classList) {

        return classList.stream().map(clazz -> {
            ProtoFileMerge annotation = clazz.getAnnotation(ProtoFileMerge.class);
            String fileName = annotation.fileName();
            String filePackage = annotation.filePackage();
            SourceClass sourceClass = protoJavaSourceFileMap.get(clazz.getName());

            var protoJava = new ProtoJava();
            protoJava.className = clazz.getSimpleName();
            protoJava.comment = sourceClass.getComment();
            protoJava.clazz = clazz;
            protoJava.fileName = fileName;
            protoJava.filePackage = filePackage;
            protoJava.sourceClass = sourceClass;

            protoJavaMap.put(clazz, protoJava);

            ProtoJavaRegionKey regionKey = new ProtoJavaRegionKey(fileName, filePackage);
            ProtoJavaRegion protoJavaRegion = this.getProtoJavaRegion(regionKey);
            protoJavaRegion.addProtoJava(protoJava);

            return protoJava;
        }).collect(Collectors.toList());
    }

    private void analyseField(ProtoJava protoJava) {
        Class<?> clazz = protoJava.clazz;
        Field[] fields = clazz.isEnum()
                ? Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.getType().isEnum()).toArray(Field[]::new)
                : clazz.getFields();

        SourceClass sourceClass = protoJava.sourceClass;
        // Enum numeric values start from 0, while message field indices start from 1.
        int order = clazz.isEnum() ? 0 : 1;
        var enumConstants = clazz.isEnum() ? clazz.getEnumConstants() : CommonConst.emptyObjects;

        for (int i = 0; i < fields.length; i++) {
            var field = fields[i];
            if (Objects.nonNull(field.getAnnotation(Ignore.class))) {
                continue;
            }

            Class<?> fieldTypeClass = field.getType();
            String fieldName = field.getName();
            SourceField sourceField = sourceClass.getFieldByName(fieldName);

            ProtoJavaField protoJavaField = new ProtoJavaField();
            protoJavaField.repeated = List.class.equals(fieldTypeClass);
            protoJavaField.fieldName = fieldName;
            protoJavaField.comment = sourceField != null ? sourceField.getComment() : null;
            protoJavaField.order = order++;
            protoJavaField.fieldTypeClass = fieldTypeClass;
            protoJavaField.field = field;
            protoJavaField.protoJavaParent = protoJava;

            // Custom enum numeric value
            if (clazz.isEnum() && EnumReadable.class.isAssignableFrom(clazz)) {
                if (enumConstants[i] instanceof EnumReadable r) {
                    protoJavaField.order = r.value();
                }
            }

            protoJava.addProtoJavaFiled(protoJavaField);

            String fieldProtoType = ProtoFieldTypeHolder.getProtoType(fieldTypeClass);
            if (Objects.nonNull(fieldProtoType)) {
                protoJavaField.fieldProtoType = fieldProtoType;
                continue;
            }

            if (protoJavaField.isMap()) {
                processMapFieldProtoJava(protoJavaField);
            } else if (protoJavaField.isList()) {
                processListFieldProtoJava(protoJavaField);
            } else {
                processFieldProtoJava(protoJavaField);
            }
        }
    }

    private String fieldProtoTypeToString(ProtoJavaField protoJavaField, Class<?> fieldTypeClass) {
        String fieldName = protoJavaField.fieldName;
        // This field references another proto object type.
        ProtoJava protoJavaFieldType = this.getFieldProtoJava(fieldTypeClass, fieldName, protoJavaField);
        ProtoJava protoJavaParent = protoJavaField.protoJavaParent;

        String filePackage = protoJavaFieldType.filePackage;
        String className = protoJavaFieldType.className;

        String fieldProtoType;

        if (protoJavaParent.inThisFile(protoJavaFieldType)) {
            // Proto object defined in the same file
            fieldProtoType = className;
        } else {
            ProtoJavaRegionKey regionKey = protoJavaParent.getProtoJavaRegionKey();
            ProtoJavaRegion protoJavaRegion = this.getProtoJavaRegion(regionKey);
            protoJavaRegion.addOtherProtoFile(protoJavaFieldType);
            // Proto object defined in another file
            fieldProtoType = String.format("%s.%s", filePackage, className);
        }

        return fieldProtoType;
    }

    private void processFieldProtoJava(ProtoJavaField protoJavaField) {
        // This field references another proto object type.

        Class<?> fieldTypeClass = protoJavaField.fieldTypeClass;
        String fieldName = protoJavaField.fieldName;

        if (protoJavaField.comment == null) {
            ProtoJava protoJavaFieldType = this.getFieldProtoJava(fieldTypeClass, fieldName, protoJavaField);
            protoJavaField.comment = protoJavaFieldType.comment;
        }

        protoJavaField.fieldProtoType = this.fieldProtoTypeToString(protoJavaField, fieldTypeClass);
    }

    private void processListFieldProtoJava(ProtoJavaField protoJavaField) {
        // Get the generic element type for List<T>.
        ParameterizedType genericType = (ParameterizedType) protoJavaField.field.getGenericType();
        Type[] actualTypeArguments = genericType.getActualTypeArguments();

        Class<?> firstClass = (Class<?>) actualTypeArguments[0];
        String fieldProtoType = ProtoFieldTypeHolder.getProtoType(firstClass);

        if (fieldProtoType == null) {
            fieldProtoType = this.fieldProtoTypeToString(protoJavaField, firstClass);
        }

        protoJavaField.repeated = true;
        protoJavaField.fieldProtoType = fieldProtoType;
    }

    private void processMapFieldProtoJava(ProtoJavaField protoJavaField) {

        Map<String, String> map = new HashMap<>();

        // map type
        Field field = protoJavaField.field;

        // Get the generic types for Map<K, V>.
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        Type[] actualTypeArguments = genericType.getActualTypeArguments();

        Class<?> keyClass = (Class<?>) actualTypeArguments[0];
        String keyFieldProtoType = ProtoFieldTypeHolder.getProtoType(keyClass);
        map.put("keyStr", keyFieldProtoType);

        if (keyFieldProtoType == null) {
            // Key is a proto object type
            String keyStr = this.fieldProtoTypeToString(protoJavaField, keyClass);
            map.put("keyStr", keyStr);
        }

        Class<?> valueClass = (Class<?>) actualTypeArguments[1];
        String valueFieldProtoType = ProtoFieldTypeHolder.getProtoType(valueClass);
        map.put("valueStr", valueFieldProtoType);
        if (valueFieldProtoType == null) {
            // Value is a proto object type
            String valueStr = this.fieldProtoTypeToString(protoJavaField, valueClass);
            map.put("valueStr", valueStr);
        }

        protoJavaField.fieldProtoType = StrKit.format("map<{keyStr},{valueStr}>", map);
    }

    private ProtoJava getFieldProtoJava(Class<?> fieldTypeClass, String fieldName, ProtoJavaField protoJavaField) {

        if (!predicateFilter.test(fieldTypeClass)) {

            String templateErr = """
                    %s.%s class type not is protobuf !
                    class must import annotation %s
                    class must import annotation %s
                    """;

            String errorMsg = String.format(templateErr
                    , protoJavaField.protoJavaParent.className
                    , fieldName
                    , ProtobufClass.class
                    , ProtoFileMerge.class
            );

            throw new RuntimeException(errorMsg);
        }

        return this.protoJavaMap.get(fieldTypeClass);
    }

    private final Predicate<Class<?>> predicateFilter = (clazz) -> {
        ProtobufClass annotation = clazz.getAnnotation(ProtobufClass.class);
        ProtoFileMerge protoFileMerge = clazz.getAnnotation(ProtoFileMerge.class);

        return Objects.nonNull(annotation) && Objects.nonNull(protoFileMerge);
    };

    private ProtoJavaRegion getProtoJavaRegion(ProtoJavaRegionKey key) {
        ProtoJavaRegion protoJavaRegion = protoJavaRegionMap.get(key);

        if (protoJavaRegion == null) {

            protoJavaRegion = new ProtoJavaRegion();

            protoJavaRegion = protoJavaRegionMap.putIfAbsent(key, protoJavaRegion);

            if (protoJavaRegion == null) {
                protoJavaRegion = protoJavaRegionMap.get(key);
            }

            protoJavaRegion.fileName = key.fileName();
            protoJavaRegion.filePackage = key.filePackage();
        }

        return protoJavaRegion;
    }
}
