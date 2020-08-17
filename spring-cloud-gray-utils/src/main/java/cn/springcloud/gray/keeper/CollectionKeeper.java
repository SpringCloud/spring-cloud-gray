package cn.springcloud.gray.keeper;

import java.util.Collection;

/**
 * @author saleson
 * @date 2020-08-16 04:20
 */
public interface CollectionKeeper<T> {
    void add(T t);

    void remove(T t);

    int size();

    Collection<T> values();
}
