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

/**
 * Contract for messages that carry user identity and authentication state.
 * <p>
 * Provides accessors for the user id and a verification flag, along with
 * convenience methods for copying identity between messages and binding
 * an authenticated user.
 *
 * @author 渔民小镇
 * @date 2025-09-17
 * @since 25.1
 */
public interface UserIdentity {
    /**
     * Get the user id.
     *
     * @return the user id
     */
    long getUserId();

    /**
     * Set the user id.
     *
     * @param userId the user id
     */
    void setUserId(long userId);

    /**
     * Check whether the user's identity has been verified (authenticated).
     *
     * @return {@code true} if the identity is verified
     */
    boolean isVerifyIdentity();

    /**
     * Set the identity verification flag.
     *
     * @param verifyIdentity {@code true} if the identity is verified
     */
    void setVerifyIdentity(boolean verifyIdentity);

    /**
     * Return the verification state as a byte (1 for verified, 0 for unverified).
     *
     * @return {@code 1} if verified, {@code 0} otherwise
     */
    default byte verifyIdentity() {
        return (byte) (isVerifyIdentity() ? 1 : 0);
    }

    /**
     * Copy the user id and verification flag from the given identity.
     *
     * @param userIdentity the source identity to copy from
     */
    default void setUserIdentity(UserIdentity userIdentity) {
        this.setUserId(userIdentity.getUserId());
        this.setVerifyIdentity(userIdentity.isVerifyIdentity());
    }

    /**
     * Bind the given user id and mark the identity as verified.
     *
     * @param userId the user id to bind
     */
    default void bindingUserId(long userId) {
        this.setUserId(userId);
        this.setVerifyIdentity(true);
    }
}
