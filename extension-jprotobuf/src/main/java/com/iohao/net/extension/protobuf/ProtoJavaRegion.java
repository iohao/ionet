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

import com.iohao.net.common.kit.CollKit;
import com.iohao.net.common.kit.time.TimeFormatKit;
import com.iohao.net.common.kit.time.TimeKit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author 渔民小镇
 * @date 2022-01-25
 */
@FieldDefaults(level = AccessLevel.PUBLIC)
public class ProtoJavaRegion {
    String fileName;
    String filePackage;

    final Map<Class<?>, ProtoJava> protoJavaMap = CollKit.ofConcurrentHashMap();
    final List<ProtoJava> protoJavaList = new CopyOnWriteArrayList<>();
    private final ProtoJavaRegionHead regionHead = new ProtoJavaRegionHead();

    public void addProtoJava(ProtoJava protoJava) {
        this.protoJavaList.add(protoJava);
        this.protoJavaMap.put(protoJava.clazz, protoJava);
    }

    public void addOtherProtoFile(ProtoJava protoJava) {
        String fileName = protoJava.fileName;
        this.regionHead.fileNameSet.add(fileName);
    }

    private static class ProtoJavaRegionHead {
        Set<String> fileNameSet = new HashSet<>();
        String filePackage;

        private String toProtoHead() {

            String templateFileName = """
                    import "%s";
                    """;

            StringBuilder fileNameBuilder = new StringBuilder();

            for (String filePackage : fileNameSet) {
                String filePackageString = String.format(templateFileName, filePackage);
                fileNameBuilder.append(filePackageString);
            }

            String templateHead = """
                    syntax = "proto3";
                    package %s;
                    %s
                    """;

            return String.format(templateHead, this.filePackage, fileNameBuilder);
        }
    }

    public String toProtoFile() {
        this.regionHead.filePackage = this.filePackage;
        String protoHead = this.regionHead.toProtoHead();

        String firstLine = """
                // GeneratedTime: %s
                // ProtocolSize: %s
                // https://github.com/iohao/ionet
                """.formatted(TimeFormatKit.format(TimeKit.nowLocalDate(), "yyyy-MM-dd"), protoJavaList.size());

        StringBuilder builder = new StringBuilder();
        builder.append(firstLine);
        builder.append(System.lineSeparator());
        builder.append(protoHead);

        this.protoJavaList.stream()
                .sorted(Comparator.comparing(o -> o.className))
                .map(ProtoJava::toProtoMessage)
                .forEach(builder::append);

        return builder.toString();
    }
}
