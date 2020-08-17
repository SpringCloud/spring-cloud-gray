package cn.springcloud.gray.apollo.sample.config;

import cn.springcloud.gray.apollo.sample.core.ApolloAutoRefreshListener;
import cn.springcloud.gray.apollo.sample.core.EnvironmentRefresher;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.spring.annotation.ApolloAnnotationProcessor;
import com.ctrip.framework.apollo.spring.config.PropertySourcesProcessor;
import com.google.common.base.Splitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author saleson
 * @date 2019-12-21 21:33
 */
@Configuration
@ConditionalOnProperty("apollo.bootstrap.enabled")
@ConditionalOnClass({PropertySourcesProcessor.class, ContextRefresher.class, ApolloAnnotationProcessor.class})
public class ApolloAutoRefreshConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public EnvironmentRefresher environmentRefresher() {
        return new EnvironmentRefresher();
    }


    @Configuration
    @AutoConfigureAfter(WebMvcAutoConfiguration.class)
    @AutoConfigureOrder
    @ConditionalOnProperty(name = "spring.cloud.refresh.enabled", matchIfMissing = true)
    public class AutoRefreshConfiguration {

        @Value("${apollo.bootstrap.namespaces:application}")
        private String namespaces;


        @Bean
        @ConditionalOnMissingBean(ApolloAutoRefreshListener.class)
        public ApolloAutoRefreshListener apolloAutoRefreshListener(EnvironmentRefresher environmentRefresher) {

            ApolloAutoRefreshListener listener = new ApolloAutoRefreshListener(environmentRefresher);
            Splitter.on(",").split(namespaces).forEach(namespace -> {
                Config config = ConfigService.getConfig(namespace);
                config.addChangeListener(listener);
            });

            return listener;
        }
    }
}
