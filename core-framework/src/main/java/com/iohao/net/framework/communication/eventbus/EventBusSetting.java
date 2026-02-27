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
package com.iohao.net.framework.communication.eventbus;

import com.iohao.net.framework.annotations.*;
import com.iohao.net.framework.core.runner.*;

/**
 * Configuration interface for the EventBus subsystem, providing factory methods
 * for creating EventBus instances and accessing the EventBus region.
 *
 * @author 渔民小镇
 * @date 2025-10-10
 * @since 25.1
 */
@Enterprise
public interface EventBusSetting {
    /**
     * Apply default settings to the given EventBus instance.
     *
     * @param eventBus the EventBus instance to configure
     */
    void defaultSetting(EventBus eventBus);

    /**
     * Create a new EventBus instance with the given ID.
     *
     * @param id the unique ID for the new EventBus
     * @return a new EventBus instance
     */
    EventBus ofEventBus(int id);

    /**
     * Get the EventBus region that manages all EventBus instances.
     *
     * @return the EventBus region
     */
    EventBusRegion getEventBusRegion();

    /**
     * Get the runner that validates EventBus configuration during startup.
     *
     * @return the checked EventBus runner, or null if not applicable
     */
    Runner getCheckedEventBusRunner();
}
