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

import com.iohao.net.framework.core.CmdInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * BroadcastDocument
 *
 * @author 渔民小镇
 * @date 2024-06-25
 */
@Getter
@FieldDefaults(level = AccessLevel.PUBLIC)
public final class BroadcastDocument {
    /** 路由 */
    final CmdInfo cmdInfo;
    /** 推送方法描述 */
    String methodDescription;
    /** 方法名 */
    String methodName;
    String cmdMethodName;

    /** 业务类型 */
    Class<?> dataClass;
    /** 业务参数 */
    String dataClassName;
    /** 广播业务参数的描述 */
    String dataDescription;

    /** true 表示协议碎片，false 表示开发者自定义的协议 */
    boolean dataTypeIsInternal;
    /** 广播业务参数是否是 List */
    boolean dataIsList;

    String bizDataType;

    /** sdk result get 方法名 */
    String resultMethodTypeName;
    /** sdk result get list 方法名 */
    String resultMethodListTypeName;

    String dataActualTypeName;

    String exampleCode;
    String exampleCodeAction;

    public int getCmdMerge() {
        return this.cmdInfo.cmdMerge();
    }

    public int getCmd() {
        return this.cmdInfo.cmd();
    }

    public int getSubCmd() {
        return this.cmdInfo.subCmd();
    }

    BroadcastDocument(CmdInfo cmdInfo) {
        this.cmdInfo = cmdInfo;
    }

    public static BroadcastDocumentBuilder builder(CmdInfo cmdInfo) {
        return new BroadcastDocumentBuilder(cmdInfo);
    }

    /**
     * create BroadcastDocumentBuilder
     *
     * @param cmdInfo cmdInfo
     * @return BroadcastDocumentBuilder
     * @deprecated see {@link BroadcastDocument#builder}
     */
    @Deprecated
    public static BroadcastDocumentBuilder newBuilder(CmdInfo cmdInfo) {
        return builder(cmdInfo);
    }
}
