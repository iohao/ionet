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

    /**
     * Whether to enable the Aeron fragment assembler for large messages.
     * <p>
     * Aeron fragments any message whose encoded length exceeds the MTU (Aeron's default MTU is
     * 1408 bytes for both network and IPC), and the receiver can only reassemble such multi-fragment
     * messages when this is enabled. Turn it on whenever a single message can exceed the MTU, i.e.
     * roughly when the payload can exceed ~1.4 KB; otherwise oversized messages will not be
     * reassembled correctly. It is off by default because typical messages fit within one MTU.
     */
    public boolean enableFragmentAssembler;
    /** Maximum number of fragments to assemble per poll operation. */
    public int fragmentLimit = 100;
    /**
     * Buffer size in bytes for the Aeron publisher.
     * <p>
     * This is the encoding-side upper bound and defaults to 64 KiB. The effective message cap is
     * the minimum of this buffer size, the relevant SBE field limit, and the Aeron publication's
     * maximum message length. Different projects can tune this value without regenerating the SBE
     * codecs up to the payload field's 8 MiB minus 4 KiB schema ceiling.
     * <p>
     * The buffer must hold the whole encoded message, not just the payload: the SBE message header
     * plus fixed fields and each variable-data length prefix add roughly 100+ bytes of overhead
     * (more if an attachment is present). So the maximum usable payload is this buffer size minus
     * that overhead - size this value with headroom above your largest payload, never exactly equal.
     * <p>
     * Upper bound: Aeron's maximum message length is {@code min(termBufferLength / 8, 16 MiB)}, which
     * is 8 MiB with Aeron's default 64 MiB IPC term buffer. Do not exceed 8 MiB unless the Aeron IPC
     * term buffer length is increased accordingly. Also note that any value above the MTU (~1.4 KB)
     * means large messages get fragmented, so {@link #enableFragmentAssembler} must be enabled.
     */
    public int publisherBufferSize = 1024 * 64;
    /** Maximum queued messages per Aeron publication; values less than or equal to 0 use an unbounded queue. */
    public int publisherQueueCapacity = 65_536;
    /** Maximum messages drained per publication per publisher loop; values less than or equal to 0 drain all. */
    public int publisherDrainLimit = 1024;
    /** Maximum retry attempts for retryable Aeron offer failures; 0 disables retry and values less than 0 retry indefinitely. */
    public int publisherOfferRetryLimit = 2;
    /** Maximum spin iterations for publisher idle backoff. */
    public int publisherIdleMaxSpins = 100;
    /** Maximum yield iterations for publisher idle backoff. */
    public int publisherIdleMaxYields = 100;
    /** Minimum park duration in nanoseconds for publisher idle backoff. */
    public long publisherIdleMinParkNanos = 1_000;
    /** Maximum park duration in nanoseconds for publisher idle backoff. */
    public long publisherIdleMaxParkNanos = 100_000;

    /** Whether development mode is enabled, providing extra diagnostics. */
    public boolean devMode;
    /** Broadcast trace hook used to record diagnostic information when broadcast messages are sent. */
    public BroadcastTrace broadcastTrace = new DefaultBroadcastTrace();

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
