package cn.springcloud.bamboo.ribbon.loadbalancer;

import cn.springcloud.bamboo.BambooAppContext;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;

import java.util.Map;

/**
 * Created by saleson on 2017/11/9.
 */
public class BambooZoneAvoidanceRule extends ZoneAvoidanceRule {

    protected CompositePredicate bambooCompositePredicate;

    public BambooZoneAvoidanceRule() {
        super();
        BambooApiVersionPredicate apiVersionPredicate = new BambooApiVersionPredicate(this);
        bambooCompositePredicate = CompositePredicate.withPredicates(super.getPredicate(),
                apiVersionPredicate).build();
    }

    @Override
    public AbstractServerPredicate getPredicate() {
        return bambooCompositePredicate;
    }


    public Map<String, String> getServerMetadata(String serviceId, Server server) {
        return BambooAppContext.getEurekaServerExtractor().getServerMetadata(serviceId, server);
    }
}
