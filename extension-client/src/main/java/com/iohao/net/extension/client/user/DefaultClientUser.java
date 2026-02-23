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
package com.iohao.net.extension.client.user;

import com.iohao.net.common.kit.attr.AttrOptions;
import com.iohao.net.extension.client.InputCommandRegion;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;

/**
 * Default client-side user model.
 * <pre>
 *     Developers can extend business data through dynamic attributes,
 *     such as currency, battle power, HP, and more.
 *
 *     {@link ClientUser} can also be extended via inheritance.
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-07-09
 */
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class DefaultClientUser implements ClientUser {
    final AttrOptions options = new AttrOptions();
    /** Communication channel used for reading and writing. */
    final ClientUserChannel clientUserChannel = new ClientUserChannel(this);
    final ClientUserInputCommands clientUserInputCommands = new ClientUserInputCommands(clientUserChannel);
    List<InputCommandRegion> inputCommandRegions;

    /** True after login succeeds. */
    boolean loginSuccess;

    long userId;
    /** Nickname. */
    String nickname;
    String jwt;

    boolean active = true;

    @Override
    public void callbackInputCommandRegion() {
        if (Objects.isNull(this.inputCommandRegions)) {
            return;
        }

        this.inputCommandRegions.forEach(InputCommandRegion::loginSuccessCallback);
    }
}
