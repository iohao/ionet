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
package com.iohao.net.server;

import com.iohao.net.common.*;
import com.iohao.net.framework.communication.*;
import com.iohao.net.framework.core.exception.*;
import com.iohao.net.framework.protocol.*;
import com.iohao.net.server.balanced.*;
import com.iohao.net.server.connection.*;
import io.aeron.*;
import io.aeron.logbuffer.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import org.junit.jupiter.api.*;

import static com.iohao.net.server.CommunicationAggregationErrorConst.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests failure-path behavior in {@link DefaultCommunicationAggregation}.
 *
 * @author 渔民小镇
 * @date 2026-05-01
 * @since 25.4
 */
class DefaultCommunicationAggregationTest {
    RecordingFutureManager futureManager;
    RecordingPublisher publisher;
    DefaultCommunicationAggregation aggregation;

    @BeforeEach
    void setUp() {
        this.futureManager = new RecordingFutureManager();
        this.publisher = new RecordingPublisher();
        this.aggregation = new DefaultCommunicationAggregation();
        this.aggregation.futureManager = this.futureManager;
        this.aggregation.publisher = this.publisher;
        this.aggregation.connectionManager = new RecordingConnectionManager();
        this.aggregation.externalServerLoadBalanced = new TestExternalServerLoadBalanced(null);
    }

    @AfterEach
    void tearDown() {
        Thread.interrupted();
    }

    @Test
    void callExternalFutureWithoutExternalServerReturnsCompletedFallbackWithoutRegisteringFuture() {
        var message = new ExternalRequestMessage();

        var future = this.aggregation.callExternalFuture(message);

        assertTrue(future.isDone());
        assertSame(externalResponseDataNotExist, future.join());
        assertEquals(0, message.getFutureId());
        assertEquals(0, this.futureManager.nextFutureIdCalls.get());
        assertTrue(this.futureManager.futures.isEmpty());
        assertTrue(this.publisher.publishedMessages.isEmpty());
    }

    @Test
    void callExternalFutureWithExternalServerRegistersFutureAndPublishes() {
        var server = newServer(1001, 2001, "external-publication", ServerTypeEnum.EXTERNAL);
        this.aggregation.externalServerLoadBalanced = new TestExternalServerLoadBalanced(server);
        var message = new ExternalRequestMessage();
        message.setExternalServerId(server.id());

        var future = this.aggregation.callExternalFuture(message);

        assertFalse(future.isDone());
        assertEquals(1, message.getFutureId());
        assertTrue(this.futureManager.contains(message.getFutureId()));

        var publishedMessage = assertSinglePublishedMessage();
        assertEquals(server.pubName(), publishedMessage.publicationName());
        assertSame(message, publishedMessage.message());
    }

    @Test
    void callExternalRestoresInterruptFlagAndRemovesPendingFuture() {
        var server = newServer(1001, 2001, "external-publication", ServerTypeEnum.EXTERNAL);
        this.aggregation.externalServerLoadBalanced = new TestExternalServerLoadBalanced(server);
        var message = new ExternalRequestMessage();
        message.setExternalServerId(server.id());

        Thread.currentThread().interrupt();
        var response = this.aggregation.callExternal(message);

        assertSame(externalResponseDataNotExist, response);
        assertTrue(Thread.currentThread().isInterrupted());
        assertFalse(this.futureManager.contains(message.getFutureId()));
    }

    @Test
    void callExternalRemovesPendingFutureWhenFutureFails() {
        var server = newServer(1001, 2001, "external-publication", ServerTypeEnum.EXTERNAL);
        this.aggregation.externalServerLoadBalanced = new TestExternalServerLoadBalanced(server);
        this.futureManager.completeExceptionallyOnCreate = true;
        var message = new ExternalRequestMessage();
        message.setExternalServerId(server.id());

        var response = this.aggregation.callExternal(message);

        assertSame(externalResponseDataNotExist, response);
        assertFalse(this.futureManager.contains(message.getFutureId()));
    }

    @Test
    void callRestoresInterruptFlagAndRemovesPendingFuture() {
        var server = newServer(1002, 2002, "logic-publication", ServerTypeEnum.LOGIC);
        this.aggregation.findServer = new TestFindServer(server);
        var message = new RequestMessage();

        Thread.currentThread().interrupt();
        var response = this.aggregation.call(message);

        assertTrue(Thread.currentThread().isInterrupted());
        assertEquals(ActionErrorEnum.internalCommunicationError.getCode(), response.getErrorCode());
        assertFalse(this.futureManager.contains(message.getFutureId()));
    }

    private PublishedMessage assertSinglePublishedMessage() {
        assertEquals(1, this.publisher.publishedMessages.size());
        return this.publisher.publishedMessages.getFirst();
    }

    private static Server newServer(int serverId, int netId, String publicationName, ServerTypeEnum serverType) {
        return Server.recordBuilder()
                .setId(serverId)
                .setName("server")
                .setTag("server")
                .setServerType(serverType)
                .setNetId(netId)
                .setIp("127.0.0.1")
                .setPubName(publicationName)
                .setCmdMerges(new int[0])
                .setPayloadMap(new HashMap<>())
                .build();
    }

    private static final class RecordingFutureManager implements FutureManager {
        final Map<Long, CompletableFuture<?>> futures = new ConcurrentHashMap<>();
        final AtomicLong idGenerator = new AtomicLong(1);
        final AtomicInteger nextFutureIdCalls = new AtomicInteger();
        boolean completeExceptionallyOnCreate;

        @Override
        public long nextFutureId() {
            this.nextFutureIdCalls.incrementAndGet();
            return this.idGenerator.getAndIncrement();
        }

        @Override
        public <T> CompletableFuture<T> ofFuture(long futureId) {
            var future = new CompletableFuture<T>();
            this.futures.put(futureId, future);
            if (this.completeExceptionallyOnCreate) {
                future.completeExceptionally(new TimeoutException());
            }
            return future;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> CompletableFuture<T> remove(long futureId) {
            return (CompletableFuture<T>) this.futures.remove(futureId);
        }

        boolean contains(long futureId) {
            return this.futures.containsKey(futureId);
        }
    }

    private static final class RecordingPublisher implements Publisher {
        final List<PublishedMessage> publishedMessages = new ArrayList<>();

        @Override
        public void addPublication(String name, Publication publication) {
        }

        @Override
        public void publishMessage(String name, Object message) {
            this.publishedMessages.add(new PublishedMessage(name, message));
        }

        @Override
        public void startup() {
        }

        @Override
        public void shutdown() {
        }
    }

    private record PublishedMessage(String publicationName, Object message) {
    }

    private static final class RecordingConnectionManager implements ConnectionManager {
        @Override
        public void awaitConnect() {
        }

        @Override
        public void addConnection(ConnectionItem connection) {
        }

        @Override
        public void publishMessage(int serverId, Object message) {
        }

        @Override
        public void publishMessageByNetId(int netId, Object message) {
        }

        @Override
        public void publishMessageToCenter(Object message) {
        }

        @Override
        public boolean containsNetId(int netId) {
            return false;
        }

        @Override
        public Publication getPublicationByNetId(int netId) {
            return null;
        }

        @Override
        public void publishMessage(String pubName, Object message) {
        }

        @Override
        public int poll(FragmentHandler fragmentHandler) {
            return 0;
        }
    }

    private record TestFindServer(Server server) implements FindServer {
        @Override
        public void setNetServerSetting(NetServerSetting setting) {
        }

        @Override
        public Server getServer(Request message) {
            return this.server;
        }
    }

    private record TestExternalServerLoadBalanced(Server server) implements ExternalServerLoadBalanced {
        @Override
        public void register(Server message) {
        }

        @Override
        public void unregister(Server message) {
        }

        @Override
        public Server getServer(int externalServerId) {
            return this.server;
        }

        @Override
        public List<Server> listServer() {
            return this.server == null ? List.of() : List.of(this.server);
        }
    }
}
