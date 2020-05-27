package cn.springcloud.gray.choose.loadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author saleson
 * @date 2020-05-27 23:49
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private final AtomicInteger nextIndex = new AtomicInteger();

    @Override
    public <T> T choose(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        int idx = incrementAndGetModulo(list.size());
        return list.get(idx);
    }

    @Override
    public <T> T choose(T... ts) {
        if (ts.length < 1) {
            return null;
        }
        int idx = incrementAndGetModulo(ts.length);
        return ts[idx];
    }


    private int incrementAndGetModulo(int modulo) {
        for (; ; ) {
            int current = nextIndex.get();
            int next = (current + 1) % modulo;
            if (nextIndex.compareAndSet(current, next) && current < modulo)
                return current;
        }
    }
}
