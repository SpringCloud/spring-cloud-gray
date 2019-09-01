package cn.springcloud.gray.decision.compare;

import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        stringStringComparators.put(CompareMode.LT, Comparators::lt);
        stringStringComparators.put(CompareMode.LTE, Comparators::lte);
        stringStringComparators.put(CompareMode.GT, Comparators::gt);
        stringStringComparators.put(CompareMode.GTE, Comparators::gte);
    }


    public static PredicateComparator<Collection<String>> getCollectionStringComparator(CompareMode mode) {
        return collectionStringComparators.get(mode);
    }


    public static PredicateComparator<String> getStringComparator(CompareMode mode) {
        return stringStringComparators.get(mode);
    }


    public static <C extends Comparable> boolean equals(C arg1, C arg2){
        return compare(arg1, arg2)==0;
    }

    public static <C extends Comparable> boolean unEquals(C arg1, C arg2){
        return compare(arg1, arg2)!=0;
    }

    public static <C extends Comparable> boolean lt(C arg1, C arg2){
        if(Objects.isNull(arg1) || Objects.isNull(arg2)){
            return false;
        }
        return compare(arg1, arg2)>0;
    }

    public static <C extends Comparable> boolean lte(C arg1, C arg2){
        if(Objects.isNull(arg1) || Objects.isNull(arg2)){
            return false;
        }
        return compare(arg1, arg2)>-1;
    }

    public static <C extends Comparable> boolean gt(C arg1, C arg2){
        if(Objects.isNull(arg1) || Objects.isNull(arg2)){
            return false;
        }
        return compare(arg2, arg1)>0;
    }

    public static <C extends Comparable> boolean gte(C arg1, C arg2){
        if(Objects.isNull(arg1) || Objects.isNull(arg2)){
            return false;
        }
        return compare(arg2, arg1)>-1;
    }


    public static <C extends Comparable> int compare(C arg1, C arg2) {
        if (arg1 == null) {
            return arg2 == null ? 0 : -1;
        }else if(arg2==null){
            return 1;
        }

        return arg1.compareTo(arg2);
    }





}
