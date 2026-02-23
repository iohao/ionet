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
package com.iohao.net.framework.core.runner;

import com.iohao.net.framework.core.BarSkeleton;
import com.iohao.net.common.kit.concurrent.TaskKit;
import com.iohao.net.common.kit.exception.ThrowKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Manages the lifecycle of {@link Runner} instances registered with a {@link BarSkeleton}.
 * <p>
 * Runners are invoked in two phases:
 * <ol>
 *   <li>{@link #onStart()} -- before the network connection is established.</li>
 *   <li>{@link #onStartAfter()} -- after the network connection is established (delayed by one second).</li>
 * </ol>
 * Adding runners after {@link #onStart()} has been called will throw an exception.
 *
 * @author 渔民小镇
 * @date 2023-04-23
 * @see Runner
 */
@FieldDefaults(level = AccessLevel.PACKAGE)
public final class Runners {
    @Getter
    final List<Runner> runnerList = new CopyOnWriteArrayList<>();
    final AtomicBoolean onStart = new AtomicBoolean(false);
    final AtomicBoolean onStartAfter = new AtomicBoolean(false);
    final List<Runner> runnerBeforeList = new CopyOnWriteArrayList<>();

    @Setter
    BarSkeleton barSkeleton;

    /** Create a new instance and register internal runners. */
    public Runners() {
        new InternalRunner(this);
    }

    /**
     * Register a runner to be executed during the startup lifecycle.
     *
     * @param runner the runner to add (must not be null)
     * @throws RuntimeException if runners have already been started
     */
    public void addRunner(Runner runner) {

        if (this.onStart.get()) {
            ThrowKit.ofRuntimeException("Cannot add Runner while running.");
        }

        this.runnerList.add(Objects.requireNonNull(runner));
    }

    /** Execute all runners' {@link Runner#onStart(BarSkeleton)} callbacks (idempotent). */
    public void onStart() {
        if (this.onStart.get()) {
            return;
        }

        this.runnerBeforeList.forEach(runner -> runner.onStart(this.barSkeleton));

        if (this.onStart.compareAndSet(false, true)) {
            this.runnerList.forEach(runner -> runner.onStart(this.barSkeleton));
        }
    }

    /** Execute all runners' {@link Runner#onStartAfter(BarSkeleton)} callbacks (idempotent, delayed). */
    public void onStartAfter() {
        if (this.onStartAfter.get()) {
            return;
        }

        if (this.onStartAfter.compareAndSet(false, true)) {
            TaskKit.runOnceSecond(() -> {
                this.runnerList.forEach(runner -> runner.onStartAfter(this.barSkeleton));
            });

            runnerBeforeList.clear();
        }
    }

    /**
     * Return the display names of all registered runners.
     *
     * @return list of runner names
     */
    public List<String> listRunnerName() {
        return this.runnerList.stream()
                .map(Runner::name)
                .collect(Collectors.toList());
    }
}
