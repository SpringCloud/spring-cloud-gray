package cn.springcloud.gray.utils;

import org.apache.commons.collections.SetUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author saleson
 * @date 2020-05-21 00:01
 */
public class StringUtils {


    /**
     * 将str字符串中的占位符(${key})替换成map中对应的值。 <br>
     * 占位符的开始字符为"${" <br>
     * 占位符的结束字符为"}"
     *
     * @param str
     * @param map
     * @return
     */
    public static String replacAll(String str, Map<String, String> map) {
        return replacAll(str, map, "${", "}", null);
    }

    /**
     * 将str字符串中的占位符(${key})替换成map中对应的值。 <br>
     * 占位符的开始字符为"${" <br>
     * 占位符的结束字符为"}" <br>
     * 如果map中获取的为null，value取默认字符。如果默认字符为null，不替换占位符
     *
     * @param str
     * @param map
     * @param defaultValue
     * @return
     */
    public static String replacAll(String str, Map<String, String> map, String defaultValue) {
        return replacAll(str, map, "${", "}", defaultValue);
    }

    /**
     * 将str字符串中的占位符替换成map中对应的值。 <br>
     * 指定占位符的开始字符和结束字符。 <br>
     * 如果map中获取的为null，value取默认字符。如果默认字符为null，不替换占位符
     *
     * @param str
     * @param map
     * @param startStr     占位符的开始字符
     * @param endStr       占位符的结束字符
     * @param defaultValue
     * @return
     */
    public static String replacAll(String str, Map<String, String> map, String startStr, String endStr,
                                   String defaultValue) {
        if (isEmpty(str) || map == null || map.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        int start = -1;
        int end = -1;
        int len = 0;
        String key = "";
        while (true) {
            start = str.indexOf(startStr, len);
            if (start == -1) {
                sb.append(str.substring(len));
                break;
            }
            end = str.indexOf(endStr, start);
            if (end == -1) {
                sb.append(str.substring(len));
                break;
            }
            key = str.substring(start + startStr.length(), end);
            String value = map.get(key);
            if (value == null) {
                if (defaultValue == null) {
                    value = startStr + key + endStr;
                } else {
                    value = defaultValue;
                }
            }
            sb.append(str.substring(len, start)).append(value);
            len = end + 1;
        }
        return sb.toString();
    }

    /**
     * 提取字符串中的占位符。 <br>
     * 占位符的开始字符为"${" <br>
     * 占位符的结束字符为"}"
     *
     * @param str
     * @return
     */
    public static Set<String> extractPlaceholder(String str) {
        return extractPlaceholder(str, "${", "}");
    }

    /**
     * 提取字符串中的占位符。 指定占位符的开始字符和结束字符。
     *
     * @param str
     * @param startStr 占位符的开始字符
     * @param endStr   占位符的结束字符
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Set<String> extractPlaceholder(String str, String startStr, String endStr) {
        if (isEmpty(str)) {
            return SetUtils.EMPTY_SET;
        }
        Set<String> set = new HashSet<>();
        int start = -1;
        int end = 0;
        String key = "";
        while (true) {
            start = str.indexOf(startStr, end);
            if (start == -1) {
                break;
            }
            end = str.indexOf(endStr, start);
            if (end == -1) {
                break;
            }
            key = str.substring(start + startStr.length(), end);
            set.add(key);
        }
        return set;
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }
}
