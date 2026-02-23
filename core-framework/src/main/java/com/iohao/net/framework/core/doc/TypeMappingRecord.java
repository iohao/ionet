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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * Record describing how a Java/protobuf type maps to client-side type names,
 * SDK method names, and result accessor names used during code generation.
 *
 * @author 渔民小镇
 * @date 2024-06-26
 */
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class TypeMappingRecord {
    @Getter
    String paramTypeName;
    /** List parameter type name. */
    String listParamTypeName;
    /** SDK factory method name. */
    String ofMethodTypeName;
    /** SDK factory method name for list parameters. */
    String ofMethodListTypeName;

    /** SDK result getter method name. */
    @Getter
    String resultMethodTypeName;
    /** SDK result list getter method name. */
    @Getter
    String resultMethodListTypeName;

    /** true if this is a built-in (internal) extension type. */
    @Getter
    boolean internalType = true;

    /**
     * Return the parameter type name, choosing the list variant when applicable.
     *
     * @param isList true to return the list type name
     * @return the parameter type name
     */
    public String getParamTypeName(boolean isList) {
        return isList ? listParamTypeName : paramTypeName;
    }

    /**
     * Return the SDK factory method name, choosing the list variant when applicable.
     *
     * @param isList true to return the list method name
     * @return the factory method name
     */
    public String getOfMethodTypeName(boolean isList) {
        return isList ? ofMethodListTypeName : ofMethodTypeName;
    }
}
