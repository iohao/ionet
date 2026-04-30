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
package com.iohao.net.server.connection;

import com.iohao.net.common.*;
import com.iohao.net.framework.protocol.*;
import io.aeron.*;
import java.util.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests missing route handling in {@link DefaultConnectionManager}.
 *
 * @author 渔民小镇
 * @date 2026-04-30
 * @since 25.4
 */
class DefaultConnectionManagerTest {
    RecordingPublisher publisher;
    DefaultConnectionManager connectionManager;

    @BeforeEach
    void setUp() {
        this.publisher = new RecordingPublisher();
        this.connectionManager = new DefaultConnectionManager(this.publisher);
    }

    @Test
    void unknownServerIdDropsMessage() {
        assertDoesNotThrow(() -> this.connectionManager.publishMessage(404, "message"));

        assertTrue(this.publisher.publishedMessages.isEmpty());
    }

    @Test
    void unknownNetIdDropsMessage() {
        assertDoesNotThrow(() -> this.connectionManager.publishMessageByNetId(404, "message"));

        assertTrue(this.publisher.publishedMessages.isEmpty());
    }

    @Test
    void nullPublicationNameDropsMessage() {
        assertDoesNotThrow(() -> this.connectionManager.publishMessage(null, "message"));

        assertTrue(this.publisher.publishedMessages.isEmpty());
    }

    @Test
    void knownServerIdPublishesToConnectionPublicationName() {
        this.connectionManager.addConnection(newConnectionItem(1001, 2001, "custom-publication"));

        var message = new Object();
        this.connectionManager.publishMessage(1001, message);

        var publishedMessage = assertSinglePublishedMessage();
        assertEquals("custom-publication", publishedMessage.publicationName());
        assertSame(message, publishedMessage.message());
    }

    @Test
    void knownNetIdPublishesToConnectionPublicationName() {
        this.connectionManager.addConnection(newConnectionItem(1001, 2001, "custom-publication"));

        var message = new Object();
        this.connectionManager.publishMessageByNetId(2001, message);

        var publishedMessage = assertSinglePublishedMessage();
        assertEquals("custom-publication", publishedMessage.publicationName());
        assertSame(message, publishedMessage.message());
    }

    private PublishedMessage assertSinglePublishedMessage() {
        assertEquals(1, this.publisher.publishedMessages.size());
        return this.publisher.publishedMessages.getFirst();
    }

    private ConnectionItem newConnectionItem(int serverId, int netId, String publicationName) {
        var server = Server.recordBuilder()
                .setId(serverId)
                .setName("logic")
                .setTag("logic")
                .setServerType(ServerTypeEnum.LOGIC)
                .setNetId(netId)
                .setIp("127.0.0.1")
                .setPubName(publicationName)
                .setCmdMerges(new int[0])
                .setPayloadMap(new HashMap<>())
                .build();

        return new ConnectionItem(server, null);
    }

    private static final class RecordingPublisher implements Publisher {
        final Map<String, Publication> publications = new HashMap<>();
        final List<PublishedMessage> publishedMessages = new ArrayList<>();

        @Override
        public void addPublication(String name, Publication publication) {
            this.publications.put(name, publication);
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
}
