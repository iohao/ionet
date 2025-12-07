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
package com.iohao.net.external.core.session;

import com.iohao.net.common.kit.attr.AttrOptionDynamic;
import com.iohao.net.external.core.config.ExternalJoinEnum;
import com.iohao.net.external.core.hook.UserHook;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * UserSession manager
 *
 * @author 渔民小镇
 * @date 2023-02-18
 */
public interface UserSessions<SessionContext, Session extends UserSession> extends AttrOptionDynamic {

    /**
     * Adds to session management.
     *
     * @param sessionContext sessionContext
     */
    Session add(SessionContext sessionContext);

    /**
     * Gets UserSession.
     *
     * @param sessionContext sessionContext
     * @return SessionContext
     */
    Session getUserSession(SessionContext sessionContext);

    /**
     * Gets UserSession.
     *
     * @param userId userId
     * @return UserSession
     */
    Session getUserSession(long userId);

    /**
     * getUserSession
     *
     * @param userChannelId userChannelId
     * @return userSession
     */
    Session getUserSessionByUserChannelId(long userChannelId);

    /**
     * If a UserSession is present, performs the given action with it, otherwise does nothing.
     *
     * @param userId   userId
     * @param consumer The action to be performed if the UserSession is present
     */
    default void ifPresent(long userId, Consumer<Session> consumer) {
        Session userSession = this.getUserSession(userId);
        if (Objects.nonNull(userSession)) {
            consumer.accept(userSession);
        }
    }

    /**
     * If a UserSession is present, performs the given action with it, otherwise does nothing.
     *
     * @param userIdList userIdList cannot be null
     * @param consumer   The action to be performed if the UserSession is present
     */
    default void ifPresent(Collection<Long> userIdList, Consumer<Session> consumer) {
        for (long userId : userIdList) {
            this.ifPresent(userId, consumer);
        }
    }

    default void ifPresent(long[] userIdList, Consumer<Session> consumer) {
        for (long userId : userIdList) {
            this.ifPresent(userId, consumer);
        }
    }

    /**
     * true if user exists
     *
     * @param userId user id
     * @return true if user exists
     */
    boolean existUserSession(long userId);

    /**
     * Sets the userId for the channel, indicating that the identity has been authenticated (i.e., logged in).
     *
     * @param userChannelId userChannelId
     * @param userId        userId
     * @return true if set successfully
     */
    boolean settingUserId(long userChannelId, long userId);

    /**
     * Removes UserSession.
     *
     * @param userSession userSession
     */
    void removeUserSession(Session userSession);

    /**
     * Removes UserSession by userId, and sends a message before removal.
     *
     * @param userId userId
     * @param msg    msg
     */
    void removeUserSession(long userId, Object msg);

    /**
     * userHook
     *
     * @param userHook userHook
     */
    void setUserHook(UserHook userHook);

    UserHook getUserHook();

    /**
     * Current number of online users
     *
     * @return Current number of online users
     */
    int countOnline();

    /**
     * Broadcasts a message to all users.
     *
     * @param msg message
     */
    void broadcast(Object msg);

    default void forEach(Consumer<Session> consumer) {
        listUserSession().forEach(consumer);
    }

    List<Session> listUserSession();

    /**
     * Connection method
     *
     * @param joinEnum joinEnum
     */
    void setJoinEnum(ExternalJoinEnum joinEnum);
}
