package cn.springcloud.gray.choose.loadbalance.factory;

import cn.springcloud.gray.choose.loadbalance.LoadBalancer;

/**
 * @author saleson
 * @date 2020-05-28 01:11
 */
public interface LoadBalancerFactory {

    LoadBalancer create();
}
