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
package com.iohao.net.external.core.hook.cache;

import java.time.*;
import java.util.*;
import lombok.*;
import lombok.experimental.*;

/**
 * External server cache configuration.
 *
 * @param expireTime      Expiration time
 * @param cacheLimit      Cache limit within cmdActionCache
 * @param expireCheckTime Cache expiration check time period
 * @author 渔民小镇
 * @date 2023-07-02
 */
public record CmdCacheOption(Duration expireTime, int cacheLimit, Duration expireCheckTime) {

    public static Builder builder() {
        return new Builder();
    }

    @Setter
    @Accessors(chain = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public final static class Builder {
        /** Expiration time */
        Duration expireTime = Duration.ofHours(1);

        /**
         * Cache limit (maximum number of caches for the same action)
         */
        int cacheLimit = 256;

        /**
         * Cache expiration check time.
         * <pre>
         * Notes:
         * Setting the cache expiration check time is to avoid frequent cache checks, so the cache expiration time will have some deviation.
         * Deviation range = expireTime (+-) expireCheckTime
         * </pre>
         */
        Duration expireCheckTime = Duration.ofMinutes(5);

        public CmdCacheOption build() {

            Objects.requireNonNull(expireTime);

            if (cacheLimit <= 0) {
                cacheLimit = 256;
            }

            return new CmdCacheOption(this.expireTime, this.cacheLimit, this.expireCheckTime);
        }
    }
}