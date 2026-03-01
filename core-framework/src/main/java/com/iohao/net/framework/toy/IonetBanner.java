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
package com.iohao.net.framework.toy;

import com.iohao.net.common.kit.*;
import com.iohao.net.common.kit.concurrent.*;
import com.iohao.net.common.kit.exception.*;
import com.iohao.net.framework.*;
import com.iohao.net.framework.i18n.*;
import java.io.*;
import java.lang.management.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import lombok.experimental.*;
import lombok.extern.slf4j.*;

import static java.lang.System.*;

/**
 * Startup banner renderer for the ionet framework.
 * <p>
 * Displays a randomly-colored ASCII art logo, server node summary, JVM information,
 * and optional breaking-news tips on the console. The banner cannot be disabled --
 * developers are expected to read it.
 *
 * @author 渔民小镇
 * @date 2023-01-30
 */
@Slf4j
@UtilityClass
public final class IonetBanner {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    final AtomicBoolean trigger = new AtomicBoolean(false);
    final AtomicInteger errorCount = new AtomicInteger(0);
    final Map<String, AtomicInteger> serverNodeMap = CollKit.ofConcurrentHashMap();
    final AtomicBoolean cleaned = new AtomicBoolean(false);

    volatile CountDownLatch countDownLatch;
    volatile Runnable callback = () -> {
        var serverAmountName = Bundle.getMessage(MessageKey.serverAmount);
        String externalTag = "ExternalServer";
        var externalCount = serverNodeMap.remove(externalTag);
        serverNodeMap.forEach((tag, count) -> println1("{%s:%s, tag:'%s'}".formatted(serverAmountName, count, tag)));

        if (externalCount != null) {
            println1("{%s:%s, tag:'%s'}".formatted(serverAmountName, externalCount, externalTag));
        }
    };

    public static void render() {
        if (trigger.get()) {
            return;
        }

        if (trigger.compareAndSet(false, true)) {
            renderBanner1();
        }
    }

    public void initCountDownLatch(int num) {
        countDownLatch = new CountDownLatch(num);
    }

    public void countDown() {
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }


    public void addTag(String tag) {
        if (cleaned.get()) {
            return;
        }

        AtomicInteger count = serverNodeMap.get(tag);
        if (count == null) {
            serverNodeMap.put(tag, new AtomicInteger(1));
            return;
        }

        count.getAndIncrement();
    }

    private void incErrorCount() {
        if (cleaned.get()) {
            return;
        }
        errorCount.getAndIncrement();
    }

    private final AtomicBoolean print = new AtomicBoolean();

    public void ofRuntimeException(String message) {
        if (!print.get()) {
            return;
        }

        incErrorCount();
        ThrowKit.ofRuntimeException(message);
        render();
    }

    public static void printLine() {
        out.println();
    }

    public static void println1(Object message) {
        out.println(message);
    }

    public static void printlnMsg(String message) {
        println1(message);
    }

    private void renderBanner1() {
        print.set(true);

        Runnable runnable = () -> {

            var startTime = new Date();
            extractedAwait();
            var endTime = new Date();
            var table = new ToyTable();

            if (callback != null) {
                callback.run();
            }

            // app
            var tableRegion = table.getRegion("ionet");
            tableRegion.putLine("pid", getPid());
            tableRegion.putLine("version", IonetVersion.VERSION);
            tableRegion.putLine("LICENSE ", "AGPL3.0");

            var internalMemory = new InternalMemory();
            var memory = table.getRegion("Memory");
            memory.putAll(internalMemory.getMemoryMap());

            extractedTime(table, startTime, endTime);

            extractedLogo();
            table.render();
            extractedLicense();

//            extractedAdv();
            // breaking news
//            extractedBreakingNews();

            extractedErrorCount();

            clean();

            IonetBanner.printLine();
        };

        TaskKit.executeVirtual(runnable);
    }

    private void extractedAwait() {
        try {
            if (Objects.nonNull(IonetBanner.countDownLatch)) {
                boolean r = IonetBanner.countDownLatch.await(5, TimeUnit.SECONDS);
                if (!r) {
                    IonetBanner.printlnMsg("countDownLatch await is false");
                }
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void clean() {
        cleaned.set(true);
        countDownLatch = null;
        callback = null;
        serverNodeMap.clear();
        separatorLine = null;
    }

    private void extractedTime(ToyTable table, Date startTime, Date endTime) {
        var consumeTime = (endTime.getTime() - startTime.getTime()) / 1000f;
        var consume = "%.2f s".formatted(consumeTime);

        ToyTableRegion other = table.getRegion("Time");
        other.putLine("start", TIME_FORMATTER.format(startTime.toInstant().atZone(ZoneId.systemDefault())));
        other.putLine("end", TIME_FORMATTER.format(endTime.toInstant().atZone(ZoneId.systemDefault())));
        other.putLine("consume", consume);
    }

    private void extractedBreakingNews() {
        var newsList = BreakingNewsKit.randomNewsList();
        for (News news : newsList) {
            System.out.printf("| News     | %s%n", news);
        }

        IonetBanner.printlnMsg("+----------+------------------------------------------------------------------------------------");
    }

    private void extractedAdv() {
        String s = BreakingNewsKit.randomAdv().toString();
        String builder = "| adv      | %s%n";
        System.out.printf(builder, s);
        IonetBanner.printlnMsg("+----------+------------------------------------------------------------------------------------");
    }

    private void extractedJavadocApi() {
        String s = BreakingNewsKit.randomMainNews().toString();
        String builder = "|          | %s%n";
        System.out.printf(builder, s);
        IonetBanner.printlnMsg("+----------+------------------------------------------------------------------------------------");
    }

    private void extractedErrorCount() {
        if (errorCount.get() == 0) {
            return;
        }

        String builder = "| Error    | error count : %s%n";
        System.out.printf(builder, errorCount.get());
        IonetBanner.printlnMsg(separatorLine);
    }

    private void extractedLogo() {
        // Use random banner and random coloring strategy for a less monotonous console output
        List<String> bannerList = new BannerData().listData();
        String banner = RandomKit.randomEle(bannerList);

        // coloring strategy
        var anyFunction = new BannerColorStrategy().anyColorFun();
        String anyBanner = anyFunction.apply(banner);

        IonetBanner.printLine();
        IonetBanner.printlnMsg(anyBanner);
    }

    private static String getPid() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName();

        try {
            return name.substring(0, name.indexOf('@'));
        } catch (Exception e) {
            return "-1";
        }
    }

    String separatorLine = "+----------+-----------------------------------------------------------------------";

    private void extractedLicense() {
        String builder = "| DOCUMENT | %s%n";
        System.out.printf(builder, "https://iohao.github.io/ionet");
        IonetBanner.printlnMsg(separatorLine);
    }
}
