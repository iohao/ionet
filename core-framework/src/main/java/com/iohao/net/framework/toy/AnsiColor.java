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

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lightweight ANSI escape code utility, replacing the external jansi library.
 *
 * @author 渔民小镇
 * @date 2026-02-27
 */
@UtilityClass
public class AnsiColor {

    private final String ESC = "\033[";
    private final String RESET = ESC + "0m";
    private final Pattern RENDER_PATTERN = Pattern.compile("@\\|([\\w,]+)\\s(.*?)\\s?\\|@");

    enum Color {
        BLACK(30), RED(31), GREEN(32), YELLOW(33), BLUE(34), MAGENTA(35), CYAN(36), WHITE(37), DEFAULT(39);

        final int code;

        Color(int code) {
            this.code = code;
        }

        static List<Color> list() {
            return List.of(values());
        }
    }

    String fg(Color color, String text) {
        return ESC + color.code + "m" + text + RESET;
    }

    String fg(Color color, char c) {
        return ESC + color.code + "m" + c;
    }

    String reset() {
        return RESET;
    }

    /**
     * Parses jansi-style {@code @|COLOR text |@} markup and replaces it with ANSI escape codes.
     */
    public String render(String text) {
        var matcher = RENDER_PATTERN.matcher(text);
        var sb = new StringBuilder();

        while (matcher.find()) {
            var colorName = matcher.group(1).toUpperCase();
            var content = matcher.group(2);

            Color color;
            try {
                color = Color.valueOf(colorName);
            } catch (IllegalArgumentException _) {
                color = Color.DEFAULT;
            }

            matcher.appendReplacement(sb, Matcher.quoteReplacement(fg(color, content)));
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Chainable builder for character-by-character coloring.
     */
    Builder builder() {
        return new Builder();
    }

    static final class Builder {
        private final StringBuilder sb = new StringBuilder();

        Builder fg(Color color) {
            sb.append(ESC).append(color.code).append('m');
            return this;
        }

        Builder a(char c) {
            sb.append(c);
            return this;
        }

        Builder a(String text) {
            sb.append(text);
            return this;
        }

        Builder reset() {
            sb.append(RESET);
            return this;
        }

        @Override
        public String toString() {
            return sb.toString();
        }
    }
}
