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
package com.iohao.net.common.kit.concurrent;

import com.iohao.net.common.kit.*;
import com.iohao.net.common.kit.collect.*;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import lombok.*;
import lombok.experimental.*;
import lombok.extern.slf4j.*;

/**
 * Internal utility class for task consumption; developers should NOT use it for time-consuming I/O tasks.
 * <p>
 * example - Execute task using another thread
 * <pre>{@code
 * TaskKit.execute(()->{
 *     log.info("Your logic");
 * });
 * }
 * </pre>
 * example - netty TimerTask
 * <pre>{@code
 * // Execute after 3 seconds
 * TaskKit.newTimeout(new TimerTask() {
 *     @Override
 *     public void run(Timeout timeout) {
 *         log.info("3-newTimeout : {}", timeout);
 *     }
 * }, 3, TimeUnit.SECONDS);
 * }
 * </pre>
 * example - TaskListener callback. Internally uses HashedWheelTimer to simulate ScheduledExecutorService scheduling
 * <pre>{@code
 * // Execute only once, after 2 seconds
 * TaskKit.runOnce(() -> log.info("2 Seconds"), 2, TimeUnit.SECONDS);
 * // Execute only once, after 1 minute
 * TaskKit.runOnce(() -> log.info("1 Minute"), 1, TimeUnit.MINUTES)
 * // Execute only once, after 500, 800 milliseconds
 * TaskKit.runOnce(() -> log.info("500 delayMilliseconds"), 500);
 * TaskKit.runOnce(() -> log.info("800 delayMilliseconds"), 800);
 *
 * // Called every minute
 * TaskKit.runIntervalMinute(() -> log.info("tick 1 Minute"), 1);
 * // Called every 2 minutes
 * TaskKit.runIntervalMinute(() -> log.info("tick 2 Minute"), 2);
 *
 * // Called every 2 seconds
 * TaskKit.runInterval(() -> log.info("tick 2 Seconds"), 2, TimeUnit.SECONDS);
 * // Called every 30 minutes
 * TaskKit.runInterval(() -> log.info("tick 30 Minute"), 30, TimeUnit.MINUTES);
 * }
 * </pre>
 * example - TaskListener - Advanced Usage
 * <pre>{@code
 * //【Example - Remove Task】Called every second. Remove the current Listener when hp is 0.
 * TaskKit.runInterval(new IntervalTaskListener() {
 *     int hp = 2;
 *
 *     @Override
 *     public void onUpdate() {
 *         hp--;
 *         log.info("Remaining hp:2-{}", hp);
 *     }
 *
 *     @Override
 *     public boolean isActive() {
 *         // Returning false indicates inactive, and the current Listener will be removed from the listener list
 *         return hp != 0;
 *     }
 * }, 1, TimeUnit.SECONDS);
 *
 * //【Example - Skip Execution】Called every second. The onUpdate method is executed only when triggerUpdate returns true (i.e., the condition is met).
 * TaskKit.runInterval(new IntervalTaskListener() {
 *     int hp;
 *
 *     @Override
 *     public void onUpdate() {
 *         log.info("current hp:{}", hp);
 *     }
 *
 *     @Override
 *     public boolean triggerUpdate() {
 *         hp++;
 *         // When the return value is true, the onUpdate method is executed
 *         return hp % 2 == 0;
 *     }
 * }, 1, TimeUnit.SECONDS);
 *
 * //【Example - Specify Thread Executor】Called every second
 * // If there are time-consuming tasks, such as those involving I/O operations, it is recommended to specify an executor to run the current callback (onUpdate method) to avoid blocking other tasks.
 * TaskKit.runInterval(new IntervalTaskListener() {
 *     @Override
 *     public void onUpdate() {
 *         log.info("Executing time-consuming I/O task, start");
 *
 *         try {
 *             TimeUnit.SECONDS.sleep(3);
 *         } catch (InterruptedException e) {
 *             throw new RuntimeException(e);
 *         }
 *
 *         log.info("Executing time-consuming I/O task, end");
 *     }
 *
 *     @Override
 *     public Executor getExecutor() {
 *         // Specify an executor to run the current callback (onUpdate method) to avoid blocking other tasks.
 *         return TaskKit.getCacheExecutor();
 *     }
 * }, 1, TimeUnit.SECONDS);
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-02
 */
@Slf4j
@UtilityClass
public class TaskKit {
    private Timer wheelTimer = new HashedWheelTimer();

    @Getter
    final ExecutorService cacheExecutor = ExecutorKit.newFixedThreadPool(RuntimeKit.availableProcessors2n, "netThread");
    @Getter
    final ExecutorService virtualExecutor = ExecutorKit.newVirtualExecutor("virtual");
    @Getter
    final ExecutorService netVirtualExecutor = ExecutorKit.newVirtualExecutor("netVirtual");
    final SetMultiMap<TickTimeUnit, IntervalTaskListener> intervalTaskListenerMap = SetMultiMap.of();

    record TickTimeUnit(long tick, TimeUnit timeUnit) {
    }

    /**
     * set HashedWheelTimer
     * <pre>{@code
     * TaskKit.setTimer(new HashedWheelTimer(17, TimeUnit.MILLISECONDS));
     * }
     * </pre>
     *
     * @param timer Timer
     * @since 21.23
     */
    public void setTimer(Timer timer) {
        Objects.requireNonNull(timer);

        var oldTimer = wheelTimer;
        wheelTimer = timer;
        oldTimer.stop();
    }

    /**
     * Submit a task to the fixed thread pool for execution.
     *
     * @param command the task to execute
     */
    public void execute(Runnable command) {
        cacheExecutor.execute(command);
    }

    /**
     * Submit a task to the virtual thread executor for execution.
     *
     * @param command the task to execute
     */
    public void executeVirtual(Runnable command) {
        virtualExecutor.execute(command);
    }

    /**
     * Returns a CompletableFuture where the task runs asynchronously on the virtualExecutor (virtual thread), and the result is obtained from the Supplier.
     *
     * @param supplier supplier
     * @param <U>      u
     * @return CompletableFuture
     * @see TaskKit#virtualExecutor
     */
    public <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(supplier, virtualExecutor);
    }

    /**
     * Executes the task after a certain delay;
     *
     * @param task  task
     * @param delay delay time
     * @param unit  delay time unit
     * @return Timeout
     */
    @SuppressWarnings("all")
    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
        return wheelTimer.newTimeout(task, delay, unit);
    }

    /**
     * Add OnceTaskListener callback, will only be executed once.
     *
     * @param taskListener taskListener
     * @param delay        delay time
     * @param unit         delay time unit
     */
    public void runOnce(OnceTaskListener taskListener, long delay, TimeUnit unit) {
        newTimeout(taskListener, delay, unit);
    }

    /**
     * Execute OnceTaskListener callback after one second, will only be executed once.
     *
     * @param taskListener taskListener
     */
    public void runOnceSecond(OnceTaskListener taskListener) {
        runOnce(taskListener, 1, TimeUnit.SECONDS);
    }

    /**
     * Add OnceTaskListener callback, will only be executed once.
     *
     * @param taskListener      taskListener
     * @param delayMilliseconds delayMilliseconds
     */
    public void runOnceMillis(OnceTaskListener taskListener, long delayMilliseconds) {
        runOnce(taskListener, delayMilliseconds, TimeUnit.MILLISECONDS);
    }

    /**
     * Add scheduled task listener
     *
     * @param taskListener scheduled task listener
     * @param tickMinute   The listener will be called once every tickMinute minutes.
     */
    public void runIntervalMinute(IntervalTaskListener taskListener, long tickMinute) {
        runInterval(taskListener, tickMinute, TimeUnit.MINUTES);
    }

    /**
     * Add task listener callback
     * <pre>
     * The task listener will be called once every tick time unit.
     * </pre>
     *
     * @param taskListener task listener
     * @param tick         tick time interval; the listener will be called once every tick time interval
     * @param timeUnit     tick time unit
     */
    public void runInterval(IntervalTaskListener taskListener, long tick, TimeUnit timeUnit) {
        TickTimeUnit tickTimeUnit = new TickTimeUnit(tick, timeUnit);

        Set<IntervalTaskListener> intervalTaskListeners = intervalTaskListenerMap.get(tickTimeUnit);

        if (CollKit.isEmpty(intervalTaskListeners)) {
            intervalTaskListeners = intervalTaskListenerMap.ofIfAbsent(tickTimeUnit, (initSet) -> {
                // Use HashedWheelTimer to simulate ScheduledExecutorService scheduling
                foreverTimerTask(tick, timeUnit, initSet);
            });
        }

        intervalTaskListeners.add(taskListener);
    }

    private void foreverTimerTask(long tick, TimeUnit timeUnit, Set<IntervalTaskListener> set) {
        TaskKit.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) {
                if (set.isEmpty()) {
                    TaskKit.newTimeout(this, tick, timeUnit);
                    return;
                }

                set.forEach(intervalTaskListener -> {
                    var executor = intervalTaskListener.getExecutor();
                    // If an executor is specified, the execution flow is placed in the executor, otherwise the current thread is used.
                    MoreKit.execute(executor, () -> executeFlowTimerListener(intervalTaskListener, set));
                });

                TaskKit.newTimeout(this, tick, timeUnit);
            }
        }, tick, timeUnit);
    }

    private void executeFlowTimerListener(IntervalTaskListener taskListener, Set<IntervalTaskListener> set) {
        try {
            // Remove inactive listeners
            if (!taskListener.isActive()) {
                set.remove(taskListener);
                return;
            }

            if (taskListener.triggerUpdate()) {
                taskListener.onUpdate();
            }
        } catch (Throwable e) {
            taskListener.onException(e);
        }
    }

    /**
     * Shut down all managed executors and the timer.
     */
    public void stop() {
        wheelTimer.stop();
        cacheExecutor.shutdown();
        virtualExecutor.shutdown();
        netVirtualExecutor.shutdown();
    }
}