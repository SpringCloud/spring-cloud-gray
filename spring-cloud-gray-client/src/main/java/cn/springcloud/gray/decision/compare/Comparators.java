package cn.springcloud.gray.decision.compare;

import java.util.HashMap;
import java.util.Map;

public class Comparators {


    private static Map<CompareMode, PredicateComparator> collectionStringComparators = new HashMap<>();

    static {
        collectionStringComparators.put(CompareMode.EQUAL, new CollectionStringEqualComparator());
        collectionStringComparators.put(CompareMode.UNEQUAL, new CollectionStringUnEqualComparator());
        collectionStringComparators.put(CompareMode.CONTAINS_ANY, new CollectionStringContainAnyComparator());
        collectionStringComparators.put(CompareMode.CONTAINS_ALL, new CollectionStringContainAllComparator());
        collectionStringComparators.put(CompareMode.NOT_CONTAINS_ALL, new CollectionStringNotContainAllComparator());
        collectionStringComparators.put(CompareMode.NOT_CONTAINS_ANY, new CollectionStringNotContainAnyComparator());
    }


    public static PredicateComparator getCollectionStringComparator(CompareMode mode) {
        return collectionStringComparators.get(mode);
    }
}
