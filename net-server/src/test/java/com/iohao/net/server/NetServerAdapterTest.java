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
import com.iohao.net.sbe.*;
import io.aeron.logbuffer.*;
import java.util.concurrent.atomic.*;
import org.agrona.*;
import org.agrona.concurrent.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests net-server Aeron fragment adapter dispatch safety.
 *
 * @author 渔民小镇
 * @date 2026-05-03
 * @since 25.4
 */
class NetServerAdapterTest {
    static final int REGISTERED_TEMPLATE_ID = OnFragmentManager.onFragments.length - 1;
    static final int UNREGISTERED_TEMPLATE_ID = OnFragmentManager.onFragments.length - 2;

    OnFragment registeredFragment;
    OnFragment unregisteredFragment;

    @BeforeEach
    void setUp() {
        this.registeredFragment = OnFragmentManager.onFragments[REGISTERED_TEMPLATE_ID];
        this.unregisteredFragment = OnFragmentManager.onFragments[UNREGISTERED_TEMPLATE_ID];
    }

    @AfterEach
    void tearDown() {
        OnFragmentManager.onFragments[REGISTERED_TEMPLATE_ID] = this.registeredFragment;
        OnFragmentManager.onFragments[UNREGISTERED_TEMPLATE_ID] = this.unregisteredFragment;
    }

    @Test
    void registeredTemplateDispatchesToFragment() {
        var processed = new AtomicInteger();
        OnFragmentManager.onFragments[REGISTERED_TEMPLATE_ID] = new RecordingOnFragment(REGISTERED_TEMPLATE_ID, processed);

        new NetServerAdapter().onFragment(newHeaderBuffer(REGISTERED_TEMPLATE_ID), 0, MessageHeaderEncoder.ENCODED_LENGTH, null);

        assertEquals(1, processed.get());
    }

    @Test
    void unregisteredTemplateDoesNotEscapeAdapter() {
        OnFragmentManager.onFragments[UNREGISTERED_TEMPLATE_ID] = null;

        assertDoesNotThrow(() -> new NetServerAdapter().onFragment(
                newHeaderBuffer(UNREGISTERED_TEMPLATE_ID),
                0,
                MessageHeaderEncoder.ENCODED_LENGTH,
                null
        ));
    }

    @Test
    void outOfRangeTemplateDoesNotEscapeAdapter() {
        assertDoesNotThrow(() -> new NetServerAdapter().onFragment(
                newHeaderBuffer(OnFragmentManager.onFragments.length),
                0,
                MessageHeaderEncoder.ENCODED_LENGTH,
                null
        ));
    }

    private static UnsafeBuffer newHeaderBuffer(int templateId) {
        var buffer = new UnsafeBuffer(new byte[MessageHeaderEncoder.ENCODED_LENGTH]);
        new MessageHeaderEncoder()
                .wrap(buffer, 0)
                .blockLength(0)
                .templateId(templateId)
                .schemaId(MessageHeaderEncoder.SCHEMA_ID)
                .version(MessageHeaderEncoder.SCHEMA_VERSION);
        return buffer;
    }

    private record RecordingOnFragment(int templateId, AtomicInteger processed) implements OnFragment {
        @Override
        public void process(DirectBuffer buffer, int offset, int actingBlockLength, int actingVersion, Header header) {
            this.processed.incrementAndGet();
        }

        @Override
        public int getTemplateId() {
            return this.templateId;
        }
    }
}
