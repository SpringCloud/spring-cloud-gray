package cn.springcloud.gray;

import java.util.Map;
import java.util.function.Function;

public interface Cache<K, V> {

    V getIfPresent(K key);


    V get(K key, Function<? super K, ? extends V> mappingFunction);

    Map<K, V> getAllPresent(Iterable<K> keys);


    void put(K key, V value);


    void putAll(Map<? extends K, ? extends V> map);


    void invalidate(Object key);

    void invalidateAll(Iterable<?> keys);

    void invalidateAll();
}
