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

import com.iohao.net.common.kit.RandomKit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Provides ASCII art banner data for the ionet startup display.
 *
 * @author 渔民小镇
 * @date 2023-01-30
 */
final class BannerData {

    List<String> listData() {
        List<String> bannerList = new ArrayList<>();

        // Georgia11
        String banner = """
                   ,,
                   db                                   mm
                                                        MM
                 `7MM   ,pW"Wq.  `7MMpMMMb.   .gP"Ya  mmMMmm
                   MM  6W'   `Wb   MM    MM  ,M'   Yb   MM
                   MM  8M     M8   MM    MM  8M""\"""\"   MM
                   MM  YA.   ,A9   MM    MM  YM.    ,   MM
                 .JMML. `Ybmd9'  .JMML  JMML. `Mbmmd'   `Mbmo
                """;

        bannerList.add(banner);


        return bannerList;
    }
}

/**
 * Randomly selects an ANSI coloring strategy for the startup banner text.
 *
 * @author 渔民小镇
 * @date 2023-01-30
 */
final class BannerColorStrategy {

    static AnsiColor.Color anyColor() {
        List<AnsiColor.Color> collect = Stream.of(AnsiColor.Color.values())
                // exclude black, white, blue, and default
                .filter(color -> color != AnsiColor.Color.BLUE)
                .filter(color -> color != AnsiColor.Color.WHITE)
                .filter(color -> color != AnsiColor.Color.BLACK)
                .filter(color -> color != AnsiColor.Color.DEFAULT)
                .toList();

        return RandomKit.randomEle(collect);
    }

    UnaryOperator<String> anyColorFun() {
        // coloring strategy
        UnaryOperator<String> colorNon = s -> s;
        UnaryOperator<String> colorSingleFun = this::colorSingle;
        UnaryOperator<String> colorRandomLineFun = this::colorRandomLine;
        UnaryOperator<String> colorRandomFun = this::colorRandom;
        UnaryOperator<String> colorRandomColumnFun = this::colorRandomColumn;
        UnaryOperator<String> colorColumnFun = this::colorColumn;

        List<UnaryOperator<String>> functionList = new ArrayList<>();
        // 单色
//        functionList.add(colorNon);
//        functionList.add(colorSingleFun);

        // 按列随机上色
//        functionList.add(colorRandomColumnFun);
        // 随机列上色
//        functionList.add(colorColumnFun);
//        functionList.add(colorColumnFun);
        // 随机行上色
//        functionList.add(colorRandomLineFun);
//        functionList.add(colorRandomLineFun);
//        functionList.add(colorRandomLineFun);
        // 随机上色
        functionList.add(colorRandomFun);
        functionList.add(colorRandomFun);
        functionList.add(colorRandomFun);

        // 随机得到一个上色策略
        return RandomKit.randomEle(functionList);
    }

    private String colorRandomLine(String banner) {
        // 按行来上色
        List<AnsiColor.Color> colorList = listColor();

        var ansi = AnsiColor.builder();

        char[] array = banner.toCharArray();

        int anInt = RandomKit.randomInt(colorList.size());
        AnsiColor.Color color = colorList.get(anInt);

        for (char c : array) {
            ansi.fg(color).a(c);

            if (c == '\n') {
                color = RandomKit.randomEle(colorList);
            }
        }

        return ansi.reset().toString();
    }

    private String colorSingle(String banner) {
        // 上单色
        AnsiColor.Color color = randomColor();
        var ansi = AnsiColor.builder().fg(color).a(banner);
        return ansi.reset().toString();
    }

    private String colorRandom(String banner) {
        // 随机字符上色

        List<AnsiColor.Color> colorList = listColor();

        var ansi = AnsiColor.builder();
        char[] array = banner.toCharArray();

        for (char c : array) {
            AnsiColor.Color color = RandomKit.randomEle(colorList);
            ansi.fg(color).a(c);
        }

        return ansi.reset().toString();
    }

    private String colorRandomColumn(String banner) {
        // 达到换行的字符数量
        int widthLen = RandomKit.randomInt(1, 10);
        AnsiColor.Color color = randomColor();

        var ansi = AnsiColor.builder();

        char[] array = banner.toCharArray();
        for (int i = 0; i < array.length; i++) {
            char c = array[i];

            if (i % widthLen == 0) {
                // 换色
                color = randomColor();
                widthLen = RandomKit.randomInt(1, 10);
            }

            ansi.fg(color).a(c);
        }

        return ansi.reset().toString();
    }

    private String colorColumn(String banner) {
        var ansi = AnsiColor.builder();

        TheColorColumn colorColumn = TheColorColumn.create();
        List<TheColorColumn> list = new ArrayList<>();
        list.add(colorColumn);

        int lineNum = 0;

        for (char c : banner.toCharArray()) {

            if (c == '\n') {
                lineNum = 0;
                colorColumn = list.get(lineNum);
                colorColumn.reset();
            }

            if (!colorColumn.has()) {
                lineNum++;
                // 取下一个数据

                if (lineNum >= list.size()) {
                    // 增加一个颜色数据
                    colorColumn = TheColorColumn.create();
                    list.add(colorColumn);
                }

                colorColumn = list.get(lineNum);
                colorColumn.reset();
            }

            colorColumn.render(ansi, c);


        }

        return ansi.reset().toString();
    }

    private static class TheColorColumn {
        AnsiColor.Color color;
        int widthLen;
        int num;

        TheColorColumn(AnsiColor.Color color, int widthLen) {
            this.color = color;
            this.widthLen = widthLen;
            this.num = widthLen;
        }

        void reset() {
            this.num = widthLen;
        }

        static TheColorColumn create() {
            int widthLen = RandomKit.randomInt(1, 5);
            AnsiColor.Color color = randomColor();
            return new TheColorColumn(color, widthLen);
        }

        void render(AnsiColor.Builder ansi, char c) {
            this.num--;
            ansi.fg(color).a(c);
        }

        boolean has() {
            return this.num > 0;
        }
    }

    private List<AnsiColor.Color> listColor() {
        return Stream.of(AnsiColor.Color.values())
                // exclude black
                .filter(color -> color != AnsiColor.Color.BLACK)
                .toList();
    }

    private static AnsiColor.Color randomColor() {
        List<AnsiColor.Color> collect = Stream.of(AnsiColor.Color.values())
                // exclude black
                .filter(color -> color != AnsiColor.Color.BLACK)
                .toList();

        return RandomKit.randomEle(collect);
    }
}


/**
 * Provides JVM memory usage statistics (used, free, total) formatted as human-readable strings.
 *
 * @author 渔民小镇
 * @date 2023-01-30
 */
final class InternalMemory {

    Map<String, String> getMemoryMap() {
        Runtime run = Runtime.getRuntime();

        long totalMemory = run.totalMemory();
        long freeMemory = run.freeMemory();
        long used = totalMemory - freeMemory;

        Map<String, String> map = new LinkedHashMap<>(3);
        map.put("used", format(used));
        map.put("freeMemory", format(freeMemory));
        map.put("totalMemory", format(totalMemory));

        return map;
    }

    private String format(long value) {

        var gb = 1024L * 1024L * 1024L;
        if (value >= gb) {
            return (Math.round((double) value / gb * 100.0D) / 100.0D) + "GB";
        }

        var mb = 1024L * 1024L;
        if (value >= mb) {
            return (Math.round((double) value / mb * 100.0D) / 100.0D) + "MB";
        }

        var kb = 1024L;
        if (value >= kb) {
            return (Math.round((double) value / kb * 100.0D) / 100.0D) + "KB";
        }

        return "";
    }
}
