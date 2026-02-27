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

import com.iohao.net.framework.core.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Action command document, stores source code information.
 *
 * @author 渔民小镇
 * @date 2022-01-22
 */
@FieldDefaults(level = AccessLevel.PUBLIC)
public final class ActionCommandDoc {
    int subCmd;
    ActionCommand actionCommand;

    int classLineNumber = 1;
    String classComment = "";

    /** Line where the code is located */
    int lineNumber = 1;
    String comment = "";

    /** Method return value description */
    String methodReturnComment = "";
    String methodParamComment = "";
}
