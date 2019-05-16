package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.event.GrayEventPublisher;
import cn.springcloud.gray.server.module.GrayServerModule;
import cn.springcloud.gray.server.module.GrayServerTrackModule;
import cn.springcloud.gray.server.module.SimpleGrayServerModule;
import cn.springcloud.gray.server.module.SimpleGrayServerTrackModule;
import cn.springcloud.gray.server.service.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = {"cn.springcloud.gray.server.dao.repository"})
@EntityScan(basePackages = {"cn.springcloud.gray.server.dao.model"})
@EnableTransactionManagement
@ConditionalOnBean(GrayServerMarkerConfiguration.GrayServerMarker.class)
public class DBStorageConfiguration {


    @ComponentScan(basePackages = {"cn.springcloud.gray.server.dao.mapper", "cn.springcloud.gray.server.service"})
    @Configuration
    public class DBGrayServrConfiguration {

        @Bean
        public GrayServerModule grayServerModule(
                GrayEventPublisher grayEventPublisher,
                GrayServiceService grayServiceService, GrayInstanceService grayInstanceService,
                GrayDecisionService grayDecisionService, GrayPolicyService grayPolicyService) {
            return new SimpleGrayServerModule(grayEventPublisher, grayServiceService, grayInstanceService, grayDecisionService, grayPolicyService);
        }


        @Bean
        public GrayServerTrackModule grayServerTrackModule(GrayEventPublisher grayEventPublisher, GrayTrackService grayTrackService) {
            return new SimpleGrayServerTrackModule(grayEventPublisher, grayTrackService);
        }

    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
