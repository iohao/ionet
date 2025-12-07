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
/**
 * External Server - <a href="https://iohao.github.io/ionet/docs/manual/cache">Cache</a>
 * <p>
 * When caching business data, the general practice is to use professional caching libraries such as Caffeine, cache2k, ehcache, JetCache, etc., to cache business data in the logic server.
 * <p>
 * However, the External Server Cache can store some hot business data in the external server's memory. When a player accesses the relevant route, the data will be fetched directly from the external server's memory. This avoids repeated requests to the logic server, achieving a super performance boost.
 * <p>
 * When we combine the external server cache with professional caching libraries, we can achieve even greater performance results. Because we can cache hot data in the external server, subsequent players accessing that hot data will not need to retrieve it from the logic server; they can get the data directly from the external server layer.
 * <p>
 * The usage of the external server cache is similar to route access control. If you are already familiar with that part, you can get started in a few minutes.
 * <p>
 * The external server cache offers a huge performance improvement, mainly in several aspects:
 * 1. When players access cached data, the response is faster because the request chain is shorter.
 * 2. Data is fetched directly from the external server, without the need to pass the request to the logic server, and without the need for serialization of business data.
 * 3. Avoids passing requests to the logic server, saving system resources.
 * <p>
 * Features:
 * 1. Zero learning curve.
 * 2. Can quickly respond to player requests.
 * 3. Simplifies cache usage; even if these business data are not cached in the logic server, configuring the relevant route cache in the external server can achieve the caching effect.
 * 4. Reduces request transmission. The external server cache also reduces request transmission, allowing business data to be processed at the external server without going through the logic server.
 * 5. Avoids serialization. Since the business data corresponding to the route is cached as a byte[] type in the external server, the business data obtained from the cache will no longer require serialization (encoding). In simple terms, there's no need to convert the business object into a byte[] type.
 * 6. Supports conditional caching; the same action can support different request parameters.
 * 7. Supports route range cache configuration.
 *
 * @author 渔民小镇
 * @date 2023-07-02
 */
package com.iohao.net.external.core.hook.cache;