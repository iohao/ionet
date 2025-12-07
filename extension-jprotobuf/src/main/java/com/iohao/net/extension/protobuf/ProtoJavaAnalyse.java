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

import com.baidu.bjf.remoting.protobuf.EnumReadable;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.net.common.kit.CommonConst;
import com.iohao.net.common.kit.ClassScanner;
import com.iohao.net.common.kit.CollKit;
import com.iohao.net.common.kit.MoreKit;
import com.iohao.net.common.kit.StrKit;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaAnnotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author 渔民小镇
 * @date 2022-01-25
 */
@Slf4j
public class ProtoJavaAnalyse {
    static final Map<String, JavaProjectBuilder> javaProjectBuilderMap = CollKit.ofConcurrentHashMap();
    final Map<ProtoJavaRegionKey, ProtoJavaRegion> protoJavaRegionMap = CollKit.ofConcurrentHashMap();
    final Map<Class<?>, ProtoJava> protoJavaMap = CollKit.ofConcurrentHashMap();
    final Map<String, JavaClass> protoJavaSourceFileMap = CollKit.ofConcurrentHashMap();

    public Map<ProtoJavaRegionKey, ProtoJavaRegion> analyse(String protoPackagePath, String protoSourcePath) {
        return this.analyse(protoPackagePath, protoSourcePath, this.predicateFilter);
    }

    public Map<ProtoJavaRegionKey, ProtoJavaRegion> analyse(String protoPackagePath, String protoSourcePath, Predicate<Class<?>> predicateFilter) {
        var javaProjectBuilder = getJavaProjectBuilder(protoSourcePath);
        Collection<JavaClass> javaClassCollection = javaProjectBuilder.getClasses();

        javaClassCollection.parallelStream().filter(javaClass -> {
            List<JavaAnnotation> annotations = javaClass.getAnnotations();
            if (annotations.size() < 2) {
                return false;
            }

            long count = annotations.stream().filter(annotation -> {
                String string = annotation.getType().toString();
                return string.contains(ProtobufClass.class.getName())
                        || string.contains(ProtoFileMerge.class.getName());
            }).count();

            return count >= 2;
        }).forEach(javaClass -> {
            protoJavaSourceFileMap.put(javaClass.toString(), javaClass);

            if (ProtoGenerateSetting.enableLog) {
                log.info("javaClass: {}", javaClass);
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

    static JavaProjectBuilder getJavaProjectBuilder(String protoSourcePath) {
        JavaProjectBuilder javaProjectBuilder = javaProjectBuilderMap.get(protoSourcePath);
        if (javaProjectBuilder == null) {
            var builder = new JavaProjectBuilder();
            builder.setEncoding(StandardCharsets.UTF_8.name());
            builder.addSourceTree(new File(protoSourcePath));
            return MoreKit.putIfAbsent(javaProjectBuilderMap, protoSourcePath, builder);
        }

        return javaProjectBuilder;
    }

    private List<ProtoJava> convert(List<Class<?>> classList) {

        return classList.stream().map(clazz -> {
            ProtoFileMerge annotation = clazz.getAnnotation(ProtoFileMerge.class);
            String fileName = annotation.fileName();
            String filePackage = annotation.filePackage();
            JavaClass javaClass = protoJavaSourceFileMap.get(clazz.toString());

            var protoJava = new ProtoJava();
            protoJava.className = clazz.getSimpleName();
            protoJava.comment = javaClass.getComment();
            protoJava.clazz = clazz;
            protoJava.fileName = fileName;
            protoJava.filePackage = filePackage;
            protoJava.javaClass = javaClass;

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

        JavaClass javaClass = protoJava.javaClass;
        // 枚举 enum 的下标从 0 开始，message 的下标从 1 开始
        int order = clazz.isEnum() ? 0 : 1;
        var enumConstants = clazz.isEnum() ? clazz.getEnumConstants() : CommonConst.emptyObjects;

        for (int i = 0; i < fields.length; i++) {
            var field = fields[i];
            if (Objects.nonNull(field.getAnnotation(Ignore.class))) {
                continue;
            }

            Class<?> fieldTypeClass = field.getType();
            String fieldName = field.getName();
            JavaField javaField = javaClass.getFieldByName(fieldName);

            ProtoJavaField protoJavaField = new ProtoJavaField();
            protoJavaField.repeated = List.class.equals(fieldTypeClass);
            protoJavaField.fieldName = fieldName;
            protoJavaField.comment = javaField.getComment();
            protoJavaField.order = order++;
            protoJavaField.fieldTypeClass = fieldTypeClass;
            protoJavaField.field = field;
            protoJavaField.protoJavaParent = protoJava;

            // 自定义枚举值
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
        // 这个字段是一个 proto 对象类型
        ProtoJava protoJavaFieldType = this.getFieldProtoJava(fieldTypeClass, fieldName, protoJavaField);
        ProtoJava protoJavaParent = protoJavaField.protoJavaParent;

        String filePackage = protoJavaFieldType.filePackage;
        String className = protoJavaFieldType.className;

        String fieldProtoType;

        if (protoJavaParent.inThisFile(protoJavaFieldType)) {
            // 同一个文件的 proto 对象
            fieldProtoType = className;
        } else {
            ProtoJavaRegionKey regionKey = protoJavaParent.getProtoJavaRegionKey();
            ProtoJavaRegion protoJavaRegion = this.getProtoJavaRegion(regionKey);
            protoJavaRegion.addOtherProtoFile(protoJavaFieldType);
            // 不在同一个文件中
            fieldProtoType = String.format("%s.%s", filePackage, className);
        }

        return fieldProtoType;
    }

    private void processFieldProtoJava(ProtoJavaField protoJavaField) {
        // 这个字段是一个 proto 对象类型

        Class<?> fieldTypeClass = protoJavaField.fieldTypeClass;
        String fieldName = protoJavaField.fieldName;

        if (Objects.isNull(protoJavaField.comment)) {
            ProtoJava protoJavaFieldType = this.getFieldProtoJava(fieldTypeClass, fieldName, protoJavaField);
            protoJavaField.comment = protoJavaFieldType.comment;
        }

        protoJavaField.fieldProtoType = this.fieldProtoTypeToString(protoJavaField, fieldTypeClass);
    }

    private void processListFieldProtoJava(ProtoJavaField protoJavaField) {
        // 获取 map 的 <k,v> 类型
        ParameterizedType genericType = (ParameterizedType) protoJavaField.field.getGenericType();
        Type[] actualTypeArguments = genericType.getActualTypeArguments();

        Class<?> firstClass = (Class<?>) actualTypeArguments[0];
        String fieldProtoType = ProtoFieldTypeHolder.getProtoType(firstClass);

        if (Objects.isNull(fieldProtoType)) {
            fieldProtoType = this.fieldProtoTypeToString(protoJavaField, firstClass);
        }

        protoJavaField.repeated = true;
        protoJavaField.fieldProtoType = fieldProtoType;
    }

    private void processMapFieldProtoJava(ProtoJavaField protoJavaField) {

        Map<String, String> map = new HashMap<>();

        // map 类型
        Field field = protoJavaField.field;

        // 获取 map 的 <k,v> 类型
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        Type[] actualTypeArguments = genericType.getActualTypeArguments();

        Class<?> keyClass = (Class<?>) actualTypeArguments[0];
        String keyFieldProtoType = ProtoFieldTypeHolder.getProtoType(keyClass);
        map.put("keyStr", keyFieldProtoType);

        if (Objects.isNull(keyFieldProtoType)) {
            // key 是一个 proto 对象类型
            String keyStr = this.fieldProtoTypeToString(protoJavaField, keyClass);
            map.put("keyStr", keyStr);
        }

        Class<?> valueClass = (Class<?>) actualTypeArguments[1];
        String valueFieldProtoType = ProtoFieldTypeHolder.getProtoType(valueClass);
        map.put("valueStr", valueFieldProtoType);
        if (Objects.isNull(valueFieldProtoType)) {
            // value 是一个 proto 对象类型
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

        if (Objects.isNull(protoJavaRegion)) {

            protoJavaRegion = new ProtoJavaRegion();

            protoJavaRegion = protoJavaRegionMap.putIfAbsent(key, protoJavaRegion);

            if (Objects.isNull(protoJavaRegion)) {
                protoJavaRegion = protoJavaRegionMap.get(key);
            }

            protoJavaRegion.fileName = key.fileName();
            protoJavaRegion.filePackage = key.filePackage();
        }

        return protoJavaRegion;
    }
}
