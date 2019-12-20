package cn.springcloud.gray.plugin.refresher;

import org.apache.commons.lang3.StringUtils;

/**
 * @author saleson
 * @date 2019-12-20 22:31
 */
public class StringPrefixMatcher implements StringMatcher {

    private String prefix;

    public StringPrefixMatcher(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean matching(String str) {
        return StringUtils.startsWith(str, prefix);
    }
}
