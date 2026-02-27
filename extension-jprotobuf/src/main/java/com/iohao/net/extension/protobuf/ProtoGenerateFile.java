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

import com.iohao.net.common.kit.ArrayKit;
import com.iohao.net.common.kit.CollKit;
import com.iohao.net.common.kit.FileKit;
import com.iohao.net.common.kit.StrKit;
import com.iohao.net.common.kit.exception.ThrowKit;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

/**
 * Generates grouped `.proto` files by scanning annotated Java classes.
 *
 * @author 渔民小镇
 * @date 2022-01-25
 */
@Slf4j
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProtoGenerateFile {
    /** proto package path */
    final Set<String> protoPackageSet = new HashSet<>();
    /** Source code root path used by source scanning. */
    String protoSourcePath;
    /** Output directory for generated proto files. */
    String generateFolder;

    public ProtoGenerateFile addProtoPackage(Collection<String> protoPackageList) {
        this.protoPackageSet.addAll(protoPackageList);
        return this;
    }

    public ProtoGenerateFile addProtoPackage(String packageName) {
        protoPackageSet.add(packageName);
        return this;
    }

    private void checked() {
        if (StrKit.isEmpty(generateFolder)) {
            String currentDir = System.getProperty("user.dir");
            this.generateFolder = ArrayKit.join(new String[]{currentDir, "target", "proto"}, File.separator);
        }

        mkdir(this.generateFolder);

        if (Objects.isNull(this.protoSourcePath)) {
            this.protoSourcePath = System.getProperty("user.dir");
        }

        if (protoPackageSet.isEmpty()) {
            ThrowKit.ofRuntimeException("protoPackageSet is empty");
        }
    }

    private void mkdir(String dirPath) {
        try {
            Path path = Paths.get(dirPath);
            Files.createDirectories(path);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void generate() {
        checked();

        ProtoJavaAnalyse.getSourceClassMap(protoSourcePath);

        ProtoJavaAnalyse analyse = new ProtoJavaAnalyse();
        Map<ProtoJavaRegionKey, ProtoJavaRegion> regionMap = CollKit.ofConcurrentHashMap();

        protoPackageSet.forEach(protoPackage -> {
            // analyse protoPackage
            regionMap.putAll(analyse.analyse(protoPackage, protoSourcePath));
        });

        Consumer<ProtoJavaRegion> javaRegionConsumer = javaRegion -> {
            var fileName = javaRegion.fileName;
            var protoJavaList = javaRegion.protoJavaList;

            String protoString = javaRegion.toProtoFile();

            if (ProtoGenerateSetting.enableLog) {
                log.info("""
                        
                        ########## {} ########## protoSize:{}
                        {}
                        """, fileName, protoJavaList.size(), protoString);
            }

            String protoFilePath = String.format("%s%s%s"
                    , this.generateFolder
                    , File.separator
                    , fileName
            );

            FileKit.writeUtf8String(protoString, protoFilePath);
            log.info("\nprotoFilePath: {}", protoFilePath);
        };

        regionMap.values().forEach(javaRegionConsumer);
    }
}
