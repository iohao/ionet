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
package com.iohao.net.common.kit.concurrent.timer.delay;

import com.iohao.net.common.kit.*;
import com.iohao.net.common.kit.concurrent.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import lombok.*;
import lombok.extern.slf4j.*;

/**
 * Internal executor interface for delay task lifecycle management.
 * <p>
 * Extends {@link DelayTask} with timer-driven callback methods for controlling
 * when and how a delay task is triggered, updated, and error-handled.
 */
interface DelayTaskExecutor extends DelayTask {
    /**
     * Determine whether the {@link #onUpdate()} callback should be triggered.
     *
     * @return {@code true} if the task is ready to execute its update callback
     */
    boolean triggerUpdate();

    /**
     * Execute the timer listener callback when the delay has elapsed.
     */
    void onUpdate();

    /**
     * Handle exceptions thrown by {@link #triggerUpdate()} or {@link #onUpdate()}.
     *
     * @param e the exception that occurred during task execution
     */
    default void onException(Throwable e) {
        System.err.println(e.getMessage());
    }

    /**
     * Get the executor used to run the {@link #onUpdate()} callback.
     *
     * @return the executor to use, or {@code null} to run on the current thread (default HashedWheelTimer thread)
     */
    Executor getExecutor();
}

/**
 * Enhanced delay task region interface for internal lifecycle management.
 * <p>
 * Extends {@link DelayTaskRegion} with methods to stop the region, iterate over
 * active tasks, and register tasks for execution.
 */
interface DelayTaskRegionEnhance extends DelayTaskRegion {
    /**
     * Stop the region and deactivate its interval task listener.
     */
    void stop();

    /**
     * Iterate over all active delay task executors in this region.
     *
     * @param consumer the action to perform on each delay task executor
     */
    void forEach(Consumer<DelayTaskExecutor> consumer);

    /**
     * Register and start tracking a delay task executor in this region.
     *
     * @param delayTaskExecutor the delay task executor to register
     */
    void runDelayTask(DelayTaskExecutor delayTaskExecutor);
}

/**
 * Internal interval task listener that periodically processes all active delay tasks in a region.
 * <p>
 * On each interval tick, iterates over all registered {@link DelayTaskExecutor} instances,
 * dispatching each to its configured executor (or running inline) and removing inactive tasks.
 */
@Getter
final class DelayIntervalTaskListener implements IntervalTaskListener {
    final DelayTaskRegionEnhance delayTaskRegion;
    boolean active = true;

    /**
     * Create an interval task listener bound to the given delay task region.
     *
     * @param delayTaskRegion the region whose tasks this listener will process
     */
    DelayIntervalTaskListener(DelayTaskRegionEnhance delayTaskRegion) {
        this.delayTaskRegion = delayTaskRegion;
    }

    /**
     * Process all active delay tasks in the region on each interval tick.
     * <p>
     * Each task is dispatched to its own executor if one is configured,
     * otherwise it runs on the current timer thread.
     */
    @Override
    public void onUpdate() {
        this.delayTaskRegion.forEach(task -> {
            var executor = task.getExecutor();

            if (Objects.nonNull(executor)) {
                executor.execute(() -> extractedFlowTaskListener(task));
            } else {
                this.extractedFlowTaskListener(task);
            }
        });
    }

    /**
     * Execute a single delay task: remove it if inactive, otherwise check and trigger its update.
     *
     * @param task the delay task executor to process
     */
    private void extractedFlowTaskListener(DelayTaskExecutor task) {
        try {
            // Remove inactive delay tasks
            if (!task.isActive()) {
                this.delayTaskRegion.cancel(task.getTaskId());
                return;
            }

            if (task.triggerUpdate()) {
                task.onUpdate();
            }
        } catch (Throwable e) {
            task.onException(e);
        }
    }
}

/**
 * Default implementation of {@link DelayTask} backed by a simple map-based region.
 * <p>
 * Tracks remaining delay time via a {@link LongAdder} and uses an {@link AtomicBoolean}
 * for thread-safe active state management. Each tick decrements the remaining time;
 * when it reaches zero, the task listener is invoked.
 */
@Getter
class SimpleDelayTask implements DelayTaskExecutor {
    /** Global counter for generating unique task IDs when none is provided. */
    static final AtomicLong taskIdCounter = new AtomicLong();

    final String taskId;
    final TaskListener taskListener;
    final DelayTaskRegionEnhance delayTaskRegion;
    final LongAdder timeMillis = new LongAdder();
    final AtomicBoolean active = new AtomicBoolean(true);

    /**
     * Create a delay task with an auto-generated task ID.
     *
     * @param taskListener    the listener callback for this task
     * @param delayTaskRegion the region managing this task
     */
    SimpleDelayTask(TaskListener taskListener, DelayTaskRegionEnhance delayTaskRegion) {
        this(String.valueOf(taskIdCounter.getAndIncrement()), taskListener, delayTaskRegion);
    }

    /**
     * Create a delay task with the specified task ID.
     *
     * @param taskId          the unique identifier for this task
     * @param taskListener    the listener callback for this task
     * @param delayTaskRegion the region managing this task
     */
    SimpleDelayTask(String taskId, TaskListener taskListener, DelayTaskRegionEnhance delayTaskRegion) {
        this.taskId = taskId;
        this.taskListener = taskListener;
        this.delayTaskRegion = delayTaskRegion;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isActive() {
        return this.active.get();
    }

    /** {@inheritDoc} */
    @Override
    public void cancel() {
        if (this.isActive() && this.active.compareAndSet(true, false)) {
            this.delayTaskRegion.cancel(this.taskId);
        }
    }

    /** {@inheritDoc} */
    @Override
    public long getMillis() {
        var sum = this.timeMillis.sum();
        return sum < 0 ? 0 : sum;
    }

    /** {@inheritDoc} */
    @Override
    public DelayTask plusTimeMillis(long millis) {
        if (this.isActive()) {
            this.timeMillis.add(millis);
        }

        return this;
    }

    /**
     * Cancel this task and invoke the underlying task listener's callback if triggered.
     */
    @Override
    public void onUpdate() {

        this.cancel();

        if (this.taskListener.triggerUpdate()) {
            this.taskListener.onUpdate();
        }
    }

    /** Negative interval used to decrement remaining time on each tick. */
    static final long INTERVAL_MILLIS_CONSUMER = -SimpleDelayTaskRegion.INTERVAL_MILLIS * 2;

    /**
     * Decrement the remaining delay time and determine whether the task should fire.
     *
     * @return {@code true} if the task is active and its remaining time has reached zero
     */
    @Override
    public boolean triggerUpdate() {
        // Execute the task when remaining time <= 0
        this.plusTimeMillis(INTERVAL_MILLIS_CONSUMER);

        return this.isActive() && this.getMillis() <= 0;
    }

    /** {@inheritDoc} */
    @Override
    public void onException(Throwable e) {
        this.taskListener.onException(e);
    }

    /** {@inheritDoc} */
    @Override
    public Executor getExecutor() {
        return this.taskListener.getExecutor();
    }

    /**
     * Start this delay task by registering it with the owning region.
     *
     * @return this task instance for fluent chaining
     */
    @Override
    public DelayTask task() {
        if (this.isActive()) {
            this.delayTaskRegion.runDelayTask(this);
        }

        return this;
    }

    @Override
    public String toString() {
        return "SimpleDelayTask{" +
                "taskId='" + this.taskId + '\'' +
                ", active=" + this.active +
                ", timeMillis=" + getMillis() +
                '}';
    }
}

/**
 * Default implementation of {@link DelayTaskRegion} using a ConcurrentHashMap.
 * <p>
 * Manages delay tasks in a thread-safe map and drives them with a periodic
 * {@link DelayIntervalTaskListener} running at a fixed interval.
 */
class SimpleDelayTaskRegion implements DelayTaskRegion, DelayTaskRegionEnhance {
    /** Interval in milliseconds between each tick of the delay task processor. */
    static final long INTERVAL_MILLIS = 50;

    final Map<String, DelayTaskExecutor> taskMap = CollKit.ofConcurrentHashMap();
    final DelayIntervalTaskListener taskListener;

    /**
     * Create a new region and start its periodic interval task listener.
     */
    SimpleDelayTaskRegion() {
        this.taskListener = new DelayIntervalTaskListener(this);
        TaskKit.runInterval(this.taskListener, INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
    }

    /** {@inheritDoc} */
    @Override
    public void forEach(Consumer<DelayTaskExecutor> consumer) {
        this.taskMap.values().forEach(consumer);
    }

    /** {@inheritDoc} */
    @Override
    public void runDelayTask(DelayTaskExecutor delayTaskExecutor) {
        this.taskMap.put(delayTaskExecutor.getTaskId(), delayTaskExecutor);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<DelayTask> optional(String taskId) {
        var task = this.taskMap.get(taskId);
        return Optional.ofNullable(task);
    }

    /** {@inheritDoc} */
    @Override
    public void cancel(String taskId) {
        var task = this.taskMap.remove(taskId);
        if (Objects.nonNull(task) && task.isActive()) {
            task.cancel();
        }
    }

    /** {@inheritDoc} */
    @Override
    public int count() {
        return this.taskMap.size();
    }

    /** {@inheritDoc} */
    @Override
    public void stop() {
        this.taskListener.active = false;
    }

    /** {@inheritDoc} */
    @Override
    public DelayTask of(TaskListener taskListener) {
        return new SimpleDelayTask(taskListener, this);
    }

    /** {@inheritDoc} */
    @Override
    public DelayTask of(String taskId, TaskListener taskListener) {
        return new SimpleDelayTask(taskId, taskListener, this);
    }
}

/**
 * Debug-friendly {@link DelayTask} implementation that logs task state transitions.
 * <p>
 * Extends {@link SimpleDelayTask} with additional elapsed-time tracking and
 * logging of remaining task count on each update for diagnostic purposes.
 */
@Slf4j
final class DebugDelayTask extends SimpleDelayTask {
    /** Accumulated total elapsed time in milliseconds since the task was started. */
    final LongAdder sumMillis = new LongAdder();

    /**
     * Create a debug delay task with the specified task ID.
     *
     * @param taskId          the unique identifier for this task
     * @param taskListener    the listener callback for this task
     * @param delayTaskRegion the region managing this task
     */
    DebugDelayTask(String taskId, TaskListener taskListener, DelayTaskRegionEnhance delayTaskRegion) {
        super(taskId, taskListener, delayTaskRegion);
    }

    /**
     * Create a debug delay task with an auto-generated task ID.
     *
     * @param taskListener    the listener callback for this task
     * @param delayTaskRegion the region managing this task
     */
    DebugDelayTask(TaskListener taskListener, DelayTaskRegionEnhance delayTaskRegion) {
        super(taskListener, delayTaskRegion);
    }

    /**
     * Execute the task listener callback and log the remaining task count in the region.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();
        log.info("Remaining task count {}, {}", this.delayTaskRegion.count(), this);
    }

    /**
     * Decrement remaining time, accumulate total elapsed time, and check whether to fire.
     *
     * @return {@code true} if the task is active and its remaining time has reached zero
     */
    @Override
    public boolean triggerUpdate() {
        // Accumulate total elapsed time
        this.sumMillis.add(Math.abs(INTERVAL_MILLIS_CONSUMER));

        return super.triggerUpdate();
    }

    @Override
    public String toString() {
        return "DebugDelayTask{" +
                "taskId='" + this.taskId + '\'' +
                ", active=" + this.active +
                ", timeMillis=" + this.timeMillis +
                ", sumMillis=" + this.sumMillis +
                "} ";
    }
}

/**
 * Debug-friendly {@link DelayTaskRegion} implementation with logging.
 * <p>
 * Extends {@link SimpleDelayTaskRegion} to produce {@link DebugDelayTask} instances
 * instead of plain {@link SimpleDelayTask}, enabling diagnostic logging of task lifecycle.
 */
final class DebugDelayTaskRegion extends SimpleDelayTaskRegion {
    /** {@inheritDoc} */
    @Override
    public DelayTask of(TaskListener taskListener) {
        return new DebugDelayTask(taskListener, this);
    }

    /** {@inheritDoc} */
    @Override
    public DelayTask of(String taskId, TaskListener taskListener) {
        return new DebugDelayTask(taskId, taskListener, this);
    }
}