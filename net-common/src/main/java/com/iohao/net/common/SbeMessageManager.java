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

import com.iohao.net.common.kit.*;
import java.util.*;
import lombok.experimental.*;
import lombok.extern.slf4j.*;

/**
 * Registry for message-class to {@link MessageSbe} encoder mappings.
 *
 * @author 渔民小镇
 * @date 2025-08-27
 * @since 25.1
 */
@Slf4j
@UtilityClass
public final class SbeMessageManager {
    final Map<Class<?>, MessageSbe<?>> encoderMap = CollKit.ofConcurrentHashMap();

    /**
     * Gets the registered SBE encoder for the given message class.
     *
     * @param messageClass message class
     * @param <T> message type
     * @return encoder, or null if not registered
     */
    @SuppressWarnings("unchecked")
    public <T> MessageSbe<T> getMessageEncoder(Class<? extends T> messageClass) {
        return (MessageSbe<T>) encoderMap.get(messageClass);
    }

    /**
     * Registers or replaces the SBE encoder for a message class.
     *
     * @param clazz message class
     * @param encoder SBE encoder
     */
    public void register(Class<?> clazz, MessageSbe<?> encoder) {
        if (encoderMap.containsKey(clazz)) {
            log.warn("WARN: SBE - Class {} is already registered by {}.  The new instance [{}] will replace the existing instance."
                    , clazz.getSimpleName()
                    , encoderMap.get(clazz).getClass().getSimpleName()
                    , encoder.getClass().getSimpleName()
            );
        }

        encoderMap.put(clazz, encoder);
    }
}
