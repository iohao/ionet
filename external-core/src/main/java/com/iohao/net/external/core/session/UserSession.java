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


import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.protocol.CommunicationMessage;
import com.iohao.net.common.kit.attr.AttrOptionDynamic;
import com.iohao.net.external.core.config.ExternalJoinEnum;
import com.iohao.net.external.core.message.CommunicationMessageKit;

/**
 * UserSession
 *
 * @author 渔民小镇
 * @date 2023-02-18
 * @see UserSessionOption UserSessionOption for dynamic properties
 */
public interface UserSession extends AttrOptionDynamic {
    /**
     * active
     *
     * @return true active
     */
    boolean isActive();

    void setExternalJoin(ExternalJoinEnum externalJoin);

    /**
     * Sets the ID of the current user (player).
     *
     * @param userId userId
     */
    void setUserId(long userId);

    /**
     * Gets the ID of the current user (player).
     *
     * @return The ID of the current user (player)
     */
    long getUserId();

    /**
     * Checks if the identity has been verified.
     *
     * @return true: if logged in
     */
    boolean isVerifyIdentity();

    /**
     * UserSessionState of current player
     *
     * @return State
     */
    UserSessionState getState();

    /**
     * Gets the UserChannelId of the current user (player).
     *
     * @return UserChannelId
     */
    long getUserChannelId();

    /**
     * Adds user info to request.
     * Developers can extend data via HeadMetadata.setAttachmentData(byte[]), which will be forwarded to the logic server.
     *
     * @param message message
     */
    void employ(CommunicationMessage message);

    /**
     * writeAndFlush
     *
     * @param message message
     * @return ChannelFuture
     */
    <T> T writeAndFlush(Object message);

    /**
     * Get player IP
     *
     * @return player IP
     */
    String getIp();

    void setAttachment(byte[] attachment);

    void setBindingLogicServerIds(int[] bindingLogicServerIds);

    int[] getBindingLogicServerIds();

    default CommunicationMessage ofMessage(CmdInfo cmdInfo) {
        var message = CommunicationMessageKit.createCommunicationMessage();
        message.setCmdInfo(cmdInfo);

        this.employ(message);

        return message;
    }
}
