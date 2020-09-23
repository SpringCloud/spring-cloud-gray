package cn.springcloud.gray.hook;

import java.util.Collection;
import java.util.List;

/**
 * @author saleson
 * @date 2020-09-23 22:25
 */
public interface HookRegistory {

    List<Hook> getHooks();

    List<StartHook> getStartHooks();

    List<ShutdownHook> getShutdownHooks();

    void registerHook(Hook hook);

    void registerHooks(List<Hook> hooks);

    void removeHook(Hook hook);

    void removeHooks(Collection<Hook> hooks);
}
