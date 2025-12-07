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

import com.iohao.net.framework.core.ActualParameter;
import com.iohao.net.framework.protocol.wrapper.*;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Wrapper class related to action parameters in the business framework
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

    public void clear() {
        methodParserMap.clear();
        paramSupplierMap.clear();
    }

    public void mappingParamSupplier(Class<?> paramClass, Supplier<?> supplier) {
        paramSupplierMap.put(paramClass, supplier);
    }

    public MethodParser getMethodParser(ActualParameter actionMethodReturn) {
        Class<?> methodResultClass = actionMethodReturn.getActualTypeArgumentClass();
        return getMethodParser(methodResultClass);
    }

    public Set<Class<?>> keySet() {
        return methodParserMap.keySet();
    }

    public MethodParser getMethodParser(Class<?> paramClazz) {
        return methodParserMap.getOrDefault(paramClazz, methodParser);
    }

    public boolean containsKey(Class<?> clazz) {
        return methodParserMap.containsKey(clazz);
    }

    public void mapping(Class<?> paramClass, MethodParser methodParamParser) {
        methodParserMap.put(paramClass, methodParamParser);
    }

    private void init() {
        // 表示在 action 参数中，遇见 int 类型的参数，用 IntValueMethodParser 来解析
        mapping(int.class, IntValueMethodParser.me());
        mapping(Integer.class, IntValueMethodParser.me());

        // 表示在 action 参数中，遇见 long 类型的参数，用 LongValueMethodParser 来解析
        mapping(long.class, LongValueMethodParser.me());
        mapping(Long.class, LongValueMethodParser.me());

        // 表示在 action 参数中，遇见 String 类型的参数，用 StringValueMethodParser 来解析
        mapping(String.class, StringValueMethodParser.me());

        // 表示在 action 参数中，遇见 boolean 类型的参数，用 BoolValueMethodParser 来解析
        mapping(boolean.class, BoolValueMethodParser.me());
        mapping(Boolean.class, BoolValueMethodParser.me());

        /*
         * 这里注册是为了顺便使用 containsKey 方法，因为生成文档的时候要用到短名字
         * 当然也可以使用 instanceof 来做这些，但似乎没有这种方式优雅
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
         * 原生 pb 如果值为 0，在 jprotobuf 中会出现 null 的情况，为了避免这个问题
         * 如果业务参数为 null，当解析到对应的类型时，则使用 Supplier 来创建对象
         */
        mappingParamSupplier(paramClass, supplier);
    }
}
