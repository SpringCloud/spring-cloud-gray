package cn.springcloud.gray.client.netflix.ribbon;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.choose.ServerChooser;
import cn.springcloud.gray.choose.loadbalance.LoadBalancer;
import cn.springcloud.gray.choose.loadbalance.RoundRobinLoadBalancer;
import cn.springcloud.gray.choose.loadbalance.factory.LoadBalancerFactory;
import cn.springcloud.gray.client.switcher.GraySwitcher;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 尽可能公平的提供负载均衡
 *
 * @author saleson
 * @date 2020-05-27 23:03
 */
@Slf4j
public class GrayLoadBalanceFairPossibleRule extends ZoneAvoidanceRule {

    private ServerChooser<Server> serverChooser;
    private GraySwitcher graySwitcher;
    private Map<String, LoadBalancer> loadBalancers = new ConcurrentHashMap<>();

    public GrayLoadBalanceFairPossibleRule() {
        serverChooser = GrayClientHolder.getServerChooser();
        graySwitcher = GrayClientHolder.getGraySwitcher();
    }

    @Override
    public Server choose(Object key) {
        if (!graySwitcher.state()) {
            log.debug("灰度未开启，从servers列表按Ribbon默认的负载逻辑挑选实例");
            return super.choose(key);
        }


        try {
            return serverChooser.chooseServer(
                    getLoadBalancer().getAllServers(),
                    (group, servers) -> choose(key, group, servers));
        } catch (Exception e) {
            log.warn("gray choose server occur exception:{}, execute super method.", e.getMessage(), e);
            return super.choose(key);
        }
    }

    protected Server choose(Object key, String group, List<Server> servers) {
        List<Server> eligible = getPredicate().getEligibleServers(servers, key);
        if (eligible.size() == 0) {
            return null;
        }
        LoadBalancer loadBalancer = getGroupLoadBalancer(group);
        return loadBalancer.choose(eligible);
    }

    /**
     * 获取负载均衡器
     * 无需加锁/影响性能，且意义不大
     *
     * @param group
     * @return
     */
    protected LoadBalancer getGroupLoadBalancer(String group) {
        LoadBalancer loadBalancer = loadBalancers.get(group);
        if (Objects.isNull(loadBalancer)) {
            LoadBalancerFactory loadBalancerFactory = GrayClientHolder.getLoadBalancerFactory();
            loadBalancer = Objects.nonNull(loadBalancer) ? loadBalancerFactory.create() : new RoundRobinLoadBalancer();
            loadBalancers.put(group, loadBalancer);
        }
        return loadBalancer;
    }
}
