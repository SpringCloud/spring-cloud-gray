package cn.springcloud.gray.server.module.event.listener;

import cn.springcloud.gray.model.RoutePolicyType;
import cn.springcloud.gray.server.constant.DataOPType;
import cn.springcloud.gray.server.module.event.GrayServiceEvent;
import cn.springcloud.gray.server.module.gray.domain.GrayService;
import cn.springcloud.gray.server.module.route.policy.RoutePolicyModule;
import org.springframework.context.ApplicationListener;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-09-18 00:58
 */
public class GrayServiceDeleteEventListener implements ApplicationListener<GrayServiceEvent> {


    private RoutePolicyModule routePolicyModule;

    public GrayServiceDeleteEventListener(RoutePolicyModule routePolicyModule) {
        this.routePolicyModule = routePolicyModule;
    }

    @Override
    public void onApplicationEvent(GrayServiceEvent event) {
        if (!Objects.equals(event.getDataOPType(), DataOPType.DELETE)) {
            return;
        }
        GrayService grayService = event.getEventSource();
        if (Objects.isNull(grayService)) {
            return;
        }
        routePolicyModule.physicsDeleteRoutePolicy(
                RoutePolicyType.SERVICE_ROUTE.name(),
                grayService.getServiceId(),
                grayService.getServiceId());

        routePolicyModule.physicsDeleteRoutePolicy(
                RoutePolicyType.SERVICE_MULTI_VER_ROUTE.name(),
                grayService.getServiceId(),
                null);
    }
}
