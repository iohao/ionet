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

import io.aeron.logbuffer.*;
import org.agrona.*;

/**
 * Handles a decoded Aeron fragment for a specific SBE template id.
 *
 * @author 渔民小镇
 * @date 2025-08-25
 * @since 25.1
 */
public interface OnFragment {
    /**
     * Processes a fragment payload that has already been routed by template id.
     *
     * @param buffer source buffer
     * @param offset message offset
     * @param actingBlockLength SBE acting block length
     * @param actingVersion SBE acting version
     * @param header Aeron fragment header
     */
    void process(DirectBuffer buffer, int offset, int actingBlockLength, int actingVersion, Header header);

    /**
     * Returns the SBE template id handled by this processor.
     *
     * @return SBE template id
     */
    int getTemplateId();
}
