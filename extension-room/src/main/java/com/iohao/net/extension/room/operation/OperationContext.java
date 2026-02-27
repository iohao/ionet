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

import com.iohao.net.extension.domain.*;
import com.iohao.net.extension.room.*;
import com.iohao.net.framework.core.flow.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Room gameplay operation execution context and domain-event payload.
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @since 21.8
 */
@Getter
@Setter
@Accessors(chain = true)
public class OperationContext implements PlayerOperationContext, Eo {
    /** room */
    final Room room;
    /** Gameplay operation business handler */
    final OperationHandler operationHandler;
    /** Specific gameplay data to be operated, usually customized by the developer based on game business logic */
    Object command;
    /** The FlowContext of the current operating player */
    FlowContext flowContext;

    OperationContext(Room room, OperationHandler operationHandler) {
        this.room = room;
        this.operationHandler = operationHandler;
    }

    /**
     * Executes the player's gameplay operation, including verification and processing.
     */
    public void execute() {
        if (this.operationHandler.processVerify(this)) {
            this.operationHandler.process(this);
        }
    }

    /**
     * Creates an OperationContext object
     *
     * @param room             Room
     * @param operationHandler Gameplay operation business interface
     * @return OperationContext Gameplay operation context
     */
    public static OperationContext of(Room room, OperationHandler operationHandler) {
        return new OperationContext(room, operationHandler);
    }
}
