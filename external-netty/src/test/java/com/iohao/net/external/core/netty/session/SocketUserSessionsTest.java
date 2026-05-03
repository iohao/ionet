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
package com.iohao.net.external.core.netty.session;

import io.netty.channel.*;
import io.netty.channel.embedded.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link SocketUserSessions} identity binding behavior.
 *
 * @author 渔民小镇
 * @date 2026-04-26
 */
public class SocketUserSessionsTest {

    @Test
    public void settingUserIdShouldRejectDifferentVerifiedUserId() {
        var holder = newSession();
        var userSessions = holder.userSessions();
        var userSession = holder.userSession();
        long userChannelId = userSession.getUserChannelId();

        assertTrue(userSessions.settingUserId(userChannelId, 1001));
        assertSame(userSession, userSessions.getUserSession(1001));
        assertEquals(1001, userSession.getUserId());
        assertTrue(userSession.isVerifyIdentity());

        assertTrue(userSessions.settingUserId(userChannelId, 1001));
        assertSame(userSession, userSessions.getUserSession(1001));
        assertEquals(1001, userSession.getUserId());

        assertFalse(userSessions.settingUserId(userChannelId, 2002));
        assertSame(userSession, userSessions.getUserSession(1001));
        assertNull(userSessions.getUserSession(2002));
        assertEquals(1001, userSession.getUserId());

        userSessions.removeUserSession(userSession);
        assertUserSessionRemoved(userSessions, 1001);
        holder.channel().finishAndReleaseAll();
    }

    @Test
    public void settingUserIdShouldAllowOnlyOneConcurrentIdentity() throws Exception {
        var holder = newSession();
        var userSessions = holder.userSessions();
        var userSession = holder.userSession();
        long userChannelId = userSession.getUserChannelId();

        int taskCount = 32;
        var ready = new CountDownLatch(taskCount);
        var start = new CountDownLatch(1);
        var done = new CountDownLatch(taskCount);
        var successCount = new AtomicInteger();
        var executorService = Executors.newFixedThreadPool(taskCount);

        try {
            for (int i = 0; i < taskCount; i++) {
                long userId = 10_000 + i;
                executorService.execute(() -> {
                    ready.countDown();
                    try {
                        start.await();
                        if (userSessions.settingUserId(userChannelId, userId)) {
                            successCount.incrementAndGet();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        done.countDown();
                    }
                });
            }

            assertTrue(ready.await(1, TimeUnit.SECONDS));
            start.countDown();
            assertTrue(done.await(3, TimeUnit.SECONDS));

            assertEquals(1, successCount.get());
            long winnerUserId = userSession.getUserId();
            assertTrue(winnerUserId >= 10_000 && winnerUserId < 10_000 + taskCount);
            assertSame(userSession, userSessions.getUserSession(winnerUserId));

            for (int i = 0; i < taskCount; i++) {
                long userId = 10_000 + i;
                if (userId != winnerUserId) {
                    assertNull(userSessions.getUserSession(userId));
                }
            }

            userSessions.removeUserSession(userSession);
            assertUserSessionRemoved(userSessions, winnerUserId);
        } finally {
            executorService.shutdownNow();
            holder.channel().finishAndReleaseAll();
        }
    }

    private static SessionHolder newSession() {
        var userSessions = new SocketUserSessions();
        var channel = new EmbeddedChannel(new ChannelInboundHandlerAdapter());
        ChannelHandlerContext context = channel.pipeline().firstContext();
        var userSession = userSessions.add(context);

        return new SessionHolder(userSessions, userSession, channel);
    }

    private static void assertUserSessionRemoved(SocketUserSessions userSessions, long userId) {
        long deadline = System.nanoTime() + 1_000_000_000L;
        while (System.nanoTime() < deadline) {
            if (userSessions.getUserSession(userId) == null) {
                return;
            }

            Thread.onSpinWait();
        }

        assertNull(userSessions.getUserSession(userId));
    }

    record SessionHolder(
            SocketUserSessions userSessions,
            SocketUserSession userSession,
            EmbeddedChannel channel
    ) {
    }
}
