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
package com.iohao.net.framework.core.doc;

import com.iohao.net.common.kit.*;
import com.iohao.net.framework.core.*;
import java.util.*;
import java.util.stream.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Documentation model for a single {@code @ActionController}, grouping all
 * {@link ActionCommandDoc} entries that share the same primary command ID.
 *
 * @author 渔民小镇
 * @date 2023-07-13
 */
@FieldDefaults(level = AccessLevel.PUBLIC)
public final class ActionDoc {
    final int cmd;
    final Class<?> controllerClazz;
    /** Action method docs keyed by subCmd. */
    final Map<Integer, ActionCommandDoc> actionCommandDocMap = CollKit.ofConcurrentHashMap();

    JavaClassDocInfo javaClassDocInfo;

    /**
     * Create a new action doc for the given command ID and controller class.
     *
     * @param cmd             the primary command ID
     * @param controllerClazz the controller class
     */
    public ActionDoc(int cmd, Class<?> controllerClazz) {
        this.cmd = cmd;
        this.controllerClazz = controllerClazz;
    }

    /**
     * Register an {@link ActionCommandDoc} under its sub-command ID.
     *
     * @param actionCommandDoc the command doc to add
     */
    public void addActionCommandDoc(ActionCommandDoc actionCommandDoc) {
        int subCmd = actionCommandDoc.subCmd;
        this.actionCommandDocMap.put(subCmd, actionCommandDoc);
    }

    /**
     * Associate a parsed {@link ActionCommand} with its existing doc entry.
     *
     * @param actionCommand the action command to link
     */
    public void addActionCommand(ActionCommand actionCommand) {
        CmdInfo cmdInfo = actionCommand.cmdInfo;
        int subCmd = cmdInfo.subCmd();
        if (actionCommandDocMap.containsKey(subCmd)) {
            ActionCommandDoc actionCommandDoc = actionCommandDocMap.get(subCmd);
            actionCommandDoc.actionCommand = actionCommand;
        }
    }

    /**
     * Return a stream of command docs sorted by sub-command ID.
     *
     * @return sorted stream of {@link ActionCommandDoc}
     */
    public Stream<ActionCommandDoc> stream() {
        return actionCommandDocMap
                .values()
                .stream()
                .sorted(Comparator.comparingInt(o -> o.subCmd));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ActionDoc that)) {
            return false;
        }

        return cmd == that.cmd && Objects.equals(controllerClazz, that.controllerClazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cmd, controllerClazz);
    }
}
