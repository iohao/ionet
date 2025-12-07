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
package com.iohao.net.framework.core.flow;

import com.iohao.net.framework.protocol.OnExternalTemplateId;
import com.iohao.net.framework.protocol.SettingUserIdResult;

/**
 *
 * @author 渔民小镇
 * @date 2025-10-09
 * @since 25.1
 */
public interface FLowUserIdSettingCommunication extends FlowExternalCommunication {

    default SettingUserIdResult bindingUserIdAndGetResult(final long bindingUserId) {
        if (bindingUserId <= 0) {
            return SettingUserIdResult.ofError("The bindingUserId must be greater than 0");
        }

        var request = this.getRequest();

        if (request.isVerifyIdentity()) {
            long userId = this.getUserId();
            var message = "The setting of the parameter userId failed because the userId already exists. [bindingUserId:%d userId:%d]"
                    .formatted(bindingUserId, userId);

            return SettingUserIdResult.ofError(message);
        }

        try {
            var message = this.ofExternalRequestMessage(OnExternalTemplateId.settingUserId);
            message.setPayload(bindingUserId);

            var responseMessage = this.callExternal(message);
            if (responseMessage == null) {
                return SettingUserIdResult.ERROR;
            }

            if (responseMessage.hasError()) {
                return SettingUserIdResult.ofError(responseMessage.getErrorMessage());
            }
        } catch (Exception e) {
            return SettingUserIdResult.ofError(e.getMessage());
        }

        request.setUserId(bindingUserId);
        request.setVerifyIdentity(true);
        this.setUserId(bindingUserId);

        return SettingUserIdResult.SUCCESS;
    }

    /**
     * After binding the userId, it means the login is successful
     *
     * @param userId userId
     * @return true:login success
     */
    default boolean bindingUserId(long userId) {
        return this.bindingUserIdAndGetResult(userId).success();
    }
}
