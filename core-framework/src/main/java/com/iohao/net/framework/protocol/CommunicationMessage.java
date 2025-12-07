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
package com.iohao.net.framework.protocol;

import com.iohao.net.framework.core.CmdInfo;

/**
 *
 * @author 渔民小镇
 * @date 2025-09-24
 * @since 25.1
 */
public interface CommunicationMessage extends CommonResponse, UserIdentity, Request {

    int getCmdCode();

    void setCmdCode(int cmdCode);

    int getProtocolSwitch();

    void setProtocolSwitch(int protocolSwitch);

    int getCmdMerge();

    void setCmdMerge(int cmdMerge);

    byte[] getData();

    void setData(byte[] data);

    int getMsgId();

    void setMsgId(int msgId);

    int getCacheCondition();

    void setCacheCondition(int cacheCondition);

    default CmdInfo getCmdInfo() {
        return CmdInfo.of(this.getCmdMerge());
    }
}
