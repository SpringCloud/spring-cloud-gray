package cn.springcloud.gray.decision.compare;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.List;

public class CollectionStringNotContainAnyComparator implements PredicateComparator<Collection<String>> {

    @Override
    public boolean test(Collection<String> src, Collection<String> another) {
        if (CollectionUtils.isEmpty(src)) {
            return false;
        }
        if (CollectionUtils.isEmpty(another)) {
            return false;
        }

        for (String v : another) {
            if (!src.contains(v)) {
                return true;
            }
        }
        return false;
    }
}
