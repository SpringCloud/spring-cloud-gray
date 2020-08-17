package cn.springcloud.gray;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saleson
 * @date 2020-05-20 23:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pair<K, V> {
    private K key;
    private V value;
}
