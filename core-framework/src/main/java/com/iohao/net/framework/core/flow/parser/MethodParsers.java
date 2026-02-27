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
package com.iohao.net.framework.core.flow.parser;

import com.iohao.net.framework.core.*;
import com.iohao.net.framework.protocol.wrapper.*;
import java.util.*;
import java.util.function.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Registry of {@link MethodParser} implementations, mapping parameter types to their parsers.
 * <p>
 * Provides lookup, registration, and default-fallback for action method parameter/return type parsers.
 * Built-in parsers handle primitive types ({@code int}, {@code long}, {@code boolean}, {@code String})
 * and their protocol wrapper counterparts.
 *
 * @author 渔民小镇
 * @date 2022-06-26
 */
@UtilityClass
public final class MethodParsers {
    /**
     * Method parser map
     * <pre>
     * key : Generally use primitive types. For example, int and its wrapper class Integer are categorized as primitive types here.
     * value : The parser corresponding to the primitive type
     * </pre>
     */
    final Map<Class<?>, MethodParser> methodParserMap = new HashMap<>();
    final Map<Class<?>, Supplier<?>> paramSupplierMap = new HashMap<>();

    /** Default parser for action business method parameters */
    @Setter
    MethodParser methodParser = DefaultMethodParser.me();

    static {
        init();
    }

    /**
     * Clear all registered parsers and parameter suppliers.
     */
    public void clear() {
        methodParserMap.clear();
        paramSupplierMap.clear();
    }

    /**
     * Register a parameter supplier for the given class, used to create default instances when data is null.
     *
     * @param paramClass the parameter class
     * @param supplier   the supplier to create default instances
     */
    public void mappingParamSupplier(Class<?> paramClass, Supplier<?> supplier) {
        paramSupplierMap.put(paramClass, supplier);
    }

    /**
     * Get the method parser for the given action method return or parameter metadata.
     *
     * @param actionMethodReturn the return or parameter type metadata
     * @return the matching parser, or the default parser if no specific one is registered
     */
    public MethodParser getMethodParser(ActualParameter actionMethodReturn) {
        Class<?> methodResultClass = actionMethodReturn.getActualTypeArgumentClass();
        return getMethodParser(methodResultClass);
    }

    public Set<Class<?>> keySet() {
        return methodParserMap.keySet();
    }

    /**
     * Get the method parser for the given parameter class.
     *
     * @param paramClazz the parameter class
     * @return the matching parser, or the default parser if no specific one is registered
     */
    public MethodParser getMethodParser(Class<?> paramClazz) {
        return methodParserMap.getOrDefault(paramClazz, methodParser);
    }

    /**
     * Check whether a parser is registered for the given class.
     *
     * @param clazz the class to check
     * @return {@code true} if a parser is registered for this class
     */
    public boolean containsKey(Class<?> clazz) {
        return methodParserMap.containsKey(clazz);
    }

    /**
     * Register a method parser for the given parameter class.
     *
     * @param paramClass       the parameter class
     * @param methodParamParser the parser to handle this class
     */
    public void mapping(Class<?> paramClass, MethodParser methodParamParser) {
        methodParserMap.put(paramClass, methodParamParser);
    }

    private void init() {
        // In action parameters, when encountering int type, use IntValueMethodParser to parse
        mapping(int.class, IntValueMethodParser.me());
        mapping(Integer.class, IntValueMethodParser.me());

        // In action parameters, when encountering long type, use LongValueMethodParser to parse
        mapping(long.class, LongValueMethodParser.me());
        mapping(Long.class, LongValueMethodParser.me());

        // In action parameters, when encountering String type, use StringValueMethodParser to parse
        mapping(String.class, StringValueMethodParser.me());

        // In action parameters, when encountering boolean type, use BoolValueMethodParser to parse
        mapping(boolean.class, BoolValueMethodParser.me());
        mapping(Boolean.class, BoolValueMethodParser.me());

        /*
         * These registrations also enable the containsKey method for short-name lookups
         * during documentation generation. Could use instanceof instead, but this approach
         * is more elegant.
         */
        mapping(IntValue.class, DefaultMethodParser.me(), IntValue::new);
        mapping(IntValueList.class, DefaultMethodParser.me(), IntValueList::new);

        mapping(LongValue.class, DefaultMethodParser.me(), LongValue::new);
        mapping(LongValueList.class, DefaultMethodParser.me(), LongValueList::new);

        mapping(BoolValue.class, DefaultMethodParser.me(), BoolValue::new);
        mapping(BoolValueList.class, DefaultMethodParser.me(), BoolValueList::new);

        mapping(StringValue.class, DefaultMethodParser.me(), StringValue::new);
        mapping(StringValueList.class, DefaultMethodParser.me(), StringValueList::new);
    }

    Object newObject(Class<?> paramClass) {
        if (paramSupplierMap.containsKey(paramClass)) {
            return paramSupplierMap.get(paramClass).get();
        }

        return null;
    }

    private void mapping(Class<?> paramClass, MethodParser methodParamParser, Supplier<?> supplier) {
        mapping(paramClass, methodParamParser);

        /*
         * Native protobuf may produce null for zero-valued fields in jprotobuf.
         * To avoid this, when a business parameter is null and matches a registered type,
         * the Supplier is used to create a default object.
         */
        mappingParamSupplier(paramClass, supplier);
    }
}
