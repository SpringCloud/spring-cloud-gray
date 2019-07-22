package cn.springcloud.gray.client.config;

import cn.springcloud.gray.*;
import cn.springcloud.gray.cache.CaffeineCache;
import cn.springcloud.gray.client.GrayClientEnrollInitializingDestroyBean;
import cn.springcloud.gray.client.config.properties.*;
import cn.springcloud.gray.client.switcher.EnbGraySwitcher;
import cn.springcloud.gray.client.switcher.GraySwitcher;
import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.GrayDecisionFactoryKeeper;
import cn.springcloud.gray.request.LocalStorageLifeCycle;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.request.ThreadLocalRequestStorage;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(
        {GrayProperties.class,
                GrayClientProperties.class,
                GrayServerProperties.class,
                GrayRequestProperties.class,
                GrayLoadProperties.class,
                GrayHoldoutServerProperties.class})
@ConditionalOnProperty(value = "gray.enabled")
@Import({InformationClientConfiguration.class,
        GrayDecisionFactoryConfiguration.class,
        GrayTrackConfiguration.class})
public class GrayClientAutoConfiguration {


    @Autowired
    private GrayClientProperties grayClientProperties;
    @Autowired
    private GrayProperties grayProperties;


    @Bean
    @ConditionalOnMissingBean
    public GrayManager grayManager(
            @Autowired(required = false) GrayLoadProperties grayLoadProperties,
            GrayDecisionFactoryKeeper grayDecisionFactoryKeeper,
            @Autowired(required = false) InformationClient informationClient) {

        CacheProperties cacheProperties = grayClientProperties.getCacheProperties("grayDecision");
        com.github.benmanes.caffeine.cache.Cache<String, List<GrayDecision>> cache = Caffeine.newBuilder()
                .maximumSize(cacheProperties.getMaximumSize())
                .expireAfterWrite(cacheProperties.getExpireSeconds(), TimeUnit.SECONDS)
                .softValues()
                .build();

        DefaultGrayManager grayManager = new DefaultGrayManager(
                grayClientProperties, grayLoadProperties, grayDecisionFactoryKeeper, informationClient,
                new CaffeineCache<>(cache));
        return grayManager;

//        return new CachedDelegateGrayManager(grayManager, new CaffeineCache<>(cache));
    }


    @Bean
    @ConditionalOnMissingBean
    public GraySwitcher graySwitcher() {
        return new EnbGraySwitcher(grayProperties);
    }

    @Bean
    @ConditionalOnProperty(value = "gray.client.instance.grayEnroll")
    public GrayClientEnrollInitializingDestroyBean grayClientEnrollInitializingDestroyBean(
            CommunicableGrayManager grayManager, InstanceLocalInfo instanceLocalInfo) {
        return new GrayClientEnrollInitializingDestroyBean(grayManager, grayClientProperties, instanceLocalInfo);
    }


    @Bean
    @ConditionalOnMissingBean
    public RequestLocalStorage requestLocalStorage() {
        return new ThreadLocalRequestStorage();
    }

    @Bean
    @ConditionalOnMissingBean
    public LocalStorageLifeCycle localStorageLifeCycle() {
        return new LocalStorageLifeCycle.NoOpLocalStorageLifeCycle();
    }

    @Bean
    public GrayClientInitializer grayClientInitializer() {
        return new GrayClientInitializer();
    }

}
