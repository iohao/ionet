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
package com.iohao.net.server.logic.fragment;

import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.core.SkeletonThreadPipeline;
import com.iohao.net.framework.core.BarSkeletonManager;
import com.iohao.net.framework.communication.CommunicationType;
import com.iohao.net.framework.protocol.Request;
import com.iohao.net.common.OnFragment;
import com.iohao.net.server.NetServerSetting;
import com.iohao.net.server.NetServerSettingAware;
import lombok.extern.slf4j.Slf4j;

/**
 * Base fragment handler for request-like messages dispatched to logic action pipelines.
 *
 * @author 渔民小镇
 * @date 2025-09-14
 * @since 25.1
 */
@Slf4j
abstract class AbstractRequestOnFragment implements OnFragment, NetServerSettingAware {
    SkeletonThreadPipeline skeletonThreadPipeline;

    @Override
    public void setNetServerSetting(NetServerSetting setting) {
        skeletonThreadPipeline = setting.skeletonThreadPipeline();
    }

    protected final void commonProcess(Request message, CommunicationType communicationType) {
        var barSkeleton = BarSkeletonManager.getBarSkeleton(message.getLogicServerId());
        var flowContext = barSkeleton.flowContextFactory.createFlowContext();

        flowContext.setCommunicationType(communicationType);
        flowContext.setRequest(message);
        flowContext.setUserId(message.getUserId());

        var cmdInfo = CmdInfo.of(message.getCmdMerge());
        flowContext.setCmdInfo(cmdInfo);

        var actionCommand = barSkeleton.actionCommands[cmdInfo.cmd()][cmdInfo.subCmd()];

        flowContext.setActionCommand(actionCommand);

        skeletonThreadPipeline.execute(barSkeleton, flowContext);
    }
}
