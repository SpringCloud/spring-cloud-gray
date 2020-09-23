package cn.springcloud.gray.hook;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-09-23 22:26
 */
public class SimpleHookRegistory implements HookRegistory {

    private List<Hook> hooks = new CopyOnWriteArrayList<>();


    @Override
    public List<Hook> getHooks() {
        return Collections.unmodifiableList(hooks);
    }

    @Override
    public List<StartHook> getStartHooks() {
        return hooks.stream()
                .filter(hook -> hook instanceof StartHook)
                .map(hook -> (StartHook) hook)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShutdownHook> getShutdownHooks() {
        List<ShutdownHook> shutdownHooks = hooks.stream()
                .filter(hook -> hook instanceof ShutdownHook)
                .map(hook -> (ShutdownHook) hook)
                .collect(Collectors.toList());
        Collections.reverse(shutdownHooks);
        return shutdownHooks;
    }

    @Override
    public void registerHook(Hook hook) {
        hooks.add(hook);
    }

    @Override
    public void registerHooks(List<Hook> hooks) {
        this.hooks.addAll(hooks);
    }

    @Override
    public void removeHook(Hook hook) {
        hooks.remove(hook);
    }

    @Override
    public void removeHooks(Collection<Hook> hooks) {
        this.hooks.removeAll(hooks);
    }
}
