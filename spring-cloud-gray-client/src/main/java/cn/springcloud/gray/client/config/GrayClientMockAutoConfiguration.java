package cn.springcloud.gray.client.config;

import cn.springcloud.gray.cache.CaffeineCache;
import cn.springcloud.gray.client.config.properties.CacheProperties;
import cn.springcloud.gray.client.config.properties.GrayClientProperties;
import cn.springcloud.gray.client.config.properties.GrayMockProperties;
import cn.springcloud.gray.decision.Policy;
import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.handle.HandleManager;
import cn.springcloud.gray.handle.HandleRuleManager;
import cn.springcloud.gray.mock.*;
import cn.springcloud.gray.mock.factory.HttpResponseMockActionFactory;
import cn.springcloud.gray.mock.factory.MockActionFactory;
import cn.springcloud.gray.mock.factory.PauseMockActionFactory;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.servernode.ServerExplainer;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author saleson
 * @date 2020-05-27 10:50
 */

@Configuration
@EnableConfigurationProperties(GrayMockProperties.class)
//@ConditionalOnBean(GrayManager.class)
@ConditionalOnProperty(value = "gray.enabled", havingValue = "true")
public class GrayClientMockAutoConfiguration {

    @Autowired
    private GrayClientProperties grayClientProperties;


    @Configuration
    @ConditionalOnProperty(value = "gray.mock.enabled", havingValue = "false", matchIfMissing = true)
    public class NoOpMockConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public MockManager mockManager() {
            return new NoOpMockManager();
        }
    }


    @Configuration
    @ConditionalOnProperty(value = "gray.mock.enabled", havingValue = "true")
    public class MockConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public MockDriver mockDriver(List<MockActionFactory> mockActionFactories) {
            MockDriver mockDriver = new DefaultMockDriver(mockActionFactories);
            return mockDriver;
        }


        @Bean
        @ConditionalOnMissingBean
        public MockManager mockManager(
                HandleManager handleManager,
                HandleRuleManager handleRuleManager,
                MockDriver mockDriver,
                RequestLocalStorage requestLocalStorage,
                PolicyDecisionManager policyDecisionManager,
                ServerExplainer serverExplainer) {

            CacheProperties cacheProperties = grayClientProperties.getCacheProperties("mock");
            com.github.benmanes.caffeine.cache.Cache<String, Policy> cache = Caffeine.newBuilder()
                    .expireAfterWrite(cacheProperties.getExpireSeconds(), TimeUnit.SECONDS)
                    .initialCapacity(20)
                    .maximumSize(cacheProperties.getMaximumSize())
                    .softValues()
                    .recordStats()
                    .build();

            return new DefaultMockManager(handleManager, handleRuleManager, mockDriver, requestLocalStorage, policyDecisionManager, serverExplainer, new CaffeineCache(cache));
        }


        @Bean
        @ConditionalOnMissingBean(name = "mockHandleChangedListener")
        public MockHandleChangedListener mockHandleChangedListener(MockManager mockManager) {
            return new MockHandleChangedListener(mockManager);
        }


        @Bean
        public PauseMockActionFactory pauseMockActionFactory() {
            return new PauseMockActionFactory();
        }


        @Bean
        public HttpResponseMockActionFactory httpResponseMockActionFactory() {
            return new HttpResponseMockActionFactory();
        }
    }


}
