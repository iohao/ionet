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
package com.iohao.net.extension.room.operation;

/**
 * Gameplay operation business interface, separating validation from operation.
 * <pre>
 * Tank:
 *     Operations like shooting bullets, launching missiles, etc.
 *
 * Mahjong:
 *     Operations like discarding tiles, Pong (melding triplets), Hu (winning hand), etc.
 *
 * Dou Dizhu (Fight the Landlord):
 *     Operations like playing cards, etc.
 *
 * Turn-based games:
 *     Attacking, etc.
 *
 * Board games:
 *     Dealing cards, etc.
 *     Playing cards, etc.
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @since 21.8
 */
public interface OperationHandler {
    /**
     * Check and verify if the user's operation step is valid, and determine whether to execute the {@link OperationHandler#process(PlayerOperationContext)} method based on the return value.
     * <p>
     * When false is returned, the process method will not be executed, which is equivalent to discarding the processing of the request.
     * It just adds a return value to decide whether to execute the process method.
     *
     * @param context Operation context
     * @return When true is returned, the {@link OperationHandler#process(PlayerOperationContext)} method will be executed
     * @since 21.23
     */
    default boolean processVerify(PlayerOperationContext context) {
        return true;
    }

    /**
     * Execute processing after verification passes
     *
     * @param context Operation context
     */
    void process(PlayerOperationContext context);
}
