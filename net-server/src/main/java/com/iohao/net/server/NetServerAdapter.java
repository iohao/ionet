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

import com.iohao.net.common.OnFragmentManager;
import com.iohao.net.sbe.MessageHeaderDecoder;
import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.agrona.DirectBuffer;

/**
 * NetServerAdapter
 *
 * @author 渔民小镇
 * @date 2025-08-24
 * @since 25.1
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
final class NetServerAdapter implements FragmentHandler {
    final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();

    @Override
    public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
        headerDecoder.wrap(buffer, offset);
        final int headerLength = headerDecoder.encodedLength();
        final int actingBlockLength = headerDecoder.blockLength();
        final int actingVersion = headerDecoder.version();
        var messageOffset = offset + headerLength;

        try {
            var on = OnFragmentManager.onFragments[headerDecoder.templateId()];
            on.process(buffer, messageOffset, actingBlockLength, actingVersion, header);
        } catch (Exception e) {
            log.error("templateId: {}", headerDecoder.templateId());
            log.error(e.getMessage(), e);
        }
    }
}
