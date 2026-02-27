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


import com.iohao.net.common.kit.attr.*;
import com.iohao.net.external.core.config.*;
import com.iohao.net.external.core.message.*;
import com.iohao.net.framework.core.*;
import com.iohao.net.framework.protocol.*;

/**
 * External user session abstraction for one connected client.
 *
 * @author 渔民小镇
 * @date 2023-02-18
 * @see UserSessionOption UserSessionOption for dynamic properties
 */
public interface UserSession extends AttrOptionDynamic {
    /**
     * Check whether the session is active.
     *
     * @return true if the session is active
     */
    boolean isActive();

    /**
     * Set the transport type used by this session.
     *
     * @param externalJoin external transport type
     */
    void setExternalJoin(ExternalJoinEnum externalJoin);

    /**
     * Sets the ID of the current user (player).
     *
     * @param userId business user id
     */
    void setUserId(long userId);

    /**
     * Gets the ID of the current user (player).
     *
     * @return current business user id
     */
    long getUserId();

    /**
     * Checks if the identity has been verified.
     *
     * @return true if logged in
     */
    boolean isVerifyIdentity();

    /**
     * Get the current session state.
     *
     * @return session state
     */
    UserSessionState getState();

    /**
     * Gets the UserChannelId of the current user (player).
     *
     * @return user channel id
     */
    long getUserChannelId();

    /**
     * Adds user info to request.
     * Developers can extend data via HeadMetadata.setAttachmentData(byte[]), which will be forwarded to the logic server.
     *
     * @param message outbound message to enrich with session identity/attachment data
     */
    void employ(CommunicationMessage message);

    /**
     * Write a message to the client and flush immediately.
     *
     * @param message message object
     * @return transport-specific future or write result
     */
    <T> T writeAndFlush(Object message);

    /**
     * Get the client IP address.
     *
     * @return player IP
     */
    String getIp();

    /**
     * Set the attachment bytes stored on the session.
     *
     * @param attachment attachment bytes
     */
    void setAttachment(byte[] attachment);

    /**
     * Set logic server bindings associated with this session.
     *
     * @param bindingLogicServerIds bound logic server ids
     */
    void setBindingLogicServerIds(int[] bindingLogicServerIds);

    /**
     * Get logic server bindings associated with this session.
     *
     * @return bound logic server ids
     */
    int[] getBindingLogicServerIds();

    /**
     * Create and populate a communication message for the given route.
     *
     * @param cmdInfo route metadata
     * @return initialized communication message with session identity data
     */
    default CommunicationMessage ofMessage(CmdInfo cmdInfo) {
        var message = CommunicationMessageKit.createCommunicationMessage();
        message.setCmdInfo(cmdInfo);

        this.employ(message);

        return message;
    }
}
