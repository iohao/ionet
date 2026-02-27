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

import com.iohao.net.common.kit.exception.*;
import com.iohao.net.framework.core.runner.*;
import java.util.*;

/**
 * Default (free-tier) implementation of {@link EventBusSetting} that throws
 * {@link EnterpriseSupportException} for most operations, indicating enterprise support is required.
 */
final class FreeEventBusSetting implements EventBusSetting {
    @Override
    public void defaultSetting(EventBus eventBus) {
        throw new EnterpriseSupportException();
    }

    @Override
    public EventBus ofEventBus(int id) {
        throw new EnterpriseSupportException();
    }

    @Override
    public EventBusRegion getEventBusRegion() {
        return new FreeEventBusRegion();
    }

    @Override
    public Runner getCheckedEventBusRunner() {
        return null;
    }
}

/**
 * Default (free-tier) implementation of {@link EventBusRegion} that throws
 * {@link EnterpriseSupportException} for most operations and returns empty collections otherwise.
 */
class FreeEventBusRegion implements EventBusRegion {

    @Override
    public EventBus getEventBus(int serverId) {
        throw new EnterpriseSupportException();
    }

    @Override
    public void addLocal(EventBus eventBus) {
        throw new EnterpriseSupportException();
    }

    @Override
    public List<Subscriber> listLocalSubscriber(EventBusMessage eventBusMessage) {
        return List.of();
    }

    @Override
    public void loadRemoteEventTopic(EventServerMessage eventServerMessage) {
        throw new EnterpriseSupportException();
    }

    @Override
    public void unloadRemoteTopic(EventServerMessage eventServerMessage) {
        throw new EnterpriseSupportException();
    }

    @Override
    public Set<EventServerMessage> listRemoteEventServerMessage(EventBusMessage eventBusMessage) {
        return Set.of();
    }
}