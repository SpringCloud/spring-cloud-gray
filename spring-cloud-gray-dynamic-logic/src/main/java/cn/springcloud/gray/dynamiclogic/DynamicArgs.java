package cn.springcloud.gray.dynamiclogic;

import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author saleson
 * @date 2019-12-28 18:20
 */
@EqualsAndHashCode
public class DynamicArgs<K, V> {

    private Map<K, V> map = new HashMap<>();

    public void set(K k, V v) {
        map.put(k, v);
    }

    public V get(K k) {
        return map.get(k);
    }

    public V remove(K k) {
        return map.remove(k);
    }


    public String toString() {
        return map.toString();
    }
}
