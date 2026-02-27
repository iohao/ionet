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

import com.iohao.net.common.kit.*;
import com.iohao.net.common.kit.exception.*;
import com.iohao.net.framework.core.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Global configuration constants for the ionet core framework.
 *
 * @author 渔民小镇
 * @date 2025-08-24
 * @since 25.1
 */
@UtilityClass
public final class CoreGlobalConfig {
    /** Global business framework settings. */
    public final BarSkeletonSetting setting = new BarSkeletonSetting();
    /** Unique network identifier for this node, randomly generated at startup. */
    @Getter
    int netId = RandomKit.randomInt(1000, Integer.MAX_VALUE);
    /** Human-readable publication name derived from {@link #netId}. */
    public String netPubName = String.valueOf(netId);

    /** Default timeout in milliseconds for request-response operations. */
    public int timeoutMillis = 3000;
    /** Frequency in milliseconds for cleaning up expired futures. */
    public int cleanFrequency = 10_000;

    /** Whether to enable Aeron fragment assembler for large messages. */
    public boolean enableFragmentAssembler;
    /** Maximum number of fragments to assemble per poll operation. */
    public int fragmentLimit = 100;
    /** Buffer size in bytes for the Aeron publisher. */
    public int publisherBufferSize = 1024 * 64;

    /** Whether development mode is enabled, providing extra diagnostics. */
    public boolean devMode;

    /**
     * Set the network ID. Must be greater than 1000.
     *
     * @param netId the network ID to set
     */
    public void setNetId(int netId) {
        if (netId < 1000) {
            ThrowKit.ofRuntimeException("netId must > 1000");
        }

        CoreGlobalConfig.netId = netId;
        CoreGlobalConfig.netPubName = String.valueOf(netId);
    }

    /**
     * Get the future timeout in milliseconds, with a 200ms buffer added to the base timeout.
     *
     * @return the timeout value in milliseconds
     */
    public int getFutureTimeoutMillis() {
        return timeoutMillis + 200;
    }

    static {
        Preloading.loading();
    }
}
