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

/**
 * Queues and publishes encoded messages to Aeron publications.
 *
 * @author 渔民小镇
 * @date 2025-09-27
 * @since 25.1
 */
public interface Publisher {
    /**
     * Adds a named Aeron publication target.
     *
     * @param name publication name
     * @param publication Aeron publication
     */
    void addPublication(String name, Publication publication);

    /**
     * Enqueues a message for publication.
     *
     * @param name publication name
     * @param message message to publish
     */
    void publishMessage(String name, Object message);

    /** Starts background publishing resources. */
    void startup();

    /** Stops background publishing resources. */
    void shutdown();
}
