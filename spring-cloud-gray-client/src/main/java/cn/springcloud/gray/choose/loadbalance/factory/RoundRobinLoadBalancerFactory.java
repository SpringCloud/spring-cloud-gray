package cn.springcloud.gray.choose.loadbalance.factory;

import cn.springcloud.gray.choose.loadbalance.LoadBalancer;
import cn.springcloud.gray.choose.loadbalance.RoundRobinLoadBalancer;

/**
 * @author saleson
 * @date 2020-05-28 01:17
 */
public class RoundRobinLoadBalancerFactory implements LoadBalancerFactory {
    @Override
    public LoadBalancer create() {
        return new RoundRobinLoadBalancer();
    }
}
