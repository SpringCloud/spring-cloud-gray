package cn.springcloud.gray.hook;

/**
 * order 值越小，优先级越低
 *
 * @author saleson
 * @date 2020-09-23 22:25
 */
@FunctionalInterface
public interface ShutdownHook extends Hook {

    void shutdown();
}
