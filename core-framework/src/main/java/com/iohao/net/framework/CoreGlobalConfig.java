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
package com.iohao.net.framework;

import com.iohao.net.framework.core.BarSkeletonSetting;
import com.iohao.net.common.kit.RandomKit;
import com.iohao.net.common.kit.exception.ThrowKit;
import lombok.Getter;
import lombok.experimental.UtilityClass;

/**
 * CoreGlobalConfig
 *
 * @author 渔民小镇
 * @date 2025-08-24
 * @since 25.1
 */
@UtilityClass
public final class CoreGlobalConfig {
    public final BarSkeletonSetting setting = new BarSkeletonSetting();
    @Getter
    int netId = RandomKit.randomInt(1000, Integer.MAX_VALUE);
    public String netPubName = String.valueOf(netId);

    public int timeoutMillis = 3000;
    public int cleanFrequency = 10_000;

    public boolean enableFragmentAssembler;
    public int fragmentLimit = 100;
    public int publisherBufferSize = 1024 * 64;

    public boolean devMode;

    public void setNetId(int netId) {
        if (netId < 1000) {
            ThrowKit.ofRuntimeException("netId must > 1000");
        }

        CoreGlobalConfig.netId = netId;
        CoreGlobalConfig.netPubName = String.valueOf(netId);
    }

    public int getFutureTimeoutMillis() {
        return timeoutMillis + 200;
    }

    static {
        Preloading.loading();
    }
}
