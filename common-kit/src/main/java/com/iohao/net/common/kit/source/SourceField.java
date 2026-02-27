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

import java.util.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Field metadata extracted from Java source, including Javadoc comment
 * and enum constant arguments.
 *
 * @author 渔民小镇
 * @date 2025-02-27
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SourceField {
    final String name;
    final String comment;
    final List<Object> enumConstantArguments;

    public SourceField(String name, String comment, List<Object> enumConstantArguments) {
        this.name = name;
        this.comment = comment;
        this.enumConstantArguments = enumConstantArguments;
    }
}
