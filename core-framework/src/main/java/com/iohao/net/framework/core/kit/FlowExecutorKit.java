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
package com.iohao.net.framework.core.kit;

import com.iohao.net.framework.core.BarSkeleton;
import com.iohao.net.framework.core.flow.FlowContext;

/**
 *
 * @author 渔民小镇
 * @date 2025-10-11
 * @since 25.1
 */
public final class FlowExecutorKit {
    public static void execute(FlowContext flowContext, BarSkeleton barSkeleton) {
        // ---- inOut fuckIn ----
        var inOuts = barSkeleton.inOuts;
        for (var actionMethodInOut : inOuts) {
            actionMethodInOut.fuckIn(flowContext);
        }

        // ---- 1 ActionController ----
        var factoryBean = barSkeleton.actionFactoryBean;
        var actionCommand = flowContext.getActionCommand();
        var controller = factoryBean.getBean(actionCommand);
        flowContext.setActionController(controller);

        // ---- 2 ActionMethodInvoke ----
        var actionMethodInvoke = barSkeleton.actionMethodInvoke;
        var result = actionMethodInvoke.invoke(flowContext);
        flowContext.setMethodResult(result);
        flowContext.setOriginalMethodResult(result);

        // ---- 3 ActionAfter, response data ----
        var actionAfter = barSkeleton.actionAfter;
        actionAfter.execute(flowContext);

        for (var actionMethodInOut : inOuts) {
            actionMethodInOut.fuckOut(flowContext);
        }
    }
}
