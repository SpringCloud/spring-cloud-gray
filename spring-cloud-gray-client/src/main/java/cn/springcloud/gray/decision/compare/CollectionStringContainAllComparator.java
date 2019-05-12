package cn.springcloud.gray.decision.compare;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.List;

public class CollectionStringContainAllComparator implements PredicateComparator<Collection<String>> {

    @Override
    public boolean test(Collection<String> src, Collection<String> another) {
        if (CollectionUtils.isEmpty(src)) {
            return false;
        }
        if (CollectionUtils.isEmpty(another)) {
            return false;
        }

        return src.containsAll(another);
    }
}
