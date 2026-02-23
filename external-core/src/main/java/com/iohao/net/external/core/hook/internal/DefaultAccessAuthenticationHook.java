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
package com.iohao.net.external.core.hook.internal;

import com.iohao.net.framework.core.kit.CmdKit;
import com.iohao.net.common.kit.CollKit;
import com.iohao.net.external.core.hook.AccessAuthenticationHook;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * Default in-memory access control implementation for external route checks.
 *
 * @author 渔民小镇
 * @date 2023-02-19
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultAccessAuthenticationHook implements AccessAuthenticationHook {
    final Set<Integer> ignoreCmdMergeSet = CollKit.ofConcurrentSet();
    final Set<Integer> ignoreCmdSet = CollKit.ofConcurrentSet();

    final Set<Integer> rejectionCmdMergeSet = CollKit.ofConcurrentSet();
    final Set<Integer> rejectionCmdSet = CollKit.ofConcurrentSet();

    /** Default: No login required. */
    @Setter
    boolean verifyIdentity;

    @Override
    public void addIgnoreAuthCmd(int cmd, int subCmd) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        this.ignoreCmdMergeSet.add(cmdMerge);
    }

    @Override
    public void addIgnoreAuthCmd(int cmd) {
        this.ignoreCmdSet.add(cmd);
    }

    @Override
    public void removeIgnoreAuthCmd(int cmd, int subCmd) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        this.ignoreCmdMergeSet.remove(cmdMerge);
    }

    @Override
    public void removeIgnoreAuthCmd(int cmd) {
        this.ignoreCmdSet.remove(cmd);
    }

    @Override
    public boolean pass(boolean loginSuccess, int cmdMerge) {

        if (!this.verifyIdentity) {
            return true;
        }

        return loginSuccess
               || this.ignoreCmdMergeSet.contains(cmdMerge)
               || this.ignoreCmdSet.contains(CmdKit.getCmd(cmdMerge));
    }

    @Override
    public void addRejectionCmd(int cmd) {
        this.rejectionCmdSet.add(cmd);
    }

    @Override
    public void addRejectionCmd(int cmd, int subCmd) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        this.rejectionCmdMergeSet.add(cmdMerge);
    }

    @Override
    public void removeRejectCmd(int cmd, int subCmd) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        this.rejectionCmdMergeSet.remove(cmdMerge);
    }

    @Override
    public void removeRejectCmd(int cmd) {
        this.rejectionCmdSet.remove(cmd);
    }

    @Override
    public boolean reject(int cmdMerge) {
        return this.rejectionCmdMergeSet.contains(cmdMerge)
               || this.rejectionCmdSet.contains(CmdKit.getCmd(cmdMerge));
    }

    @Override
    public void clear() {
        this.ignoreCmdSet.clear();
        this.ignoreCmdMergeSet.clear();
        this.rejectionCmdSet.clear();
        this.rejectionCmdMergeSet.clear();
    }
}
