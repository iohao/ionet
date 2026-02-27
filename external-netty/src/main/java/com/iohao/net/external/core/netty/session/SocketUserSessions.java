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


import com.iohao.net.common.kit.*;
import com.iohao.net.common.kit.concurrent.executor.*;
import com.iohao.net.external.core.session.*;
import io.netty.channel.*;
import io.netty.channel.group.*;
import io.netty.util.*;
import io.netty.util.concurrent.*;
import java.util.*;
import lombok.*;
import lombok.experimental.*;
import org.agrona.concurrent.*;

/**
 * Netty session manager for TCP and WebSocket external transports.
 *
 * @author 渔民小镇
 * @date 2023-02-18
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SocketUserSessions extends AbstractUserSessions<ChannelHandlerContext, SocketUserSession> {
    static final AttributeKey<SocketUserSession> userSessionKey = AttributeKey.valueOf("userSession");
    @Setter
    static SnowflakeIdGenerator idGenerator;

    final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public SocketUserSessions() {
        if (idGenerator == null) {
            idGenerator = new SnowflakeIdGenerator(RandomKit.random(1023));
        }
    }

    /**
     * Add a newly connected channel as a user session and assign a generated channel-scoped id.
     *
     * @param channelHandlerContext netty channel context
     * @return created user session
     */
    @Override
    public SocketUserSession add(ChannelHandlerContext channelHandlerContext) {

        long userChannelId = idGenerator.nextId();
        Channel channel = channelHandlerContext.channel();
        var userSession = new SocketUserSession(channel, userChannelId);

        channel.attr(SocketUserSessions.userSessionKey).set(userSession);

        this.userChannelIdMap.put(userChannelId, userSession);
        this.channelGroup.add(channel);

        this.settingDefault(userSession);

        return userSession;
    }

    @Override
    public SocketUserSession getUserSession(ChannelHandlerContext channelHandlerContext) {
        Channel channel = channelHandlerContext.channel();
        return channel.attr(userSessionKey).get();
    }

    @Override
    public boolean settingUserId(long userChannelId, long userId) {

        SocketUserSession userSession = this.getUserSessionByUserChannelId(userChannelId);
        if (userSession == null) {
            return false;
        }

        if (!userSession.isActive()) {
            removeUserSession(userSession);
            return false;
        }

        userSession.setUserId(userId);

        this.userIdMap.put(userId, userSession);

        // Fire online hook only after the session is fully identity-verified.
        if (userSession.isVerifyIdentity()) {
            this.userHookInto(userSession);
        }

        return true;
    }

    @Override
    public void removeUserSession(SocketUserSession userSession) {
        if (userSession == null) {
            return;
        }

        var userId = userSession.getUserId();
        ExecutorRegionKit.getUserVirtualThreadExecutor(userId)
                .executeTry(() -> internalRemoveUserSession(userSession));
    }

    private void internalRemoveUserSession(SocketUserSession userSession) {
        if (userSession.getState() == UserSessionState.DEAD) {
            removeUserSessionMap(userSession);
            return;
        }

        if (userSession.getState() == UserSessionState.ACTIVE && userSession.isVerifyIdentity()) {
            userSession.setState(UserSessionState.DEAD);
            this.userHookQuit(userSession);
        }

        removeUserSessionMap(userSession);

        userSession.getChannel().close();
    }

    private void removeUserSessionMap(SocketUserSession userSession) {
        long userId = userSession.getUserId();
        this.userIdMap.remove(userId, userSession);

        var userChannelId = userSession.getUserChannelId();
        this.userChannelIdMap.remove(userChannelId);

        Channel channel = userSession.getChannel();
        if (Objects.nonNull(channel)) {
            this.channelGroup.remove(channel);
        }
    }

    @Override
    public int countOnline() {
        return this.channelGroup.size();
    }

    @Override
    public void broadcast(Object msg) {
        this.channelGroup.writeAndFlush(msg);
    }
}
