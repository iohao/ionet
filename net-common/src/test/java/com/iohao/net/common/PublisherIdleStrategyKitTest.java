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

import com.iohao.net.framework.*;
import org.agrona.concurrent.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests publisher idle strategy creation.
 *
 * @author 渔民小镇
 * @date 2026-05-01
 * @since 25.4
 */
class PublisherIdleStrategyKitTest {
    int originalPublisherIdleMaxSpins;
    int originalPublisherIdleMaxYields;
    long originalPublisherIdleMinParkNanos;
    long originalPublisherIdleMaxParkNanos;

    @BeforeEach
    void setUp() {
        this.originalPublisherIdleMaxSpins = CoreGlobalConfig.publisherIdleMaxSpins;
        this.originalPublisherIdleMaxYields = CoreGlobalConfig.publisherIdleMaxYields;
        this.originalPublisherIdleMinParkNanos = CoreGlobalConfig.publisherIdleMinParkNanos;
        this.originalPublisherIdleMaxParkNanos = CoreGlobalConfig.publisherIdleMaxParkNanos;
    }

    @AfterEach
    void tearDown() {
        CoreGlobalConfig.publisherIdleMaxSpins = this.originalPublisherIdleMaxSpins;
        CoreGlobalConfig.publisherIdleMaxYields = this.originalPublisherIdleMaxYields;
        CoreGlobalConfig.publisherIdleMinParkNanos = this.originalPublisherIdleMinParkNanos;
        CoreGlobalConfig.publisherIdleMaxParkNanos = this.originalPublisherIdleMaxParkNanos;
    }

    @Test
    void defaultConfigCreatesBackoffIdleStrategy() {
        var idleStrategy = PublisherIdleStrategyKit.newIdleStrategy();

        assertInstanceOf(BackoffIdleStrategy.class, idleStrategy);
        assertDoesNotThrow(() -> idleStrategy.idle());
        assertDoesNotThrow(idleStrategy::reset);
    }

    @Test
    void invalidConfigIsNormalized() {
        CoreGlobalConfig.publisherIdleMaxSpins = -1;
        CoreGlobalConfig.publisherIdleMaxYields = -1;
        CoreGlobalConfig.publisherIdleMinParkNanos = -1;
        CoreGlobalConfig.publisherIdleMaxParkNanos = 0;

        var idleStrategy = PublisherIdleStrategyKit.newIdleStrategy();

        assertInstanceOf(BackoffIdleStrategy.class, idleStrategy);
        assertDoesNotThrow(() -> idleStrategy.idle());
        assertDoesNotThrow(idleStrategy::reset);
    }
}
