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
package com.iohao.net.external.core.micro;

import com.iohao.net.external.core.ExternalSettingAware;

/**
 * The startup process for a server that connects with real players.
 * <pre>
 * Developers can use this interface to orchestrate the server, with orchestration divided into two phases: build time and new connection time.
 *
 * You can also selectively override the process methods to customize the business logic for your own projects.
 * The framework provides implementations for TCP, WebSocket, and UDP.
 * </pre>
 * <p>
 * The execution order of the interface methods is:
 * <pre>
 * 1. The execution flow for [Build Time], where createFlow calls the option and channelInitializer methods.
 * 1.1 option
 * 1.2 channelInitializer
 *
 * 2. The execution flow for [New Connection Time], where pipelineFlow calls pipelineCodec, pipelineIdle, and pipelineCustom.
 * 2.1 pipelineCodec
 * 2.2 pipelineIdle
 * 2.3 pipelineCustom
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-05-28
 */
public interface MicroBootstrapFlow<Bootstrap> extends ExternalSettingAware {
    /**
     * Configures some options for the server. The server is not yet started at this point.
     *
     * @param bootstrap The server bootstrap.
     */
    void option(Bootstrap bootstrap);

    /**
     * Arranges some business orchestration for the server. The server is not yet started at this point.
     *
     * @param bootstrap The server bootstrap.
     */
    void channelInitializer(Bootstrap bootstrap);

    /**
     * The execution flow when a new connection is established.
     *
     * <pre>
     * Typically, we can divide the implementation within ChannelInitializer into three parts:
     * 1. pipelineCodec: Encoding and decoding.
     * 2. pipelineIdle: Heartbeat-related logic.
     * 3. pipelineCustom: Custom business orchestration (in most cases, just overriding pipelineCustom is enough for strong extension).
     * </pre>
     *
     * @param pipelineContext context
     */
    default void pipelineFlow(PipelineContext pipelineContext) {
        pipelineCodec(pipelineContext);
        pipelineIdle(pipelineContext);
        pipelineCustom(pipelineContext);
    }

    /**
     * Orchestrates the encoding and decoding logic.
     *
     * @param context The PipelineContext.
     */
    void pipelineCodec(PipelineContext context);

    /**
     * Orchestrates the heartbeat-related logic.
     *
     * @param context The PipelineContext.
     */
    void pipelineIdle(PipelineContext context);

    /**
     * Custom business orchestration (to arrange some business logic for the server).
     *
     * @param context The PipelineContext.
     */
    void pipelineCustom(PipelineContext context);
}