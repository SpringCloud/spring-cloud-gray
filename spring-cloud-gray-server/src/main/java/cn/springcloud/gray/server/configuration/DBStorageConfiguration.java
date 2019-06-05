package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.event.GrayEventPublisher;
import cn.springcloud.gray.server.configuration.properties.GrayServerProperties;
import cn.springcloud.gray.server.discovery.ServiceDiscovery;
import cn.springcloud.gray.server.module.GrayInstanceRecordEvictor;
import cn.springcloud.gray.server.module.GrayServerModule;
import cn.springcloud.gray.server.module.GrayServerTrackModule;
import cn.springcloud.gray.server.module.jpa.JPAGrayInstanceRecordEvictor;
import cn.springcloud.gray.server.module.jpa.JPAGrayServerModule;
import cn.springcloud.gray.server.module.jpa.JPAGrayServerTrackModule;
import cn.springcloud.gray.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
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
        public GrayServerModule grayServerModule(
                GrayEventPublisher grayEventPublisher, @Autowired(required = false) ServiceDiscovery serviceDiscovery,
                GrayServiceService grayServiceService, GrayInstanceService grayInstanceService,
                GrayDecisionService grayDecisionService, GrayPolicyService grayPolicyService) {
            return new JPAGrayServerModule(
                    grayServerProperties, grayEventPublisher, serviceDiscovery, grayServiceService, grayInstanceService,
                    grayDecisionService, grayPolicyService);
        }


        @Bean
        public GrayServerTrackModule grayServerTrackModule(GrayEventPublisher grayEventPublisher, GrayTrackService grayTrackService) {
            return new JPAGrayServerTrackModule(grayEventPublisher, grayTrackService);
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

}
