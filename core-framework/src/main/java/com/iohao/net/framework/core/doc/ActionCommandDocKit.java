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

import com.iohao.net.common.kit.CollKit;
import com.iohao.net.common.kit.IonetLogName;
import com.iohao.net.common.kit.ClassScanner;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;

/**
 * @author 渔民小镇
 * @date 2022-01-28
 */
@UtilityClass
@Slf4j(topic = IonetLogName.CommonStdout)
public class ActionCommandDocKit {
    @Setter
    Function<URL, String> sourceFilePathFun = resourceUrl -> {
        String path = resourceUrl.getPath();
        boolean isMaven = path.contains("target/classes");

        // #459
        if (!isMaven && path.contains(".jar!")) {
            // jar 包内的路径，目前只处理了 gradle
            int indexOf = path.indexOf(":");
            if (indexOf != -1) {
                path = path.substring(indexOf + 1);
            }

            // 定义正则表达式模式
            String regex = "/build/*/.*?\\.jar!/";
            // 使用正则表达式替换
            return path.replaceAll(regex, "/src/main/java/");
        }

        return isMaven
                // maven
                ? path.replace("target/classes", "src/main/java")
                // gradle
                : path.replace("build/classes", "src/main/java");
    };

    Set<Class<?>> processedSet = CollKit.ofConcurrentSet();
    final Map<String, JavaClassDocInfo> javaClassDocInfoMap = CollKit.ofConcurrentHashMap();

    /**
     * java class doc map
     * <pre>
     *     key : java class name (YourJavaFile.class)
     *     value : {@link JavaClassDocInfo}
     * </pre>
     *
     * @param controllerList classList
     * @return map
     */
    public Map<String, JavaClassDocInfo> getJavaClassDocInfoMap(Set<Class<?>> controllerList) {

        Set<String> sourceTreeSet = new HashSet<>();
        JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();

        for (Class<?> actionClazz : controllerList) {
            try {
                String packagePath = actionClazz.getPackageName();

                if (processedSet.contains(actionClazz)) {
                    continue;
                } else {
                    processedSet.add(actionClazz);
                }

                ClassScanner classScanner = new ClassScanner(packagePath, null);
                List<URL> resources = classScanner.listResource();

                for (URL resource : resources) {
                    String srcPath = sourceFilePathFun.apply(resource);

                    File file = new File(srcPath);
                    if (!exist(file)) {
                        continue;
                    }

                    String path = file.getPath();
                    if (sourceTreeSet.contains(path)) {
                        continue;
                    } else {
                        sourceTreeSet.add(path);
                    }

                    javaProjectBuilder.addSourceTree(file);
                }

                var classes = javaProjectBuilder.getClasses();
                for (JavaClass javaClass : classes) {
                    javaClassDocInfoMap.computeIfAbsent(javaClass.toString(), _ -> new JavaClassDocInfo(javaClass));
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        return javaClassDocInfoMap;
    }

    private boolean exist(File file) {
        return file != null && file.exists();
    }
}