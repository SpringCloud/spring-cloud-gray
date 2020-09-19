package cn.springcloud.gray.server.module.event.listener;

import cn.springcloud.gray.model.RoutePolicyType;
import cn.springcloud.gray.server.constant.DataOPType;
import cn.springcloud.gray.server.module.event.GrayInstanceEvent;
import cn.springcloud.gray.server.module.gray.domain.GrayInstance;
import cn.springcloud.gray.server.module.route.policy.RoutePolicyModule;
import org.springframework.context.ApplicationListener;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-09-18 00:58
 */
public class GrayInstanceDeleteEventListener implements ApplicationListener<GrayInstanceEvent> {


    private RoutePolicyModule routePolicyModule;

    public GrayInstanceDeleteEventListener(RoutePolicyModule routePolicyModule) {
        this.routePolicyModule = routePolicyModule;
    }

    @Override
    public void onApplicationEvent(GrayInstanceEvent event) {
        if (!Objects.equals(event.getDataOPType(), DataOPType.DELETE)) {
            return;
        }
        GrayInstance grayInstance = event.getEventSource();
        if (Objects.isNull(grayInstance)) {
            return;
        }
        routePolicyModule.physicsDeleteRoutePolicy(
                RoutePolicyType.INSTANCE_ROUTE.name(),
                grayInstance.getServiceId(),
                grayInstance.getInstanceId());
    }
}
