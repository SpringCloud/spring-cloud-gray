package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.server.configuration.properties.GrayServerProperties;
import cn.springcloud.gray.server.discovery.ServiceDiscovery;
import cn.springcloud.gray.server.module.NamespaceFinder;
import cn.springcloud.gray.server.module.NamespaceModule;
import cn.springcloud.gray.server.module.audit.OperateAuditModule;
import cn.springcloud.gray.server.module.audit.jpa.JPAOperateAuditModule;
import cn.springcloud.gray.server.module.event.listener.GrayInstanceDeleteEventListener;
import cn.springcloud.gray.server.module.event.listener.GrayServiceDeleteEventListener;
import cn.springcloud.gray.server.module.event.listener.UserResourceAuthorityEventListener;
import cn.springcloud.gray.server.module.gray.*;
import cn.springcloud.gray.server.module.gray.jpa.*;
import cn.springcloud.gray.server.module.jpa.JPANamespaceFinder;
import cn.springcloud.gray.server.module.jpa.JPANamespaceModule;
import cn.springcloud.gray.server.module.route.policy.RoutePolicyModule;
import cn.springcloud.gray.server.module.route.policy.jpa.JPARoutePolicyModule;
import cn.springcloud.gray.server.module.user.AuthorityModule;
import cn.springcloud.gray.server.module.user.ServiceManageModule;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.module.user.jpa.JPAAuthorityModule;
import cn.springcloud.gray.server.module.user.jpa.JPAServiceManageModule;
import cn.springcloud.gray.server.module.user.jpa.JPAUserModule;
import cn.springcloud.gray.server.oauth2.Oauth2Service;
import cn.springcloud.gray.server.service.*;
import cn.springcloud.gray.event.server.GrayEventTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableJpaRepositories(basePackages = {"cn.springcloud.gray.server.dao.repository"})
@EntityScan(basePackages = {"cn.springcloud.gray.server.dao.model"})
@EnableTransactionManagement
@ConditionalOnBean(GrayServerMarkerConfiguration.GrayServerMarker.class)
public class DBStorageConfiguration {


    @ComponentScan(basePackages = {"cn.springcloud.gray.server.dao.mapper", "cn.springcloud.gray.server.service"})
    @Configuration
    public class DBGrayServrConfiguration {
        @Autowired
        private GrayServerProperties grayServerProperties;

        @Bean
        @ConditionalOnMissingBean
        public GrayServerModule grayServerModule(
                @Autowired(required = false) ServiceDiscovery serviceDiscovery,
                GrayEventTrigger grayEventTrigger,
                GrayServiceService grayServiceService,
                GrayInstanceService grayInstanceService,
                ServiceManageModule serviceManageModule,
                ApplicationEventPublisher applicationEventPublisher) {
            return new JPAGrayServerModule(
                    grayServerProperties,
                    grayEventTrigger,
                    serviceDiscovery,
                    grayServiceService,
                    grayInstanceService,
                    serviceManageModule,
                    applicationEventPublisher);
        }


        @Bean
        @ConditionalOnMissingBean
        public GrayServerTrackModule grayServerTrackModule(
                GrayEventTrigger grayEventTrigger,
                GrayTrackService grayTrackService) {
            return new JPAGrayServerTrackModule(grayEventTrigger, grayTrackService);
        }

        @Bean
        @ConditionalOnMissingBean
        public GrayServiceIdFinder grayServiceIdFinder(
                GrayInstanceService grayInstanceService,
                GrayTrackService grayTrackService) {
            return new JPAGrayServiceIdFinder(grayInstanceService, grayTrackService);
        }

        @Bean
        @ConditionalOnMissingBean
        public UserModule userModule(UserService userService, Oauth2Service oauth2Service) {
            return new JPAUserModule(userService, oauth2Service);
        }

        @Bean
        @ConditionalOnMissingBean
        public ServiceManageModule serviceManageModule(
                UserModule userModule,
                UserServiceAuthorityService userServiceAuthorityService,
                ServiceOwnerService serviceOwnerService) {
            return new JPAServiceManageModule(userModule, userServiceAuthorityService, serviceOwnerService);
        }


        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty(value = "gray.server.operate.audit.enable", matchIfMissing = true)
        public OperateAuditModule operateAuditModule(OperateRecordService operateRecordService) {
            return new JPAOperateAuditModule(operateRecordService);
        }


        @Bean
        @ConditionalOnMissingBean
        public GrayEventLogModule grayEventLogModule(GrayEventLogService grayEventLogService) {
            return new JPAGrayEventLogModule(grayEventLogService);
        }

        @Bean
        @ConditionalOnMissingBean
        public GrayPolicyModule grayPolicyModule(
                GrayPolicyService grayPolicyService,
                GrayDecisionService grayDecisionService,
                GrayEventTrigger grayEventTrigger) {
            return new JPAGrayPolicyModule(grayPolicyService, grayDecisionService, grayEventTrigger);
        }

        @Bean
        @ConditionalOnMissingBean
        public RoutePolicyModule instanceRouteModule(
                RoutePolicyRecordService routePolicyRecordService,
                GrayEventTrigger grayEventTrigger,
                NamespaceFinder namespaceFinder,
                AuthorityModule authorityModule) {
            return new JPARoutePolicyModule(routePolicyRecordService, grayEventTrigger, namespaceFinder, authorityModule);
        }

        @Bean
        @ConditionalOnMissingBean
        public AuthorityModule authorityModule(
                ApplicationEventPublisher eventPublisher,
                UserResourceAuthorityService userResourceAuthorityService,
                AuthorityService authorityService,
                UserModule userModule) {
            return new JPAAuthorityModule(eventPublisher, userResourceAuthorityService, authorityService, userModule);
        }


        @Bean
        @ConditionalOnMissingBean
        public NamespaceFinder namespaceFinder(
                GrayServiceService grayServiceService,
                GrayInstanceService grayInstanceService,
                GrayTrackService grayTrackService,
                GrayPolicyService grayPolicyService,
                GrayDecisionService grayDecisionService) {
            return new JPANamespaceFinder(grayServiceService,
                    grayInstanceService,
                    grayTrackService,
                    grayPolicyService,
                    grayDecisionService);
        }

        @Bean
        @ConditionalOnMissingBean
        public NamespaceModule namespaceModule(NamespaceService namespaceService, NamespaceFinder namespaceFinder, AuthorityModule authorityModule) {
            return new JPANamespaceModule(namespaceService, namespaceFinder, authorityModule);
        }

    }


    @Configuration
    @ConditionalOnProperty("gray.server.instance.eviction.enabled")
    public static class JPAGrayInstanceRecordEvictionConfiguration {

        @Bean
        @ConditionalOnMissingBean
        @Transactional
        public GrayInstanceRecordEvictor grayInstanceRecordEvictor(
                GrayInstanceService grayInstanceService, GrayServerProperties grayServerProperties) {
            GrayServerProperties.InstanceRecordEvictProperties evictProperties =
                    grayServerProperties.getInstance().getEviction();
            return new JPAGrayInstanceRecordEvictor(grayInstanceService,
                    evictProperties.getEvictionInstanceStatus(),
                    evictProperties.getLastUpdateDateExpireDays());
        }
    }


    @Bean
    public UserResourceAuthorityEventListener userResourceAuthorityEventListener(NamespaceModule namespaceModule, AuthorityModule authorityModule) {
        return new UserResourceAuthorityEventListener(namespaceModule, authorityModule);
    }


    @Bean
    public GrayServiceDeleteEventListener grayServiceDeleteEventListener(RoutePolicyModule routePolicyModule) {
        return new GrayServiceDeleteEventListener(routePolicyModule);
    }


    @Bean
    public GrayInstanceDeleteEventListener grayInstanceDeleteEventListener(RoutePolicyModule routePolicyModule) {
        return new GrayInstanceDeleteEventListener(routePolicyModule);
    }

}
