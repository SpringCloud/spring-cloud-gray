package cn.springcloud.gray.server.initialize;

import cn.springcloud.gray.model.RoutePolicy;
import cn.springcloud.gray.model.RoutePolicyType;
import cn.springcloud.gray.server.module.gray.GrayServerModule;
import cn.springcloud.gray.server.module.gray.domain.GrayInstance;
import cn.springcloud.gray.server.module.gray.domain.GrayService;
import cn.springcloud.gray.server.module.route.policy.RoutePolicyModule;
import cn.springcloud.gray.server.module.user.ServiceManageModule;
import org.springframework.beans.factory.InitializingBean;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-11 18:58
 */
public class RoutePolicyAuthorityPredicateInitializer implements InitializingBean {
    private RoutePolicyModule routePolicyModule;
    private GrayServerModule grayServerModule;
    private ServiceManageModule serviceManageModule;

    public RoutePolicyAuthorityPredicateInitializer(RoutePolicyModule routePolicyModule, GrayServerModule grayServerModule, ServiceManageModule serviceManageModule) {
        this.routePolicyModule = routePolicyModule;
        this.grayServerModule = grayServerModule;
        this.serviceManageModule = serviceManageModule;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        routePolicyModule.registerResourceAuthorityPredicate(
                RoutePolicyType.INSTANCE_ROUTE.name(), this::predicateInstanceRouteResourceAuthority);

        routePolicyModule.registerResourceAuthorityPredicate(
                RoutePolicyType.SERVICE_MULTI_VER_ROUTE.name(), this::predicateServiceRouteResourceAuthority);

        routePolicyModule.registerResourceAuthorityPredicate(
                RoutePolicyType.SERVICE_ROUTE.name(), this::predicateServiceRouteResourceAuthority);

    }


    private boolean predicateInstanceRouteResourceAuthority(String ns, String userId, RoutePolicy routePolicy) {
        if (!predicateServiceRouteResourceAuthority(ns, userId, routePolicy)) {
            return false;
        }

        GrayInstance grayInstance = grayServerModule.getGrayInstance(routePolicy.getResource());
        return Objects.nonNull(grayInstance);
    }

    private boolean predicateServiceRouteResourceAuthority(String ns, String userId, RoutePolicy routePolicy) {
        if (!serviceManageModule.hasServiceAuthority(routePolicy.getModuleId(), userId)) {
            return false;
        }

        GrayService grayService = grayServerModule.getGrayService(routePolicy.getModuleId());
        if (Objects.isNull(grayService)) {
            return false;
        }

        return Objects.equals(ns, grayService.getNamespace());
    }
}
