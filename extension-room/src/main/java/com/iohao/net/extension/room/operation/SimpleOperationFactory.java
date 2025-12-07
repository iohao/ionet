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

import com.iohao.net.common.kit.CollKit;
import com.iohao.net.common.kit.exception.ThrowKit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.Optional;

/**
 * Gameplay Operation Factory (Flyweight) implementation class (Built-in implementation)
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @since 21.8
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
final class SimpleOperationFactory implements OperationFactory {
    /**
     * Operation handling
     * <pre>
     * key : Operation code
     * value : Business logic handler corresponding to the operation code
     * </pre>
     */
    final Map<Integer, OperationHandler> operationMap = CollKit.ofConcurrentHashMap();
    /**
     * Operation handling that the user can operate
     * <pre>
     * key : Operation code
     * value : Business logic handler corresponding to the operation code
     * </pre>
     */
    final Map<Integer, OperationHandler> userOperationMap = CollKit.ofConcurrentHashMap();

    public OperationHandler getUserOperationHandler(int operation) {
        return this.userOperationMap.get(operation);
    }

    public OperationHandler getOperationHandler(int operation) {
        return this.operationMap.get(operation);
    }

    public void mapping(int operation, OperationHandler operationHandler) {
        if (this.operationMap.containsKey(operation)) {
            ThrowKit.ofRuntimeException("operation already exists : " + operation);
        }

        this.operationMap.put(operation, operationHandler);
    }

    public void mappingUser(int operation, OperationHandler operationHandler) {
        this.mapping(operation, operationHandler);

        if (this.userOperationMap.containsKey(operation)) {
            ThrowKit.ofRuntimeException("operation already exists : " + operation);
        }

        this.userOperationMap.put(operation, operationHandler);
    }

    public Optional<OperationHandler> optionalOperationHandler(int operation) {
        return Optional.ofNullable(this.operationMap.get(operation));
    }

    SimpleOperationFactory() {
    }
}