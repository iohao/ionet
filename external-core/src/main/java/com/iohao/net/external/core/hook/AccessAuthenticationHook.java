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
package com.iohao.net.external.core.hook;

import com.iohao.net.external.core.session.*;

/**
 * <a href="https://iohao.github.io/ionet/docs/manual/access_authentication">Routing access control</a>
 *
 * @author 渔民小镇
 * @date 2023-02-19
 */
public interface AccessAuthenticationHook {

    /**
     * Indicates that login is required to access business methods.
     *
     * @param verifyIdentity true if login is required to access business methods
     */
    void setVerifyIdentity(boolean verifyIdentity);

    /**
     * Adds routes that need to be ignored; these ignored routes can be accessed without login.
     *
     * @param cmd    cmd
     * @param subCmd subCmd
     */
    void addIgnoreAuthCmd(int cmd, int subCmd);

    /**
     * Adds main routes that need to be ignored; these ignored main routes can be accessed without login.
     *
     * @param cmd main route
     */
    void addIgnoreAuthCmd(int cmd);

    /**
     * Removes a route that needs to be ignored.
     *
     * @param cmd    cmd
     * @param subCmd subCmd
     */
    void removeIgnoreAuthCmd(int cmd, int subCmd);

    /**
     * Removes a route that needs to be ignored.
     *
     * @param cmd cmd
     */
    void removeIgnoreAuthCmd(int cmd);

    /**
     * Access verification.
     *
     * @param loginSuccess true if the user login was successful {@link UserSession#isVerifyIdentity()}
     * @param cmdMerge     route
     * @return true if access verification passed
     */
    boolean pass(boolean loginSuccess, int cmdMerge);

    /**
     * Adds a main route to be rejected; these main routes cannot be accessed directly from the outside.
     *
     * @param cmd main route
     */
    void addRejectionCmd(int cmd);

    /**
     * Adds a route to be rejected; these routes cannot be accessed directly from the outside.
     *
     * @param cmd    main route
     * @param subCmd sub route
     */
    void addRejectionCmd(int cmd, int subCmd);

    /**
     * Removes a route from rejection.
     *
     * @param cmd    main route
     * @param subCmd sub route
     */
    void removeRejectCmd(int cmd, int subCmd);

    /**
     * Removes a route from rejection.
     *
     * @param cmd main route
     */
    void removeRejectCmd(int cmd);

    /**
     * Rejection check for the route.
     *
     * @param cmdMerge route
     * @return true if the user is forbidden from accessing this route
     */
    boolean reject(int cmdMerge);

    /**
     * Clears all configurations for ignored routes and rejected routes.
     */
    void clear();
}
