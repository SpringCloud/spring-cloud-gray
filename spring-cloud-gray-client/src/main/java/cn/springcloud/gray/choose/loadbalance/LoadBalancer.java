package cn.springcloud.gray.choose.loadbalance;

import java.util.List;

/**
 * 负载均衡器
 *
 * @author saleson
 * @date 2020-05-27 23:44
 */
public interface LoadBalancer {

    <T> T choose(List<T> list);

    <T> T choose(T... ts);

}
