package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.event.GrayEventPublisher;
import cn.springcloud.gray.server.module.GrayServerModule;
import cn.springcloud.gray.server.module.SimpleGrayServerModule;
import cn.springcloud.gray.server.service.GrayDecisionService;
import cn.springcloud.gray.server.service.GrayInstanceService;
import cn.springcloud.gray.server.service.GrayPolicyService;
import cn.springcloud.gray.server.service.GrayServiceService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@ConditionalOnBean(GrayServerMarkerConfiguration.GrayServerMarker.class)
public class DBStorageConfiguration {


    @Bean
    public GrayServerModule grayServerModule(
            GrayEventPublisher grayEventPublisher,
            GrayServiceService grayServiceService, GrayInstanceService grayInstanceService,
            GrayDecisionService grayDecisionService, GrayPolicyService grayPolicyService) {
        return new SimpleGrayServerModule(grayEventPublisher, grayServiceService, grayInstanceService, grayDecisionService, grayPolicyService);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
