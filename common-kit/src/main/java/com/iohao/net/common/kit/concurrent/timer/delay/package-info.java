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
/**
 * Utility tools - <a href="https://iohao.github.io/ionet/docs/kit/delay_task">Lightweight and controllable delayed tasks</a>. Tasks will execute when the specified time is reached, can be cancelled, can have their delay time increased or decreased, can be overwritten, and can have task listener callbacks set.
 * <p>
 * Lightweight and Controllable Delayed Tasks - Introduction
 * <pre>
 * As we know, in {@link com.iohao.net.common.kit.concurrent.TaskKit}, a utility module combining task, time, delay listener, timeout listener, etc., is provided, allowing for the execution of some delayed tasks via runOnce;
 * However, sometimes we need some controllable delayed tasks, meaning the delay time can change based on subsequent business logic, such as increasing or decreasing the delay time, cancelling the task, and other controllable operations.
 * </pre>
 * Lightweight and Controllable Delayed Tasks - Features
 * <pre>
 * 1. Single Responsibility Principle
 * 2. Tasks execute when the specified time is reached
 * 3. Tasks can be cancelled
 * 4. Tasks can be overwritten
 * 5. Tasks can have their delay time increased or decreased
 * 6. Task listener callbacks can be set
 * 7. Internally uses Netty HashedWheelTimer, easily supporting millions of tasks.
 * </pre>
 * for example
 * <pre>{@code
 * public class DelayTaskTest {
 *     @Test
 *     public void runDelayTask() {
 *         // ---------------Example - Delayed Task---------------
 *         // Execute the delayed task after 1 second
 *         DelayTaskKit.of(() -> {
 *             log.info("Delayed task executed after 1 second");
 *         })
 *         .plusTime(Duration.ofSeconds(1)) // Increase delay by 1 second
 *         .task(); // Start the task
 *     }
 *
 *     @Test
 *     public void plusDelayTime() {
 *         // ---------------Example - Increase Delay Time---------------
 *         long timeMillis = System.currentTimeMillis();
 *
 *         DelayTask delayTask = DelayTaskKit.of(() -> {
 *             long value = System.currentTimeMillis() - timeMillis;
 *             log.info("Increased delay time, finally executed after {} ms", value);
 *             // Assert.assertTrue(value > 1490);
 *         })
 *         .plusTime(Duration.ofSeconds(1)) // Increase delay by 1 second
 *         .task(); // Start the task
 *
 *         delayTask.plusTimeMillis(500); // Increase delay by 0.5 seconds
 *
 *         // Finally executed after 1.5 seconds
 *     }
 *
 *     @Test
 *     public void minusDelayTime() {
 *         // ---------------Example - Decrease Delay Time---------------
 *         long timeMillis = System.currentTimeMillis();
 *
 *         // Delayed task executed after 1 second
 *         DelayTask delayTask = DelayTaskKit.of(() -> {
 *             long value = System.currentTimeMillis() - timeMillis;
 *             log.info("Decreased delay time, finally executed after {} ms", value);
 *             // Assert.assertTrue(value < 510);
 *         })
 *         .plusTime(Duration.ofSeconds(1)) // Increase delay by 1 second
 *         .task(); // Start the task
 *
 *         delayTask.minusTimeMillis(500); // Decrease delay time by 0.5 seconds
 *
 *         // Finally executed after 0.5 seconds
 *     }
 *
 *     @Test
 *     public void coverDelayTask() throws InterruptedException {
 *         // ---------------Example - Overwrite Delayed Task---------------
 *
 *         String taskId = "1";
 *
 *         DelayTaskKit.of(taskId, () -> log.info("Execute task - 1"))
 *             .plusTime(Duration.ofSeconds(2)) // Increase delay by 2 seconds
 *             .task(); // Start the task
 *
 *         TimeUnit.MILLISECONDS.sleep(500);
 *
 *         long timeMillis = System.currentTimeMillis();
 *
 *         // Since the taskId is the same, the previous delayed task will be overwritten
 *         DelayTask delayTask = DelayTaskKit.of(taskId, () -> {
 *             long value = System.currentTimeMillis() - timeMillis;
 *             log.info("Execute task - 2, finally executed after {} ms", value);
 *             // Assert.assertTrue(value > 990);
 *         })
 *         .plusTime(Duration.ofSeconds(1)) // Increase delay by 1 second
 *         .task(); // Start the task
 *     }
 *
 *     @Test
 *     public void cancelDelayTask() throws InterruptedException {
 *         // ---------------Example - Cancel Delayed Task, using DelayTask for cancellation---------------
 *
 *         DelayTask delayTask = DelayTaskKit.of(() -> {
 *             log.info("Cancel - Delayed Task");
 *         })
 *         .plusTime(Duration.ofSeconds(2)) // Increase delay by 2 seconds
 *         .task(); // Start the task
 *
 *         log.info("After 0.5 seconds, the scheduled task is no longer needed because a certain business condition is met");
 *         TimeUnit.MILLISECONDS.sleep(500);
 *
 *         delayTask.isActive(); // true
 *         delayTask.cancel(); // Cancel the task
 *         delayTask.isActive(); // false
 *
 *         // -----------Example - Cancel Delayed Task, using taskId for cancellation-----------
 *
 *         String taskId = "1";
 *         // Set taskId when creating the delayed task
 *         DelayTaskKit.of(taskId, () -> log.info("Cancel via taskId - Delayed Task"))
 *             .plusTime(Duration.ofSeconds(1)) // Increase delay by 1 second
 *             .task(); // Start the task
 *
 *         log.info("After 0.5 seconds, the scheduled task is no longer needed because a certain business condition is met");
 *         TimeUnit.MILLISECONDS.sleep(500);
 *         DelayTaskKit.cancel(taskId); // Cancel the task via taskId
 *     }
 *
 *     @Test
 *     public void optionalDelayTask() {
 *         // ---------------Example - Look up Delayed Task---------------
 *
 *         String newTaskId = "1";
 *         DelayTaskKit.of(newTaskId, () -> log.info("hello DelayTask"))
 *             // Execute delayed task after 2.5 seconds. (Demonstrates two ways to add delay time here)
 *             .plusTime(Duration.ofSeconds(1)) // Increase delay by 1 second
 *             .plusTime(Duration.ofMillis(1000)) // Increase delay by 1 second
 *             .plusTimeMillis(500) // Increase delay by 0.5 seconds
 *             .task(); // Start the task
 *
 *         // In subsequent business logic, the delayed task can be looked up using the taskId
 *         Optional<DelayTask> optionalDelayTask = DelayTaskKit.optional(newTaskId);
 *         if (optionalDelayTask.isPresent()) {
 *             DelayTask delayTask = optionalDelayTask.get();
 *             log.info("{}", delayTask);
 *         }
 *
 *         // Look up the delayed task by taskId, and if present, execute the given logic
 *         DelayTaskKit.ifPresent(newTaskId, delayTask -> {
 *             delayTask.plusTimeMillis(500); // Increase delay time by 0.5 seconds
 *         });
 *     }
 *
 *     @Test
 *     public void customTaskListener() {
 *         // ---------------Example - Enhanced TaskListener ---------------
 *
 *         DelayTaskKit.of(new TaskListener() {
 *             @Override
 *             public void onUpdate() {
 *                 log.info("Delayed task executed after 1.7 seconds");
 *             }
 *
 *             @Override
 *             public boolean triggerUpdate() {
 *                 // Whether to trigger the onUpdate listener callback method
 *                 return TaskListener.super.triggerUpdate();
 *             }
 *
 *             @Override
 *             public Executor getExecutor() {
 *                 // Specify an executor to consume the current onUpdate
 *                 // Executors yourExecutors = ...; // Note: This line requires a valid Executor instance
 *                 return null; // For example, return null or a valid Executor
 *             }
 *
 *             @Override
 *             public void onException(Throwable e) {
 *                 // Exception callback
 *                 TaskListener.super.onException(e);
 *             }
 *         })
 *         .plusTime(Duration.ofMillis(1700))
 *         .task();
 *     }
 * }
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-09-01
 * @since 21.16
 */
package com.iohao.net.common.kit.concurrent.timer.delay;