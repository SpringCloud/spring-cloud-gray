package cn.springcloud.gray.decision.compare;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;

import java.util.*;

public class CollectionStringEqualComparator implements PredicateComparator<Collection<String>> {

    @Override
    public boolean test(Collection<String> src, Collection<String> another) {
        if (CollectionUtils.isEmpty(src)) {
            return CollectionUtils.isEmpty(another) ? true : false;
        }
        if (CollectionUtils.isEmpty(another)) {
            return false;
        }

        List<String> srcSorted = new ArrayList<>(src);
        Collections.sort(srcSorted);
        List<String> anotherSorted = new ArrayList<>(another);
        Collections.sort(anotherSorted);

//        return CollectionUtils.isEqualCollection();
        return ListUtils.isEqualList(srcSorted, anotherSorted);
    }
}
