package com.iohao.net.common.kit;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 *
 * @author 渔民小镇
 * @date 2025-09-23
 * @since 25.1
 */
public class StrKitTest {

    @Test
    public void format1() {
        String template = """
                // {classComment}
                {classOrEnum} {className} {
                {fieldsString}
                }
                """;

        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("className", "AnimalTypeEnum");
        messageMap.put("fieldsString", """
                  // the cat
                  cat = 0;
                  // the tiger
                  tiger = 10;
                
                """);
        messageMap.put("classComment", "TestAnimalTypeEnum");
        messageMap.put("classOrEnum", "enum");

        String str = format1(template, messageMap);
        System.out.println(str);
    }

    public String format1(@NonNull String template, @NonNull Map<?, ?> map) {
        if (template.isEmpty() || map.isEmpty()) {
            return template;
        }

        String templateValue = template;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            var value = entry.getValue();
            if (value == null) {
                continue;
            }

            templateValue = replace(templateValue, "{" + entry.getKey() + "}", value.toString());
        }

        return templateValue;
    }

    private String replace(String str, String searchStr, String replacement) {
        if (StrKit.isEmpty(str) || StrKit.isEmpty(searchStr)) {
            return str;
        }

        if (replacement == null) {
            replacement = "";
        }

        int fromIndex = 0;
        final int strLength = str.length();
        final int searchStrLength = searchStr.length();
        if (strLength < searchStrLength) {
            return str;
        }

        final StringBuilder result = new StringBuilder(strLength - searchStrLength + replacement.length());

        int preIndex = fromIndex;
        int index;
        while ((index = indexOf(str, searchStr, preIndex)) > -1) {
            result.append(str, preIndex, index);
            result.append(replacement);
            preIndex = index + searchStrLength;
        }

        if (preIndex < strLength) {
            result.append(str, preIndex, strLength);
        }

        return result.toString();
    }

    private int indexOf(String text, String searchStr, int from) {
        if (StrKit.isEmpty(text) || StrKit.isEmpty(searchStr)) {
            return Objects.equals(text, searchStr) ? 0 : -1;
        }

        return new StringFinder(searchStr, text).start(from);
    }

    private static class StringFinder {
        String text;
        int endIndex = -1;
        boolean negative;
        final String strToFind;

        StringFinder(String strToFind, String text) {
            this.strToFind = strToFind;
            this.text = text;
        }

        int getValidEndIndex() {
            if (negative && endIndex == -1) {
                return -1;
            }
            return (endIndex < 0) ? endIndex + text.length() + 1 : Math.min(endIndex, text.length());
        }

        int start(int from) {
            final int subLen = strToFind.length();
            if (from < 0) {
                from = 0;
            }
            int endLimit = getValidEndIndex();
            if (negative) {
                for (int i = from; i > endLimit; i--) {
                    if (isSubEquals(text, i, strToFind, subLen)) {
                        return i;
                    }
                }
            } else {
                endLimit = endLimit - subLen + 1;
                for (int i = from; i < endLimit; i++) {
                    if (isSubEquals(text, i, strToFind, subLen)) {
                        return i;
                    }
                }
            }

            return -1;
        }

        boolean isSubEquals(String str1, int start1, String str2, int length) {
            return str1 != null && str2 != null && str1.regionMatches(false, start1, str2, 0, length);
        }
    }
}