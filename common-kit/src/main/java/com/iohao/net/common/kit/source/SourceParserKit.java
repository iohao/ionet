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
package com.iohao.net.common.kit.source;

import com.sun.source.tree.*;
import com.sun.source.util.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import javax.tools.*;
import lombok.experimental.*;

/**
 * Utility for parsing Java source files using the JDK Compiler Tree API.
 * Extracts Javadoc comments, line numbers, annotations, and enum constant arguments.
 *
 * @author 渔民小镇
 * @date 2025-02-27
 */
@UtilityClass
public class SourceParserKit {

    /**
     * Parse all {@code .java} files under the given roots and return a map of
     * fully qualified class name to {@link SourceClass}.
     *
     * @param roots source directories or single {@code .java} files
     * @return map keyed by fully qualified class name
     */
    public Map<String, SourceClass> parseSourceTree(File... roots) {
        var javaFiles = collectJavaFiles(roots);
        if (javaFiles.isEmpty()) {
            return Collections.emptyMap();
        }
        return doParse(javaFiles);
    }

    /**
     * Parse source files and return a single class by fully qualified name.
     *
     * @param fqcn  fully qualified class name
     * @param roots source directories or single {@code .java} files
     * @return the parsed class, or an empty {@link SourceClass} if not found
     */
    public SourceClass parseClass(String fqcn, File... roots) {
        var map = parseSourceTree(roots);
        return map.getOrDefault(fqcn, new SourceClass(fqcn, null, 0,
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList()));
    }

    private List<File> collectJavaFiles(File... roots) {
        var javaFiles = new ArrayList<File>();
        for (File root : roots) {
            if (!root.exists()) {
                continue;
            }

            if (root.isFile() && root.getName().endsWith(".java")) {
                javaFiles.add(root);
            } else if (root.isDirectory()) {
                try {
                    Files.walkFileTree(root.toPath(), new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                            if (file.toString().endsWith(".java")) {
                                javaFiles.add(file.toFile());
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } catch (IOException ignored) {
                }
            }
        }
        return javaFiles;
    }

    private Map<String, SourceClass> doParse(List<File> javaFiles) {
        var compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            return Collections.emptyMap();
        }

        var fileManager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8);
        var fileObjects = fileManager.getJavaFileObjectsFromFiles(javaFiles);
        var task = (JavacTask) compiler.getTask(null, fileManager, _ -> {}, null, null, fileObjects);

        Map<String, SourceClass> result = new LinkedHashMap<>();
        try {
            var units = task.parse();
            var docTrees = DocTrees.instance(task);
            var trees = Trees.instance(task);

            for (CompilationUnitTree unit : units) {
                var packageName = unit.getPackageName() != null
                        ? unit.getPackageName().toString() : "";

                unit.accept(new ClassVisitor(packageName, unit, docTrees, trees, result), null);
            }

            fileManager.close();
        } catch (IOException ignored) {
        }

        return result;
    }
}
