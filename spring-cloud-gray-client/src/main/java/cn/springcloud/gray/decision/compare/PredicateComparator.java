package cn.springcloud.gray.decision.compare;

public interface PredicateComparator<T> {

    boolean test(T src, T another);
}
