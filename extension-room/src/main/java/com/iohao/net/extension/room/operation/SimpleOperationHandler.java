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
 * Executes the task {@link Runnable}. The task must be placed in {@link OperationContext#setCommand(Object)}.
 *
 * @author 渔民小镇
 * @date 2024-12-09
 * @since 21.23
 */
public final class SimpleOperationHandler implements OperationHandler {
    @Override
    public void process(PlayerOperationContext context) {
        if (context.getCommand() instanceof Runnable runnable) {
            runnable.run();
        }
    }

    private SimpleOperationHandler() {
    }

    public static SimpleOperationHandler me() {
        return Holder.ME;
    }

    private static class Holder {
        static final SimpleOperationHandler ME = new SimpleOperationHandler();
    }
}