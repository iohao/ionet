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
 * Contract for external communication messages exchanged between clients and the external server.
 * <p>
 * Combines {@link CommonResponse} for error handling, {@link UserIdentity} for user binding,
 * and {@link Request} for routing metadata. Adds protocol-level fields such as the command code,
 * protocol switch flags, message ID, cache condition, and the merged command route key.
 * Implementations are typically codec-generated classes used by the Netty handler pipeline.
 *
 * @author 渔民小镇
 * @date 2025-09-24
 * @since 25.1
 */
public interface CommunicationMessage extends CommonResponse, UserIdentity, Request {

    /**
     * Get the command code identifying the protocol-level message type.
     *
     * @return the command code
     */
    int getCmdCode();

    /**
     * Set the command code identifying the protocol-level message type.
     *
     * @param cmdCode the command code
     */
    void setCmdCode(int cmdCode);

    /**
     * Get the protocol switch flags controlling codec behavior.
     *
     * @return the protocol switch bitmask
     */
    int getProtocolSwitch();

    /**
     * Set the protocol switch flags controlling codec behavior.
     *
     * @param protocolSwitch the protocol switch bitmask
     */
    void setProtocolSwitch(int protocolSwitch);

    /**
     * Get the merged command route key (cmd + subCmd packed into a single int).
     *
     * @return the merged command key
     */
    int getCmdMerge();

    /**
     * Set the merged command route key.
     *
     * @param cmdMerge the merged command key
     */
    void setCmdMerge(int cmdMerge);

    /**
     * Get the serialized payload data.
     *
     * @return the payload byte array
     */
    byte[] getData();

    /**
     * Set the serialized payload data.
     *
     * @param data the payload byte array
     */
    void setData(byte[] data);

    /**
     * Get the client-assigned message ID for request-response correlation.
     *
     * @return the message ID
     */
    int getMsgId();

    /**
     * Set the client-assigned message ID.
     *
     * @param msgId the message ID
     */
    void setMsgId(int msgId);

    /**
     * Get the cache condition flag indicating whether this message is cacheable.
     *
     * @return the cache condition value
     */
    int getCacheCondition();

    /**
     * Set the cache condition flag.
     *
     * @param cacheCondition the cache condition value
     */
    void setCacheCondition(int cacheCondition);

    /**
     * Derive the {@link CmdInfo} route descriptor from the merged command key.
     *
     * @return the command info
     */
    default CmdInfo getCmdInfo() {
        return CmdInfo.of(this.getCmdMerge());
    }
}
