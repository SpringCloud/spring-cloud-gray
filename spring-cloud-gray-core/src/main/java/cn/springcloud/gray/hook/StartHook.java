package cn.springcloud.gray.hook;

/**
 * order 值越小，优先级越高
 *
 * @author saleson
 * @date 2020-09-23 22:24
 */
@FunctionalInterface
public interface StartHook extends Hook {
    void start();
}
