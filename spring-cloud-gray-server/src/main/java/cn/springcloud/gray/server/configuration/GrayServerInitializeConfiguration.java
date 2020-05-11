package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.server.GrayServerInitializer;
import cn.springcloud.gray.server.initialize.RoutePolicyAuthorityPredicateInitializer;
import cn.springcloud.gray.server.module.gray.GrayServerModule;
import cn.springcloud.gray.server.module.route.policy.RoutePolicyModule;
import cn.springcloud.gray.server.module.user.ServiceManageModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author saleson
 * @date 2020-05-11 19:17
 */
@Configuration
public class GrayServerInitializeConfiguration {


    @Bean
    public GrayServerInitializer grayServerInitializer() {
        return new GrayServerInitializer();
    }

    @Bean
    public RoutePolicyAuthorityPredicateInitializer routePolicyAuthorityPredicateInitializer(
            RoutePolicyModule routePolicyModule,
            GrayServerModule grayServerModule,
            ServiceManageModule serviceManageModule) {
        return new RoutePolicyAuthorityPredicateInitializer(routePolicyModule, grayServerModule, serviceManageModule);
    }


}
