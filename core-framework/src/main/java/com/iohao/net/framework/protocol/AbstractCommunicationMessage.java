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

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.core.exception.ErrorInformation;
import lombok.Getter;
import lombok.Setter;

/**
 * AbstractCommunicationMessage
 *
 * @author 渔民小镇
 * @date 2025-09-24
 * @since 25.1
 */
@Getter
@Setter
public abstract class AbstractCommunicationMessage implements CommunicationMessage {
    @Ignore
    transient Object other;
    @Ignore
    transient Object inetSocketAddress;
    @Ignore
    transient int cacheCondition;
    @Ignore
    transient long userId;
    @Ignore
    transient boolean verifyIdentity;
    @Ignore
    transient int stick;
    @Ignore
    transient int hopCount;
    @Ignore
    transient int[] bindingLogicServerIds;
    @Ignore
    transient byte[] attachment;

    @Ignore
    transient String traceId;

    @Ignore
    transient int externalServerId;
    @Ignore
    transient int logicServerId;
    @Ignore
    transient int sourceServerId;
    @Ignore
    transient int netId;
    @Ignore
    transient long futureId;
    @Ignore
    transient long nanoTime;

    @Override
    public void setOutputError(ErrorInformation outputError) {
        this.setError(outputError);
    }

    @Override
    public void setCmdInfo(CmdInfo cmdInfo) {
        this.setCmdMerge(cmdInfo.cmdMerge());
    }

    @SuppressWarnings("unchecked")
    public <T> T getOther() {
        return (T) other;
    }

    @Override
    public int getCmdCode() {
        return 0;
    }

    @Override
    public void setCmdCode(int cmdCode) {
    }

    @Override
    public int getProtocolSwitch() {
        return 0;
    }

    @Override
    public void setProtocolSwitch(int protocolSwitch) {

    }

    @Override
    public int getMsgId() {
        return 0;
    }

    @Override
    public void setMsgId(int msgId) {

    }

    @Override
    public int getErrorCode() {
        return 0;
    }

    @Override
    public void setErrorCode(int errorCode) {

    }

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
