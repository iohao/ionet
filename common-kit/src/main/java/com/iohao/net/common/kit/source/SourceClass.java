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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;

/**
 * Class metadata extracted from Java source, including Javadoc comment,
 * line number, methods, fields, and annotations.
 *
 * @author 渔民小镇
 * @date 2025-02-27
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SourceClass {
    final String fullyQualifiedName;
    final String comment;
    final int lineNumber;
    final List<SourceMethod> methods;
    final List<SourceField> fields;
    final List<SourceAnnotation> annotations;

    public SourceClass(String fullyQualifiedName, String comment, int lineNumber,
                       List<SourceMethod> methods, List<SourceField> fields,
                       List<SourceAnnotation> annotations) {
        this.fullyQualifiedName = fullyQualifiedName;
        this.comment = comment;
        this.lineNumber = lineNumber;
        this.methods = methods;
        this.fields = fields;
        this.annotations = annotations;
    }

    public SourceField getFieldByName(String name) {
        for (SourceField field : this.fields) {
            if (Objects.equals(field.getName(), name)) {
                return field;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.fullyQualifiedName;
    }
}
