package cn.springcloud.gray.client.config;

import cn.springcloud.gray.*;
import cn.springcloud.gray.cache.CaffeineCache;
import cn.springcloud.gray.choose.*;
import cn.springcloud.gray.client.GrayClientEnrollInitializingDestroyBean;
import cn.springcloud.gray.client.config.properties.*;
import cn.springcloud.gray.client.initialize.DefaultGrayInfosInitializer;
import cn.springcloud.gray.client.initialize.GrayInfosInitializer;
import cn.springcloud.gray.client.switcher.EnvGraySwitcher;
import cn.springcloud.gray.client.switcher.GraySwitcher;
import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.decision.*;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.refresh.RefreshDriver;
import cn.springcloud.gray.request.LocalStorageLifeCycle;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.request.ThreadLocalRequestStorage;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerIdExtractor;
import cn.springcloud.gray.servernode.ServerListProcessor;
import cn.springcloud.gray.spring.SpringEventPublisher;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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
    public SpringEventPublisher springEventPublisher() {
        return new SpringEventPublisher();
    }

    @Bean
    @ConditionalOnMissingBean
    public GrayManager grayManager(
            GrayTrackHolder grayTrackHolder,
            PolicyDecisionManager policyDecisionManager) {
        return new DefaultGrayManager(grayTrackHolder, policyDecisionManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public PolicyDecisionManager policyDecisionManager(GrayDecisionFactoryKeeper grayDecisionFactoryKeeper) {
        PolicyDecisionManager policyDecisionManager = new DefaultPolicyDecisionManager(grayDecisionFactoryKeeper);
        CacheProperties cacheProperties = grayClientProperties.getCacheProperties("policy");
        com.github.benmanes.caffeine.cache.Cache<String, Policy> cache = Caffeine.newBuilder()
                .expireAfterWrite(cacheProperties.getExpireSeconds(), TimeUnit.SECONDS)
                .initialCapacity(20)
                .maximumSize(cacheProperties.getMaximumSize())
                .softValues()
                .recordStats()
                .build();
        return new DefaultCachePolicyDecisionManager(new CaffeineCache(cache), policyDecisionManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public GraySwitcher graySwitcher() {
        return new EnvGraySwitcher(grayProperties);
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

    @Bean
    @ConditionalOnMissingBean
    public PolicyPredicate grayServerPredicate(
            GrayManager grayManager, PolicyDecisionManager policyDecisionManager) {
        return new GrayServerPredicate(grayManager, policyDecisionManager);
    }


    @Bean
    @ConditionalOnMissingBean
    public GrayInfosInitializer grayInfosInitializer(
            GrayClientConfig grayClientConfig,
            InformationClient informationClient,
            RefreshDriver refreshDriver) {
        return new DefaultGrayInfosInitializer(grayClientConfig, informationClient, refreshDriver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerChooser serverChooser(
            GrayManager grayManager,
            GraySwitcher graySwitcher,
            ServerExplainer serverExplainer,
            ServerIdExtractor serverIdExtractor,
            InstanceGrayServerSorter instanceGrayServerSorter,
            @Autowired(required = false) ServerListProcessor serverListProcessor) {

        if (serverListProcessor == null) {
            serverListProcessor = GrayClientHolder.getServereListProcessor();
        }
        return new DefaultServerChooser(
                grayManager,
                graySwitcher,
                serverIdExtractor,
                serverExplainer,
                instanceGrayServerSorter,
                null,
                serverListProcessor);
    }


    @Bean
    public InstanceGrayServerSorter instanceGrayServerSorter(
            ServerIdExtractor serverServerIdExtractor,
            GrayManager grayManager,
            RequestLocalStorage requestLocalStorage,
            PolicyDecisionManager policyDecisionManager,
            ServerExplainer serverExplainer) {

        return new InstanceGrayServerSorter(
                serverServerIdExtractor,
                grayManager,
                requestLocalStorage,
                policyDecisionManager,
                serverExplainer);
    }


    @Bean
    @ConditionalOnMissingBean
    public ServerIdExtractor serverIdExtractor(
            RequestLocalStorage requestLocalStorage,
            ServerExplainer serverServerExplainer) {
        return new DefaultServerIdextractor(requestLocalStorage, serverServerExplainer);
    }
}
