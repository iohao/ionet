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

import com.iohao.net.common.kit.OperationCode;

import java.util.Optional;

/**
 * Gameplay Operation Factory
 *
 * @author 渔民小镇
 * @date 2024-05-12
 * @since 21.8
 */
public interface OperationFactory {
    /**
     * Get OperationHandler (Gameplay Operation Business Class)
     *
     * @param operation Operation code
     * @return Business logic handler corresponding to the operation code
     */
    OperationHandler getOperationHandler(int operation);

    /**
     * Get OperationHandler that the user can operate (Gameplay Operation Business Class)
     *
     * @param operation Operation code
     * @return Gameplay Operation Business Class
     */
    OperationHandler getUserOperationHandler(int operation);

    /**
     * Associate the operation code with the OperationHandler (Gameplay Operation Business Class)
     *
     * @param operation        Operation code
     * @param operationHandler Gameplay Operation Business Class
     */
    void mapping(int operation, OperationHandler operationHandler);

    /**
     * OperationHandler that the user can operate. Associate the operation code with the OperationHandler (Gameplay Operation Business Class)
     *
     * @param operation        Operation code
     * @param operationHandler Gameplay Operation Business Class
     */
    void mappingUser(int operation, OperationHandler operationHandler);

    /**
     * OperationHandler that the user can operate. Associate the operation code with the OperationHandler (Gameplay Operation Business Class)
     *
     * @param operationCode    Operation code
     * @param operationHandler Gameplay Operation Business Class
     * @since 21.23
     */
    default void mappingUser(OperationCode operationCode, OperationHandler operationHandler) {
        this.mappingUser(operationCode.getOperationCode(), operationHandler);
    }

    /**
     * Associate the operation code with the OperationHandler (Gameplay Operation Business Class)
     *
     * @param operationCode    Operation code
     * @param operationHandler Gameplay Operation Business Class
     * @since 21.23
     */
    default void mapping(OperationCode operationCode, OperationHandler operationHandler) {
        this.mapping(operationCode.getOperationCode(), operationHandler);
    }

    /**
     * Get OperationHandler Optional by operation code
     *
     * @param operation Operation code
     * @return Optional OperationHandler
     */
    Optional<OperationHandler> optionalOperationHandler(int operation);

    /**
     * Create OperationFactory object (Built-in implementation provided by the framework)
     *
     * @return OperationFactory object
     */
    static OperationFactory of() {
        return new SimpleOperationFactory();
    }
}