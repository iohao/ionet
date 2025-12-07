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
package com.iohao.net.server.fragment;

import com.iohao.net.framework.core.BarSkeletonManager;
import com.iohao.net.framework.core.codec.DataCodecManager;
import com.iohao.net.framework.communication.eventbus.EventBusMessage;
import com.iohao.net.common.OnFragment;
import com.iohao.net.common.kit.ByteKit;
import com.iohao.net.sbe.EventBusMessageDecoder;
import io.aeron.logbuffer.Header;
import lombok.extern.slf4j.Slf4j;
import org.agrona.DirectBuffer;

/**
 *
 * @author 渔民小镇
 * @date 2025-09-21
 * @since 25.1
 */
@Slf4j
public final class EventBusMessageOnFragment implements OnFragment {
    final EventBusMessageDecoder decoder = new EventBusMessageDecoder();

    @Override
    public void process(DirectBuffer buffer, int offset, int actingBlockLength, int actingVersion, Header header) {
        decoder.wrap(buffer, offset, actingBlockLength, actingVersion);

        int serverId = decoder.serverId();
        long threadIndex = decoder.threadIndex();
        String traceId = decoder.traceId();
        String topic = decoder.topic();

        var dataLength = decoder.dataLength();
        var dataBytes = ByteKit.ofBytes(dataLength);
        decoder.getData(dataBytes, 0, dataLength);

        var eventBusMessage = new EventBusMessage();
        eventBusMessage.serverId = serverId;
        eventBusMessage.threadIndex = threadIndex;
        eventBusMessage.traceId = traceId;
        eventBusMessage.topic = topic;

        var barSkeleton = BarSkeletonManager.getBarSkeleton(serverId);
        var eventBus = barSkeleton.eventBus;

        // gets the dataClass by the topic
        Class<?> topicClass = eventBus.getTopicClass(topic);
        if (topicClass != null) {
            var codec = DataCodecManager.getInternalDataCodec();
            var eventSource = codec.decode(dataBytes, topicClass);
            eventBusMessage.setEventSource(eventSource);

            eventBus.fireMe(eventBusMessage);
        } else {
            log.error("The topicClass is null. {}", topic);
        }

    }

    @Override
    public int getTemplateId() {
        return EventBusMessageDecoder.TEMPLATE_ID;
    }
}