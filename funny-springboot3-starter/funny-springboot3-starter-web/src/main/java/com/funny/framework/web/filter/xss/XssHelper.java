package com.funny.framework.web.filter.xss;

import java.util.regex.Pattern;

/**
 * Xss处理工具
 *
 * @author funny2048
 * @version 1.0
 * @date 2019-10-28
 */
public class XssHelper {

    private static String LEFT_TRIANGLE = "(<|%3C)";
    private static String RIGHT_TRIANGLE = "(>|%3E)";
    private static String END_TRIANGLE = "(\\s((?!" + RIGHT_TRIANGLE + ").)*)?" + RIGHT_TRIANGLE;

    private static Pattern[] patterns = new Pattern[]{
            // Script fragments
            createPattern(LEFT_TRIANGLE + "script" + END_TRIANGLE + "(.*)" + LEFT_TRIANGLE + "/script" + END_TRIANGLE),
            // lonely script tags
            createPattern(LEFT_TRIANGLE + "/?script" + END_TRIANGLE),
            // eval(...)
            // expression(...)
            createPattern("(eval|expression)\\([^)]*\\)"),
            // javascript:...
            // vbscript:...
            createPattern("(javascript|vbscript):"),
            // onload(...)=...
            createPattern("onload(\\([^)]*\\))?\\s*=")
    };

    private static Pattern createPattern(String regex) {
        // 忽略大小写、支持多行、包含换行符
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    }

    /**
     * 去掉XSS关键字
     *
     * @param values 原值列表
     * @return 处理后的值列表
     */
    public static String[] stripXss(String[] values) {
        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXss(values[i]);
        }

        return encodedValues;
    }

    /**
     * 去掉XSS关键字
     *
     * @param value 原值
     * @return 处理后的值
     */
    public static String stripXss(String value) {
        if (value == null) {
            return null;
        }
        // Avoid null characters
        String tmpValue = value.replaceAll("\0", "");

        // Remove all sections that match a pattern
        for (Pattern scriptPattern : patterns) {
            tmpValue = scriptPattern.matcher(tmpValue).replaceAll("");
        }
        return tmpValue;
    }

}
