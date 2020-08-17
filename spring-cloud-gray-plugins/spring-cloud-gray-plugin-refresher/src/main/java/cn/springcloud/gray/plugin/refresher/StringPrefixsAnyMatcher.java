package cn.springcloud.gray.plugin.refresher;

import org.apache.commons.lang3.StringUtils;

/**
 * @author saleson
 * @date 2019-12-20 22:41
 */
public class StringPrefixsAnyMatcher implements StringMatcher {

    private String[] prefixs;

    public StringPrefixsAnyMatcher(String[] prefixs) {
        this.prefixs = prefixs;
    }

    @Override
    public boolean matching(String str) {
        for (String prefix : prefixs) {
            if (StringUtils.startsWith(str, prefix)) {
                return true;
            }
        }
        return false;
    }
}
