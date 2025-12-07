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
package com.iohao.net.extension.client.user;

import com.iohao.net.common.kit.LocaleKit;
import com.iohao.net.common.kit.concurrent.TaskKit;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * PressureKit
 *
 * @author 渔民小镇
 * @date 2023-07-16
 */
@Slf4j
@UtilityClass
public class PressureKit {
    final Queue<Runnable> loginAfterTaskQueue = new LinkedBlockingQueue<>();
    final AtomicBoolean loginSuccess = new AtomicBoolean();
    final List<ClientUser> clientUsers = new CopyOnWriteArrayList<>();


    public void addClientUser(ClientUser clientUser) {
        clientUsers.add(clientUser);
    }

    private boolean isLoginSuccess() {

        if (loginSuccess.get()) {
            return true;
        }

        int size = 0;
        for (ClientUser clientUser : clientUsers) {
            if (clientUser.getUserId() != 0) {
                size++;
            }
        }

        loginSuccess.set(clientUsers.size() == size);
        return loginSuccess.get();
    }

    /**
     *
     * Tasks to be executed after all users have completed login
     *
     * @param task task
     */
    public void addAfterLoginTask(Runnable task) {
        loginAfterTaskQueue.add(task);
    }

    static {
        TaskKit.executeVirtual(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }

            while (true) {
                boolean loginSuccess = PressureKit.isLoginSuccess();
                if (loginSuccess) {
                    break;
                }

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }

            TaskKit.executeVirtual(() -> {
                if (clientUsers.size() > 1) {
                    int sleep = 2;
                    if (LocaleKit.isChina()) {
                        log.info("[{}]个用户全部登录完成，[{}]秒后开始执行任务[{}]", clientUsers.size(), sleep, loginAfterTaskQueue.size());
                    } else {
                        log.info("[{}] users have completed login, executing task [{}] after [{}] seconds", clientUsers.size(), loginAfterTaskQueue.size(), sleep);
                    }

                    try {
                        TimeUnit.SECONDS.sleep(sleep);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }

                Runnable take;
                while ((take = loginAfterTaskQueue.poll()) != null) {
                    TaskKit.executeVirtual(take);
                }
            });
        });
    }
}
