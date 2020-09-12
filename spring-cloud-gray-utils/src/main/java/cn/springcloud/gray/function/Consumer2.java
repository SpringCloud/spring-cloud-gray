package cn.springcloud.gray.function;

/**
 * @author saleson
 * @date 2020-05-11 08:36
 */
@FunctionalInterface
public interface Consumer2<T1, T2> {

    void accept(T1 t1, T2 t2);
}
