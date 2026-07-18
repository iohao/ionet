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
package com.iohao.net.extension.protobuf;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Verifies proto comment rendering, including multi-line comment support.
 *
 * @author 渔民小镇
 * @date 2026-07-18
 */
public class ProtoCommentTest {
    @Test
    public void nullOrBlankReturnsEmpty() {
        assertEquals("", ProtoJava.toProtoComment(null, ""));
        assertEquals("", ProtoJava.toProtoComment("", "  "));
        assertEquals("", ProtoJava.toProtoComment("   \n  ", "  "));
    }

    @Test
    public void singleLineComment() {
        assertEquals("// hello\n", ProtoJava.toProtoComment("hello", ""));
        assertEquals("  // hello\n", ProtoJava.toProtoComment("hello", "  "));
    }

    @Test
    public void multiLineComment() {
        // Every line must carry the // prefix, otherwise the generated proto is invalid.
        assertEquals("// line1\n// line2\n", ProtoJava.toProtoComment("line1\nline2", ""));
        assertEquals("  // line1\n  // line2\n", ProtoJava.toProtoComment("line1\nline2", "  "));
    }

    @Test
    public void multiLineCommentDropsBlankLinesAndStrips() {
        assertEquals("// line1\n// line2\n", ProtoJava.toProtoComment("  line1  \n\n  line2  ", ""));
    }
}
