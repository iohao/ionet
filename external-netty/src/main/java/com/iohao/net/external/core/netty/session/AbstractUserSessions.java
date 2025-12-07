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

import com.iohao.net.common.kit.attr.AttrOptions;
import com.iohao.net.external.core.config.ExternalJoinEnum;
import com.iohao.net.external.core.hook.UserHook;
import com.iohao.net.external.core.session.UserSession;
import com.iohao.net.external.core.session.UserSessions;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;

/**
 * AbstractUserSessions
 *
 * @author 渔民小镇
 * @date 2023-05-28
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
abstract class AbstractUserSessions<ChannelHandlerContext, Session extends UserSession>
        implements UserSessions<ChannelHandlerContext, Session> {

    @Getter
    final AttrOptions options = new AttrOptions();
    /** key : userId */
    final Long2ObjectConcurrentHashMap<Session> userIdMap = new Long2ObjectConcurrentHashMap<>();
    /** key : userChannelId */
    final Long2ObjectConcurrentHashMap<Session> userChannelIdMap = new Long2ObjectConcurrentHashMap<>();

    ExternalJoinEnum joinEnum;
    UserHook userHook;

    @Override
    public void setJoinEnum(ExternalJoinEnum joinEnum) {
        this.joinEnum = joinEnum;
    }

    @Override
    public void setUserHook(UserHook userHook) {
        this.userHook = userHook;
    }

    @Override
    public UserHook getUserHook() {
        return this.userHook;
    }

    @Override
    public boolean existUserSession(long userId) {
        return this.userIdMap.containsKey(userId);
    }

    @Override
    public Session getUserSession(long userId) {
        return this.userIdMap.get(userId);
    }

    @Override
    public Session getUserSessionByUserChannelId(long userChannelId) {
        return this.userChannelIdMap.get(userChannelId);
    }

    @Override
    public void removeUserSession(long userId, Object msg) {
        this.ifPresent(userId, userSession -> {
            ChannelFuture channelFuture = userSession.writeAndFlush(msg);
            channelFuture.addListener((ChannelFutureListener) future -> {
                // 回调 UserSessions 中移除对应的用户
                this.removeUserSession(userSession);
            });
        });
    }

    @Override
    public List<Session> listUserSession() {
        return this.userChannelIdMap.values();
    }

    /**
     * Online notification.
     *
     * @param userSession userSession
     */
    void userHookInto(UserSession userSession) {
        if (Objects.isNull(this.userHook)) {
            return;
        }

        this.userHook.into(userSession);
    }

    /**
     * Offline notification.
     *
     * @param userSession userSession
     */
    void userHookQuit(UserSession userSession) {
        if (Objects.isNull(userHook)) {
            return;
        }

        this.userHook.quit(userSession);
    }

    void settingDefault(UserSession userSession) {
        userSession.setExternalJoin(this.joinEnum);
    }
}
