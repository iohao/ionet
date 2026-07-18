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

import com.iohao.net.common.kit.*;
import com.iohao.net.common.kit.source.*;
import java.util.*;
import java.util.stream.*;

/**
 * Parsed Java class metadata used to render a proto message/enum block.
 *
 * @author 渔民小镇
 * @date 2022-01-24
 */
public class ProtoJava {
    Class<?> clazz;
    String className;

    String comment;

    String fileName;
    String filePackage;

    SourceClass sourceClass;

    List<ProtoJavaField> protoJavaFieldList = new ArrayList<>(16);

    public void addProtoJavaFiled(ProtoJavaField protoJavaField) {
        this.protoJavaFieldList.add(protoJavaField);
    }

    public boolean inThisFile(ProtoJava protoJava) {
        return Objects.equals(this.fileName, protoJava.fileName) && Objects.equals(this.filePackage, protoJava.filePackage);
    }

    public ProtoJavaRegionKey getProtoJavaRegionKey() {
        return new ProtoJavaRegionKey(this.fileName, this.filePackage);

    }

    public String toProtoMessage() {

        String fieldsString = protoJavaFieldList
                .stream()
                .map(ProtoJavaField::toProtoFieldLine)
                .collect(Collectors.joining("\n"));

        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("className", this.className);
        messageMap.put("fieldsString", fieldsString);
        messageMap.put("classComment", toProtoComment(this.comment, ""));
        messageMap.put("classOrEnum", clazz.isEnum() ? "enum" : "message");

        String template = """
                {classComment}{classOrEnum} {className} {
                {fieldsString}
                }
                
                """;

        return StrKit.format(template, messageMap);
    }

    /**
     * Render a comment into one or more proto comment lines.
     *
     * <p>Each line of a multi-line comment is prefixed with the given {@code indent}
     * and {@code "// "}, so multi-line Javadoc no longer produces lines that are
     * missing the {@code //} prefix. Blank lines are dropped. Returns an empty string
     * when {@code comment} is {@code null} or blank.
     *
     * @param comment the raw comment text, possibly spanning multiple lines
     * @param indent  the indentation applied before each {@code //}
     * @return proto comment lines terminated with a trailing newline, or an empty string
     * @since 25.7
     */
    static String toProtoComment(String comment, String indent) {
        if (comment == null || comment.isBlank()) {
            return "";
        }

        return comment.lines()
                .map(String::strip)
                .filter(line -> !line.isEmpty())
                .map(line -> "%s// %s".formatted(indent, line))
                .collect(Collectors.joining("\n", "", "\n"));
    }
}
