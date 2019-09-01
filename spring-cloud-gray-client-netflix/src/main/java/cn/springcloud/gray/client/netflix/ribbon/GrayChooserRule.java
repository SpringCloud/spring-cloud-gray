package cn.springcloud.gray.client.netflix.ribbon;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.ServerChooser;
import com.google.common.base.Optional;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;

public class GrayChooserRule extends ZoneAvoidanceRule {

    private ServerChooser<Server> serverChooser;

    public GrayChooserRule() {
        serverChooser = GrayClientHolder.getServerChooser();
    }

    @Override
    public Server choose(Object key) {
        return serverChooser.chooseServer(getLoadBalancer().getAllServers(), servers -> {
            Optional<Server> server = getPredicate().chooseRoundRobinAfterFiltering(servers, key);
            if (server.isPresent()) {
                return server.get();
            } else {
                return null;
            }
        });
    }
}
