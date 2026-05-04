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

import io.aeron.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link DefaultPublisher} lifecycle behavior.
 *
 * @author 渔民小镇
 * @date 2026-05-03
 * @since 25.4
 */
class DefaultPublisherTest {

    @BeforeAll
    static void beforeAll() {
        PublisherTestKit.registerTestMessageEncoder();
    }

    @Test
    void shutdownWithoutStartupClosesPublications() {
        var publisher = new DefaultPublisher();
        var publication = RecordingPublication.create();
        publisher.addPublication("logic", publication);

        publisher.shutdown();

        assertTrue(publication.isClosed());
        assertEquals(1, publication.closeCount());
    }

    @Test
    void repeatedShutdownClosesPublicationsOnlyOnce() {
        var publisher = new DefaultPublisher();
        var publication = RecordingPublication.create();
        publisher.addPublication("logic", publication);

        publisher.shutdown();
        publisher.shutdown();

        assertTrue(publication.isClosed());
        assertEquals(1, publication.closeCount());
    }

    @Test
    void startupPublishesQueuedMessageToPublication() throws InterruptedException {
        var publisher = new DefaultPublisher();
        var publication = RecordingPublication.create();
        publisher.addPublication("logic", publication);

        try {
            publisher.startup();
            publisher.publishMessage("logic", new PublisherTestKit.TestMessage(1));

            PublisherTestKit.awaitUntil(() -> publication.offerCount() == 1);

            assertEquals(8, publication.lastOfferLength());
        } finally {
            publisher.shutdown();
        }
    }

    @Test
    void startupRetriesBackPressuredOfferUntilSuccess() throws InterruptedException {
        var publisher = new DefaultPublisher();
        var publication = RecordingPublication.create()
                .setOfferResults(Publication.BACK_PRESSURED, 1);
        publisher.addPublication("logic", publication);

        try {
            publisher.startup();
            publisher.publishMessage("logic", new PublisherTestKit.TestMessage(2));

            PublisherTestKit.awaitUntil(() -> publication.offerCount() == 2);

            assertEquals(8, publication.lastOfferLength());
        } finally {
            publisher.shutdown();
        }
    }
}
