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
package com.iohao.net.extension.codegen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import org.beetl.core.Template;
import org.junit.jupiter.api.Test;

/**
 * Verifies comment and string literal rendering used by code generation templates.
 *
 * @author 渔民小镇
 * @date 2026-07-19
 */
public class DocumentGenerateAboutTest {
    @Test
    public void multiLineCommentAddsPrefixAndSuffixToContinuationLines() {
        var function = new DocumentGenerateKit.MultiLineComment();

        assertEquals("line1\n* line2", function.call(new Object[]{"  line1  \r\nline2  ", "* "}, null));
        assertEquals("line1[br]\n## line2", function.call(new Object[]{"line1\nline2", "## ", "[br]"}, null));
        assertEquals("", function.call(new Object[]{"  \n  "}, null));
        assertEquals("", function.call(new Object[]{null, "* "}, null));
    }

    @Test
    public void oneLineCommentCollapsesLineBreaks() {
        var function = new DocumentGenerateKit.OneLineComment();

        assertEquals("line1 line2", function.call(new Object[]{"  line1  \r\n  line2  "}, null));
        assertEquals("", function.call(new Object[]{null}, null));
    }

    @Test
    public void stringLiteralEscapesConfiguredQuoteAndBackslash() {
        var function = new DocumentGenerateKit.StringLiteral();

        assertEquals("say \\\"hi\\\" \\\\path next", function.call(new Object[]{"say \"hi\" \\path\nnext", "double"}, null));
        assertEquals("it\\'s \\\\path next", function.call(new Object[]{"it's \\path\nnext", "single"}, null));
        assertEquals("", function.call(new Object[]{null, "double"}, null));
    }

    @Test
    public void broadcastTemplatesNormalizeDataDescription() throws IOException {
        for (String language : new String[]{"ts", "csharp", "gdscript"}) {
            String template = readTemplate("generate/%s/broadcast_action.txt".formatted(language));
            assertFalse(template.contains("${o.dataDescription"), language);
        }
    }

    @Test
    public void broadcastTemplatesRenderEscapedDescriptions() {
        for (String language : new String[]{"ts", "csharp", "gdscript"}) {
            Template template = DocumentGenerateKit.getTemplate("%s/broadcast_action.txt".formatted(language));
            template.binding("generateTime", "");
            template.binding("iohaoHome", "");
            template.binding("imports", "");
            template.binding("namespace", "Ionet.Gen");

            var broadcast = new HashMap<String, Object>();
            broadcast.put("cmdMethodName", "test");
            broadcast.put("cmd", 1);
            broadcast.put("subCmd", 2);
            broadcast.put("cmdMerge", 65538);
            broadcast.put("methodDescription", "method \"description\" and 'name'\nnext");
            broadcast.put("dataDescription", "data line 1\ndata line 2");
            broadcast.put("dataIsList", false);
            broadcast.put("bizDataType", "Value");
            broadcast.put("methodName", "Test");
            broadcast.put("exampleCode", "");
            broadcast.put("exampleCodeAction", null);
            template.binding("broadcastDocumentList", List.of(broadcast));

            String rendered = template.render();
            assertFalse(rendered.contains("data line 1\ndata line 2"), language);
            String escapedDescription = "ts".equals(language)
                    ? "method \"description\" and \\'name\\' next"
                    : "method \\\"description\\\" and 'name' next";
            assertTrue(rendered.contains(escapedDescription), language);
        }
    }

    private String readTemplate(String path) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            return new String(Objects.requireNonNull(inputStream).readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
