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

import com.iohao.net.common.kit.*;

/**
 * Gameplay operation related service. Get user's gameplay operations, all gameplay operations, and the gameplay operation factory.
 *
 * @author 渔民小镇
 * @date 2024-05-12
 * @since 21.8
 */
public interface OperationService {
    /**
     * @return Gameplay operation factory
     */
    OperationFactory getOperationFactory();

    /**
     * Get OperationHandler (Gameplay Operation Business Class)
     *
     * @param operation Operation code
     * @return Business logic handler corresponding to the operation code
     */
    default OperationHandler getOperationHandler(int operation) {
        return this.getOperationFactory().getOperationHandler(operation);
    }

    /**
     * Get OperationHandler that the user can operate (Gameplay Operation Business Class)
     *
     * @param operation Operation code
     * @return Gameplay Operation Business Class
     */
    default OperationHandler getUserOperationHandler(int operation) {
        return this.getOperationFactory().getUserOperationHandler(operation);
    }

    default OperationHandler getOperationHandler(OperationCode operationCode) {
        return this.getOperationHandler(operationCode.getOperationCode());
    }

    default OperationHandler getUserOperationHandler(OperationCode operationCode) {
        return this.getUserOperationHandler(operationCode.getOperationCode());
    }
}