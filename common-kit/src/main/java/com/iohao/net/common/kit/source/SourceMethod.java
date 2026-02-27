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

/**
 * Method metadata extracted from Java source, including Javadoc comment,
 * line number, and doc tags ({@code @param}, {@code @return}).
 *
 * @author 渔民小镇
 * @date 2025-02-27
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SourceMethod {
    final String name;
    final String comment;
    final int lineNumber;
    final List<SourceDocTag> tags;
    final List<String> parameterTypes;

    public SourceMethod(String name, String comment, int lineNumber,
                        List<SourceDocTag> tags, List<String> parameterTypes) {
        this.name = name;
        this.comment = comment;
        this.lineNumber = lineNumber;
        this.tags = tags;
        this.parameterTypes = parameterTypes;
    }
}
