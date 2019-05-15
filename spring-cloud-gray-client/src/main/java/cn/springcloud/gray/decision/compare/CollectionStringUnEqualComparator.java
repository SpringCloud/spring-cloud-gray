package cn.springcloud.gray.decision.compare;

import org.apache.commons.collections.ListUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CollectionStringUnEqualComparator implements PredicateComparator<Collection<String>> {

    @Override
    public boolean test(Collection<String> src, Collection<String> another) {
        if (CollectionUtils.isEmpty(src)) {
            return CollectionUtils.isEmpty(another) ? false : true;
        }
        if (CollectionUtils.isEmpty(another)) {
            return true;
        }

        return !ListUtils.isEqualList(src, another);
    }
}
