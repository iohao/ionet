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
package com.iohao.net.external.core.netty.micro;

import com.iohao.net.common.kit.*;
import com.iohao.net.common.kit.concurrent.*;
import io.netty.channel.*;
import io.netty.channel.epoll.*;
import io.netty.channel.kqueue.*;
import io.netty.channel.nio.*;
import io.netty.channel.socket.nio.*;
import java.util.concurrent.*;
import lombok.experimental.*;

/**
 * Selects Netty event-loop/channel implementations based on the current operating system.
 *
 * @author 渔民小镇
 * @date 2025-09-24
 * @since 25.1
 */
@UtilityClass
public final class GroupChannelKit {
    /** Current OS-specific Netty group/channel configuration. */
    public GroupChannelOption groupChannelOption = createGroupChannelOption();

    private GroupChannelOption createGroupChannelOption() {
        if (OsInfo.isMac()) {
            return new GroupChannelOptionForMac();
        }

        // Lightweight or embedded Linux distributions may not have fulled I/O multiplexing support
        if (OsInfo.isLinux() && Epoll.isAvailable()) {
            return new GroupChannelOptionForLinux();
        }

        // other system nio
        return new GroupChannelOptionForOther();
    }

    private final class EventLoopGroupThreadFactory {
        static ThreadFactory workerThreadFactory() {
            return new DaemonThreadFactory("iohao.com:external-worker");
        }

        static ThreadFactory bossThreadFactory() {
            return new DaemonThreadFactory("iohao.com:external-boss");
        }
    }

    /**
     * For Linux（Epoll）
     *
     * @author 渔民小镇
     * @date 2023-02-18
     */
    private final class GroupChannelOptionForLinux implements GroupChannelOption {
        @Override
        public EventLoopGroup bossGroup() {
            return new EpollEventLoopGroup(
                    1,
                    EventLoopGroupThreadFactory.bossThreadFactory()
            );
        }

        @Override
        public EventLoopGroup workerGroup() {
            int availableProcessors = Runtime.getRuntime().availableProcessors() << 1;

            return new EpollEventLoopGroup(
                    availableProcessors,
                    EventLoopGroupThreadFactory.workerThreadFactory()
            );
        }

        @Override
        public Class<? extends ServerChannel> channelClass() {
            return EpollServerSocketChannel.class;
        }
    }

    /**
     * For Mac（KQueue）
     *
     * @author 渔民小镇
     * @date 2023-02-18
     */
    private final class GroupChannelOptionForMac implements GroupChannelOption {
        @Override
        public EventLoopGroup bossGroup() {
            return new KQueueEventLoopGroup(
                    1,
                    EventLoopGroupThreadFactory.bossThreadFactory()
            );
        }

        @Override
        public EventLoopGroup workerGroup() {
            int availableProcessors = Runtime.getRuntime().availableProcessors() << 1;

            return new KQueueEventLoopGroup(
                    availableProcessors,
                    EventLoopGroupThreadFactory.workerThreadFactory()
            );
        }

        @Override
        public Class<? extends ServerChannel> channelClass() {
            return KQueueServerSocketChannel.class;
        }
    }

    /**
     * For OtherSystem（NIO）
     *
     * @author 渔民小镇
     * @date 2023-02-18
     */
    private final class GroupChannelOptionForOther implements GroupChannelOption {
        @Override
        public EventLoopGroup bossGroup() {
            return new NioEventLoopGroup(
                    1,
                    EventLoopGroupThreadFactory.bossThreadFactory()
            );
        }

        @Override
        public EventLoopGroup workerGroup() {
            int availableProcessors = Runtime.getRuntime().availableProcessors() << 1;

            return new NioEventLoopGroup(
                    availableProcessors,
                    EventLoopGroupThreadFactory.workerThreadFactory()
            );
        }

        @Override
        public Class<? extends ServerChannel> channelClass() {
            return NioServerSocketChannel.class;
        }
    }
}
