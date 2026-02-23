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
import com.iohao.net.framework.core.exception.ErrorInformation;

/**
 * Contract for messages transmitted between servers in the ionet cluster.
 * <p>
 * Extends {@link FutureMessage} and defines the common wire-level fields shared by all
 * inter-server messages: the merged command route key, trace ID for distributed tracing,
 * server routing IDs (external, logic, source), network identifier, serialized payload,
 * error output, and nanosecond-precision timing.
 *
 * @author 渔民小镇
 * @date 2025-09-15
 * @since 25.1
 */
public interface RemoteMessage extends FutureMessage {
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
     * Get the distributed trace ID for this message.
     *
     * @return the trace ID, or {@code null} if not set
     */
    String getTraceId();

    /**
     * Set the distributed trace ID for this message.
     *
     * @param traceId the trace ID
     */
    void setTraceId(String traceId);

    /**
     * Get the ID of the external server that originated or will receive this message.
     *
     * @return the external server ID
     */
    int getExternalServerId();

    /**
     * Set the ID of the external server.
     *
     * @param externalServerId the external server ID
     */
    void setExternalServerId(int externalServerId);

    /**
     * Get the ID of the logic server handling this message.
     *
     * @return the logic server ID
     */
    int getLogicServerId();

    /**
     * Set the ID of the logic server handling this message.
     *
     * @param logicServerId the logic server ID
     */
    void setLogicServerId(int logicServerId);

    /**
     * Get the ID of the server that originally created this message.
     *
     * @return the source server ID
     */
    int getSourceServerId();

    /**
     * Set the ID of the server that originally created this message.
     *
     * @param sourceServerId the source server ID
     */
    void setSourceServerId(int sourceServerId);

    /**
     * Get the network-level identifier.
     *
     * @return the net ID
     */
    int getNetId();

    /**
     * Set the network-level identifier.
     *
     * @param netId the net ID
     */
    void setNetId(int netId);

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
     * Set the command route from a {@link CmdInfo} descriptor.
     *
     * @param cmdInfo the command info containing the merged route key
     */
    void setCmdInfo(CmdInfo cmdInfo);

    /**
     * Derive the {@link CmdInfo} route descriptor from the merged command key.
     *
     * @return the command info
     */
    CmdInfo getCmdInfo();

    /**
     * Set the error information to be included in the response.
     *
     * @param error the error information
     */
    void setOutputError(ErrorInformation error);

    /**
     * Get the nanosecond timestamp recorded when this message was created or received.
     *
     * @return the nano time
     */
    long getNanoTime();

    /**
     * Set the nanosecond timestamp for this message.
     *
     * @param nanoTime the nano time
     */
    void setNanoTime(long nanoTime);
}
