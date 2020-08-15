package cn.springcloud.gray.keeper;

import java.util.List;

/**
 * @author saleson
 * @date 2020-08-16 04:22
 */
public interface ListKeeper<T> extends CollectionKeeper<T> {

    List<T> values();

    T get(int index);

}
