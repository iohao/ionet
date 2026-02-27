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

import com.iohao.net.common.kit.exception.*;
import lombok.extern.slf4j.*;

/**
 * Registry for {@link OnFragment} handlers keyed by SBE template id.
 *
 * @author 渔民小镇
 * @date 2025-09-05
 * @since 25.1
 */
@Slf4j
public final class OnFragmentManager {
    /** Template-id indexed handler table. */
    public static final OnFragment[] onFragments = new OnFragment[36];

    /**
     * Registers or replaces a fragment handler for its template id.
     *
     * @param onFragment fragment handler
     */
    public static void register(OnFragment onFragment) {
        int templateId = onFragment.getTemplateId();
        if (templateId < 0 || templateId >= onFragments.length) {
            var message = "Template ID %s is out of bounds [0, %s].".formatted(templateId, onFragments.length - 1);
            throw new CommonIllegalArgumentException(message);
        }

        if (onFragments[templateId] == null) {
            onFragments[templateId] = onFragment;
        } else {
            // The latest registration wins so later module bootstraps can override a previous handler.
            log.warn("WARN: onFragment - Template ID {} is already registered by {}. The new instance [{}] will replace the existing instance."
                    , templateId
                    , onFragments[templateId].getClass().getSimpleName()
                    , onFragment.getClass().getSimpleName()
            );

            onFragments[templateId] = onFragment;
        }
    }
}
