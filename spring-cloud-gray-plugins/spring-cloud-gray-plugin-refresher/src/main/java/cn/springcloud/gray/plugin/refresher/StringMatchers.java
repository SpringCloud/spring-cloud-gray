package cn.springcloud.gray.plugin.refresher;

/**
 * @author saleson
 * @date 2019-12-20 22:32
 */
public class StringMatchers {

    public static StringMatcher prefixMatcher(String prefix) {
        return new StringPrefixMatcher(prefix);
    }

    public static StringMatcher prefixsAnyMatcher(String... prefixs) {
        return new StringPrefixsAnyMatcher(prefixs);
    }
}
