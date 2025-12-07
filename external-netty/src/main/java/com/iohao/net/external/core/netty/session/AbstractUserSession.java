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
package com.iohao.net.external.core.netty.session;

import com.iohao.net.framework.protocol.CommunicationMessage;
import com.iohao.net.common.kit.CommonConst;
import com.iohao.net.common.kit.attr.AttrOptions;
import com.iohao.net.external.core.config.ExternalJoinEnum;
import com.iohao.net.external.core.session.UserSession;
import com.iohao.net.external.core.session.UserSessionOption;
import com.iohao.net.external.core.session.UserSessionState;
import io.netty.channel.Channel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * @author 渔民小镇
 * @date 2023-05-28
 */
@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
abstract class AbstractUserSession implements UserSession {
    final AttrOptions options = new AttrOptions();
    Channel channel;
    long userId;
    long userChannelId;
    @Setter
    int externalServerId;
    @Setter
    UserSessionState state = UserSessionState.ACTIVE;
    @Setter
    ExternalJoinEnum externalJoin;
    int[] bindingLogicServerIds;
    @Setter
    byte[] attachment;

    AbstractUserSession() {
        bindingLogicServerIds = CommonConst.emptyInts;
        this.option(UserSessionOption.verifyIdentity, false);
    }

    @Override
    public void employ(CommunicationMessage message) {
        if (this.isVerifyIdentity()) {
            message.setUserId(userId);
            message.setVerifyIdentity(true);
        } else {
            message.setUserId(userChannelId);
        }

        message.setExternalServerId(this.externalServerId);
        message.setAttachment(this.attachment);
        message.setBindingLogicServerIds(this.bindingLogicServerIds);

        if (this.externalJoin != null) {
            message.setStick(externalJoin.getIndex());
        }
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
        this.option(UserSessionOption.verifyIdentity, true);
    }

    @Override
    public void setBindingLogicServerIds(int[] bindingLogicServerIds) {
        this.bindingLogicServerIds = bindingLogicServerIds;
    }

    @Override
    public boolean isVerifyIdentity() {
        return this.optionValue(UserSessionOption.verifyIdentity, false);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof AbstractUserSession that)) {
            return false;
        }

        return userChannelId == that.userChannelId;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.userChannelId);
    }
}
