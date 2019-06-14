package cn.springcloud.gray.decision.compare;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Comparators {


    private static Map<CompareMode, PredicateComparator<Collection<String>>> collectionStringComparators = new HashMap<>();
    private static Map<CompareMode, PredicateComparator<String>> stringStringComparators = new HashMap<>();

    static {
        collectionStringComparators.put(CompareMode.EQUAL, new CollectionStringEqualComparator());
        collectionStringComparators.put(CompareMode.UNEQUAL, new CollectionStringUnEqualComparator());
        collectionStringComparators.put(CompareMode.CONTAINS_ANY, new CollectionStringContainAnyComparator());
        collectionStringComparators.put(CompareMode.CONTAINS_ALL, new CollectionStringContainAllComparator());
        collectionStringComparators.put(CompareMode.NOT_CONTAINS_ALL, new CollectionStringNotContainAllComparator());
        collectionStringComparators.put(CompareMode.NOT_CONTAINS_ANY, new CollectionStringNotContainAnyComparator());

        stringStringComparators.put(CompareMode.EQUAL, (arg1, arg2) -> StringUtils.equals(arg1, arg2));
        stringStringComparators.put(CompareMode.UNEQUAL, (arg1, arg2) -> !StringUtils.equals(arg1, arg2));
    }


    public static PredicateComparator<Collection<String>> getCollectionStringComparator(CompareMode mode) {
        return collectionStringComparators.get(mode);
    }


    public static PredicateComparator<String> getStringComparator(CompareMode mode) {
        return stringStringComparators.get(mode);
    }
}
