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

import com.iohao.net.framework.core.ActionMethodParameter;
import com.iohao.net.framework.core.ActualParameter;
import com.iohao.net.framework.core.codec.DataCodec;

/**
 * Action method parsing: parses method parameters, parses method return value
 *
 * @author 渔民小镇
 * @date 2022-06-26
 */
public interface MethodParser {

    Class<?> getActualClazz(ActualParameter parameterReturn);

    /**
     * Parses action method parameters
     *
     * @param data                  data
     * @param actionMethodParameter paramInfo
     * @param codec                 codec
     * @return The parsed data
     */
    Object parseParam(byte[] data, ActionMethodParameter actionMethodParameter, DataCodec codec);

    Object parseDataList(Object data, DataCodec codec);

    Object parseData(Object data);
}