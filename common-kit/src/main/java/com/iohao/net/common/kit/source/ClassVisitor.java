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

import com.sun.source.doctree.*;
import com.sun.source.doctree.ReturnTree;
import com.sun.source.tree.*;
import com.sun.source.util.*;
import java.util.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Visits class declarations in a compilation unit, extracting metadata
 * (Javadoc, annotations, methods, fields) into {@link SourceClass} instances.
 *
 * @author 渔民小镇
 * @date 2025-02-27
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class ClassVisitor extends TreeScanner<Void, Void> {

    String packageName;
    CompilationUnitTree unit;
    DocTrees docTrees;
    Trees trees;
    Map<String, SourceClass> result;

    ClassVisitor(String packageName, CompilationUnitTree unit,
                 DocTrees docTrees, Trees trees, Map<String, SourceClass> result) {
        this.packageName = packageName;
        this.unit = unit;
        this.docTrees = docTrees;
        this.trees = trees;
        this.result = result;
    }

    @Override
    public Void visitClass(ClassTree classTree, Void unused) {
        var className = classTree.getSimpleName().toString();
        if (className.isEmpty()) {
            return super.visitClass(classTree, unused);
        }

        var fqcn = this.packageName.isEmpty() ? className : this.packageName + "." + className;

        var treePath = TreePath.getPath(this.unit, classTree);
        var comment = extractComment(treePath);
        int lineNumber = extractLineNumber(classTree);

        var annotations = extractAnnotations(classTree.getModifiers());
        var methods = new ArrayList<SourceMethod>();
        var fields = new ArrayList<SourceField>();

        for (Tree member : classTree.getMembers()) {
            switch (member) {
                case MethodTree methodTree -> {
                    var methodName = methodTree.getName().toString();
                    // skip constructors
                    if ("<init>".equals(methodName)) {
                        continue;
                    }

                    var methodPath = TreePath.getPath(this.unit, methodTree);
                    var methodComment = extractComment(methodPath);
                    int methodLine = extractLineNumber(methodTree);
                    var tags = extractDocTags(methodPath);
                    var paramTypes = extractParameterTypes(methodTree);

                    methods.add(new SourceMethod(methodName, methodComment, methodLine, tags, paramTypes));
                }
                case VariableTree variableTree -> {
                    var fieldName = variableTree.getName().toString();
                    var fieldPath = TreePath.getPath(this.unit, variableTree);
                    var fieldComment = extractComment(fieldPath);
                    var enumArgs = extractEnumConstantArguments(variableTree);

                    fields.add(new SourceField(fieldName, fieldComment, enumArgs));
                }
                default -> {}
            }
        }

        this.result.put(fqcn, new SourceClass(fqcn, comment, lineNumber, methods, fields, annotations));

        // recurse into inner classes
        return super.visitClass(classTree, unused);
    }

    private String extractComment(TreePath path) {
        if (path == null) {
            return null;
        }

        var docComment = this.docTrees.getDocCommentTree(path);
        if (docComment == null) {
            return null;
        }

        var firstSentence = docComment.getFirstSentence();
        var body = docComment.getBody();

        var sb = new StringBuilder();
        for (var node : firstSentence) {
            sb.append(docTreeText(node));
        }
        for (var node : body) {
            sb.append(docTreeText(node));
        }

        var text = sb.toString().strip();
        return text.isEmpty() ? null : text;
    }

    private String docTreeText(DocTree node) {
        if (node instanceof TextTree textTree) {
            return textTree.getBody();
        }
        return node.toString();
    }

    private String docTreeListText(List<? extends DocTree> nodes) {
        var sb = new StringBuilder();
        for (var node : nodes) {
            sb.append(docTreeText(node));
        }
        return sb.toString();
    }

    private int extractLineNumber(Tree tree) {
        var sourcePositions = this.trees.getSourcePositions();
        long startPos = sourcePositions.getStartPosition(this.unit, tree);
        if (startPos < 0) {
            return 0;
        }

        var lineMap = this.unit.getLineMap();
        return (int) lineMap.getLineNumber(startPos);
    }

    private List<SourceAnnotation> extractAnnotations(ModifiersTree modifiers) {
        if (modifiers == null) {
            return Collections.emptyList();
        }

        var annotations = modifiers.getAnnotations();
        if (annotations.isEmpty()) {
            return Collections.emptyList();
        }

        var list = new ArrayList<SourceAnnotation>(annotations.size());
        for (var annotation : annotations) {
            var typeName = annotation.getAnnotationType().toString();
            list.add(new SourceAnnotation(typeName));
        }
        return list;
    }

    private List<SourceDocTag> extractDocTags(TreePath path) {
        if (path == null) {
            return Collections.emptyList();
        }

        var docComment = this.docTrees.getDocCommentTree(path);
        if (docComment == null) {
            return Collections.emptyList();
        }

        var blockTags = docComment.getBlockTags();
        if (blockTags.isEmpty()) {
            return Collections.emptyList();
        }

        var tags = new ArrayList<SourceDocTag>(blockTags.size());
        for (var tag : blockTags) {
            if (tag instanceof ParamTree paramTree) {
                var tagValue = paramTree.getName().toString() + " " + docTreeListText(paramTree.getDescription());
                tags.add(new SourceDocTag("param", tagValue.strip()));
            } else if (tag instanceof ReturnTree returnTree) {
                tags.add(new SourceDocTag("return", docTreeListText(returnTree.getDescription()).strip()));
            } else if (tag instanceof BlockTagTree blockTag) {
                tags.add(new SourceDocTag(blockTag.getTagName(), docTreeListText(List.of(blockTag)).strip()));
            }
        }
        return tags;
    }

    private List<String> extractParameterTypes(MethodTree methodTree) {
        var params = methodTree.getParameters();
        if (params.isEmpty()) {
            return Collections.emptyList();
        }

        var types = new ArrayList<String>(params.size());
        for (var param : params) {
            types.add(param.getType().toString());
        }
        return types;
    }

    private List<Object> extractEnumConstantArguments(VariableTree variableTree) {
        var initializer = variableTree.getInitializer();
        if (initializer instanceof NewClassTree newClassTree) {
            var args = newClassTree.getArguments();
            if (args.isEmpty()) {
                return Collections.emptyList();
            }

            var list = new ArrayList<Object>(args.size());
            for (var arg : args) {
                list.add(arg.toString());
            }
            return list;
        }
        return Collections.emptyList();
    }
}
