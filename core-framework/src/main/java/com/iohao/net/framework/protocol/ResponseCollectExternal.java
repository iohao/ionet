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
package com.iohao.net.framework.protocol;

import com.iohao.net.common.kit.CollKit;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author 渔民小镇
 * @date 2025-09-16
 * @since 25.1
 */
public interface ResponseCollectExternal {
    List<ExternalResponse> getResponseList();

    default boolean anySuccess() {
        var responseList = this.getResponseList();
        if (Objects.isNull(responseList)) {
            return false;
        }

        for (var response : responseList) {
            if (response.isSuccess()) {
                return true;
            }
        }

        return false;
    }

    default Optional<ExternalResponse> optionalAnySuccess() {
        var responseList = this.getResponseList();
        if (CollKit.isEmpty(responseList)) {
            return Optional.empty();
        }

        return responseList.stream()
                .filter(ExternalResponse::isSuccess)
                .findAny();
    }
}
