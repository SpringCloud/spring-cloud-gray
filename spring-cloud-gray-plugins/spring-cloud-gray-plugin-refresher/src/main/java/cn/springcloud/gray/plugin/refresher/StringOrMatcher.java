package cn.springcloud.gray.plugin.refresher;

/**
 * @author saleson
 * @date 2019-12-20 22:39
 */
public class StringOrMatcher implements StringMatcher {

    private StringMatcher[] stringMatchers;

    public StringOrMatcher(StringMatcher... stringMatchers) {
        this.stringMatchers = stringMatchers;
    }

    @Override
    public boolean matching(String str) {
        for (StringMatcher stringMatcher : stringMatchers) {
            if (stringMatcher.matching(str)) {
                return true;
            }
        }
        return false;
    }
}
