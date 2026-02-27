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
package com.iohao.net.common;

import io.aeron.*;
import io.aeron.logbuffer.*;

/**
 * Manages Aeron publications and fragment polling for common network connections.
 *
 * @author 渔民小镇
 * @date 2025-10-25
 * @since 25.1
 */
public interface CommonConnectionManager {
    /**
     * Tests whether a publication mapping exists for the given network id.
     *
     * @param netId network id
     * @return true if the mapping exists
     */
    boolean containsNetId(int netId);

    /**
     * Gets the Aeron publication bound to the given network id.
     *
     * @param netId network id
     * @return publication, or null if absent
     */
    Publication getPublicationByNetId(int netId);

    /**
     * Publishes a message through the named publication pipeline.
     *
     * @param pubName publication name
     * @param message message to publish
     */
    void publishMessage(String pubName, Object message);

    /**
     * Polls inbound fragments and dispatches them to the provided fragment handler.
     *
     * @param fragmentHandler fragment callback
     * @return number of fragments processed
     */
    int poll(FragmentHandler fragmentHandler);
}
