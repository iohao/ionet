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
package com.iohao.net.extension.domain;

import com.lmax.disruptor.dsl.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import lombok.experimental.*;

/**
 * Responsible for managing the Disruptor
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
@UtilityClass
public class DisruptorManager {
    /** Event Topic */
    private final Map<Class<?>, Disruptor<CommonEvent>> topicMap = new ConcurrentHashMap<>();

    CommonEventProducer commonEventProducer;

    /**
     * Get all Disruptor
     *
     * @return List of Disruptor
     */
    public Collection<Disruptor<CommonEvent>> listDisruptor() {
        return topicMap.values();
    }

    public void forEach(Consumer<Disruptor<CommonEvent>> action) {
        listDisruptor().forEach(action);
    }

    /**
     * Get the Disruptor corresponding to the domain message topic
     *
     * @param topic Domain message topic
     * @return Disruptor
     */
    public Disruptor<CommonEvent> getDisruptor(final Class<?> topic) {
        return topicMap.get(topic);
    }

    public void put(Class<?> topic, Disruptor<CommonEvent> disruptor) {
        topicMap.putIfAbsent(topic, disruptor);
    }
}
