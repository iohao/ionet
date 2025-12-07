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
package com.iohao.net.framework.core;

import com.iohao.net.framework.core.doc.JavaClassDocInfo;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import com.iohao.net.common.kit.CollKit;

import java.util.Collection;
import java.util.Map;

/**
 * ActionCommandRegion
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
@FieldDefaults(level = AccessLevel.PUBLIC)
public final class ActionCommandRegion {
    final int cmd;
    /** actionControllerClazz */
    Class<?> actionControllerClazz;
    /** actionControllerClazz 的源文件信息 */
    JavaClassDocInfo javaClassDocInfo;
    /** key: subCmd */
    Map<Integer, ActionCommand> subActionCommandMap = CollKit.ofConcurrentHashMap();

    public ActionCommandRegion(int cmd) {
        this.cmd = cmd;
    }

    public boolean containsKey(int subCmd) {
        return this.subActionCommandMap.containsKey(subCmd);
    }

    public void add(ActionCommand subActionCommand) {
        var cmdInfo = subActionCommand.cmdInfo;

        int subCmd = cmdInfo.subCmd();

        this.subActionCommandMap.put(subCmd, subActionCommand);
    }

    public int getMaxSubCmd() {
        return subActionCommandMap
                .keySet()
                .stream()
                .max(Integer::compareTo)
                .orElse(0);
    }

    public Collection<ActionCommand> values() {
        return this.subActionCommandMap.values();
    }

    public ActionCommand[] arrayActionCommand() {
        int subCmdMax = this.getMaxSubCmd() + 1;
        ActionCommand[] subBehaviors = new ActionCommand[subCmdMax];

        for (Map.Entry<Integer, ActionCommand> subEntry : subActionCommandMap.entrySet()) {
            subBehaviors[subEntry.getKey()] = subEntry.getValue();
        }

        return subBehaviors;
    }
}
