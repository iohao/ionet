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

import com.iohao.net.common.kit.CollKit;
import com.iohao.net.common.kit.LocaleKit;
import com.iohao.net.common.kit.StrKit;
import com.iohao.net.common.kit.concurrent.TaskKit;
import com.iohao.net.common.kit.time.TimeFormatKit;
import com.iohao.net.common.kit.time.TimeKit;
import com.iohao.net.framework.communication.*;
import com.iohao.net.framework.i18n.Bundle;
import com.iohao.net.framework.i18n.MessageKey;
import com.iohao.net.framework.protocol.BroadcastMessage;
import com.iohao.net.framework.protocol.BroadcastMulticastMessage;
import com.iohao.net.framework.protocol.BroadcastUserListMessage;
import com.iohao.net.framework.protocol.BroadcastUserMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Default broadcast trace implementation that prints the business call site and broadcast payload diagnostics.
 *
 * @author 渔民小镇
 * @date 2025-10-09
 * @since 25.1
 */
@Slf4j
public final class DefaultBroadcastTrace implements BroadcastTrace {
    final Map<String, Integer> traceMap = CollKit.ofConcurrentHashMap();

    public DefaultBroadcastTrace() {
        traceConfig(BroadcastCommunication.class, 1);
        traceConfig(CommunicationAggregation.class, 1);

        traceConfig(BroadcastUserCommunicationDecorator.class, 1);
        traceConfig(BroadcastUserListCommunicationDecorator.class, 1);
        traceConfig(BroadcastMulticastCommunicationDecorator.class, 1);

        traceConfig(RangeBroadcast.class, 1);
        traceConfig("com.iohao.net.extension.room.DefaultRangeBroadcast", 1);

        traceConfig("com.iohao.net.action.skeleton.core.flow.SimpleBroadcastMeCommunication", 1);
        traceConfig("com.iohao.net.action.skeleton.core.flow.SimpleBroadcastUserCommunication", 1);
        traceConfig("com.iohao.net.action.skeleton.core.flow.SimpleBroadcastUserListCommunication", 1);
        traceConfig("com.iohao.net.action.skeleton.core.flow.SimpleBroadcastMulticastCommunication", 1);
    }

    @Override
    public void trace(BroadcastMessage message) {
        RuntimeException ex = new RuntimeException();
        TaskKit.execute(() -> extractedTrace(message, ex));
    }

    public void traceConfig(String name, int value) {
        traceMap.put(name, value);
    }

    private void extractedTrace(BroadcastMessage message, RuntimeException ex) {
        StackTraceElement[] traces = ex.getStackTrace();
        int index = lookBusinessCodeInTrace(traces);
        StackTraceElement traceElement = traces[index];

        String userIds = getUserIds(message);
        Object originalData = message.getOriginalData();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("originalData", originalData == null ? "" : originalData);
        paramMap.put("userIds", userIds);
        paramMap.put("className", traceElement.getFileName());
        paramMap.put("lineNumber", traceElement.getLineNumber());
        paramMap.put("cmdInfo", CmdInfo.of(message.getCmdMerge()));
        paramMap.put("time", TimeFormatKit.format(TimeKit.nowLocalDateTime()));

        paramMap.put("broadcastTitle", Bundle.getMessage(MessageKey.broadcastTitle));
        paramMap.put("broadcastData", Bundle.getMessage(MessageKey.broadcastData));
        paramMap.put("broadcastTime", Bundle.getMessage(MessageKey.broadcastTime));

        String template = """
                ┏━━━━━ {broadcastTitle}. [({className}:{lineNumber})] ━━━ {cmdInfo}
                ┣ userId: {userIds}
                ┣ {broadcastData}: {originalData}
                ┣ {broadcastTime}: {time}
                ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                """;

        System.out.println(StrKit.format(template, paramMap));
    }

    private String getUserIds(BroadcastMessage broadcastMessage) {

        if (broadcastMessage instanceof BroadcastMulticastMessage) {
            return LocaleKit.isChina()
                    ? "全服广播"
                    : "BroadcastMulticast";
        }

        if (broadcastMessage instanceof BroadcastUserMessage broadcastUserMessage) {
            return String.valueOf(broadcastUserMessage.getUserId());
        }

        if (broadcastMessage instanceof BroadcastUserListMessage broadcastUserListMessage) {
            long[] userIds = broadcastUserListMessage.getUserIds();
            return Arrays.toString(userIds);
        }

        return "";
    }

    private int lookBusinessCodeInTrace(StackTraceElement[] traces) {
        for (int index = traces.length - 1; index >= 0; index--) {
            String name = traces[index].getClassName();

            Integer value = traceMap.get(name);
            if (value != null) {
                return index + value;
            }
        }

        return 2;
    }
}
