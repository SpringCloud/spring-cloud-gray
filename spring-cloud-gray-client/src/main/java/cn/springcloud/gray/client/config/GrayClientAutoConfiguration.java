package cn.springcloud.gray.client.config;

import cn.springcloud.gray.*;
import cn.springcloud.gray.cache.CaffeineCache;
import cn.springcloud.gray.choose.*;
import cn.springcloud.gray.choose.loadbalance.factory.LoadBalancerFactory;
import cn.springcloud.gray.choose.loadbalance.factory.RoundRobinLoadBalancerFactory;
import cn.springcloud.gray.client.GrayClientEnrollInitializingDestroyBean;
import cn.springcloud.gray.client.config.properties.*;
import cn.springcloud.gray.client.initialize.DefaultGrayInfosInitializer;
import cn.springcloud.gray.client.initialize.GrayClientApplicationRunner;
import cn.springcloud.gray.client.initialize.GrayInfosInitializer;
import cn.springcloud.gray.client.switcher.EnvGraySwitcher;
import cn.springcloud.gray.client.switcher.GraySwitcher;
import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.decision.*;
import cn.springcloud.gray.mock.MockManager;
import cn.springcloud.gray.mock.NoOpMockManager;
import cn.springcloud.gray.refresh.RefreshDriver;
import cn.springcloud.gray.request.LocalStorageLifeCycle;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.request.ThreadLocalRequestStorage;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import cn.springcloud.gray.routing.connectionpoint.DefaultRoutingConnectionPoint;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectionPoint;
import cn.springcloud.gray.servernode.*;
import cn.springcloud.gray.spring.SpringEventPublisher;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties({
        GrayProperties.class,
        GrayClientProperties.class,
        GrayChooseProperties.class,
        GrayServerProperties.class,
        GrayRequestProperties.class,
        GrayLoadProperties.class,
        GrayHoldoutServerProperties.class
})
@Import({
        InformationClientConfiguration.class,
        GrayDecisionFactoryConfiguration.class,
        GrayTrackConfiguration.class
})
@ConditionalOnProperty(value = "gray.enabled")
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
    public AliasRegistry aliasRegistry() {
        return new SimpleAliasRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    public GrayManager grayManager(
            GrayTrackHolder grayTrackHolder,
            PolicyDecisionManager policyDecisionManager,
            AliasRegistry aliasRegistry) {
        return new DefaultGrayManager(grayTrackHolder, policyDecisionManager, aliasRegistry);
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


    @Configuration
    @ConditionalOnProperty(value = "gray.client.instance.grayEnroll")
    public class GrayClientEnrollConfiguration {
        @Bean
        public GrayClientEnrollInitializingDestroyBean grayClientEnrollInitializingDestroyBean(
                InformationClient informationClient) {
            return new GrayClientEnrollInitializingDestroyBean(informationClient, grayClientProperties);
        }
    }


    @Bean
    @ConditionalOnMissingBean
    public RequestLocalStorage requestLocalStorage(LocalStorageLifeCycle localStorageLifeCycle) {
        return new ThreadLocalRequestStorage(localStorageLifeCycle);
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
    @ConditionalOnMissingBean(name = "simplePolicyPredicate")
    public SimplePolicyPredicate simplePolicyPredicate() {
        return new SimplePolicyPredicate();
    }

    @Bean
    @ConditionalOnMissingBean(name = "grayServerPredicate")
    public GrayServerPredicate grayServerPredicate(
            GrayManager grayManager, PolicyDecisionManager policyDecisionManager) {
        return new GrayServerPredicate(grayManager, policyDecisionManager);
    }

    @Bean
    @ConditionalOnMissingBean(name = "serviceGrayServerPredicate")
    public ServiceGrayServerPredicate serviceGrayServerPredicate(
            GrayManager grayManager, PolicyDecisionManager policyDecisionManager) {
        return new ServiceGrayServerPredicate(grayManager, policyDecisionManager);
    }

    @Bean
    @ConditionalOnMissingBean(name = "serviceMultiVersionGrayServerPredicate")
    public ServiceMultiVersionGrayServerPredicate serviceMultiVersionGrayServerPredicate(
            GrayManager grayManager, PolicyDecisionManager policyDecisionManager) {
        return new ServiceMultiVersionGrayServerPredicate(grayManager, policyDecisionManager);
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
    public GrayClientApplicationRunner grayClientApplicationRunner(
            GrayInfosInitializer grayInfosInitializer,
            ApplicationEventPublisher applicationEventPublisher) {
        return new GrayClientApplicationRunner(grayInfosInitializer, applicationEventPublisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerChooser serverChooser(
            GrayManager grayManager,
            GraySwitcher graySwitcher,
            ServerExplainer serverExplainer,
            ServerIdExtractor serverIdExtractor,
            @Qualifier("instanceGrayServerSorter") InstanceGrayServerSorter instanceGrayServerSorter,
            @Qualifier("serviceGrayServerSorter") ServiceGrayServerSorter serviceGrayServerSorter,
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
                serviceGrayServerSorter,
                serverListProcessor);
    }


    @Bean
    @ConditionalOnMissingBean
    public ServerDistinguisher serverDistinguisher(
            GrayManager grayManager,
            GraySwitcher graySwitcher,
            ServerExplainer serverExplainer,
            ServerIdExtractor serverIdExtractor,
            @Qualifier("instanceGrayServerSorter") InstanceGrayServerSorter instanceGrayServerSorter,
            @Qualifier("serviceGrayServerSorter") ServiceGrayServerSorter serviceGrayServerSorter,
            @Autowired(required = false) ServerListProcessor serverListProcessor) {

        if (serverListProcessor == null) {
            serverListProcessor = GrayClientHolder.getServereListProcessor();
        }
        return new DefaultServerDistinguisher(
                grayManager,
                graySwitcher,
                serverIdExtractor,
                instanceGrayServerSorter,
                serviceGrayServerSorter,
                serverExplainer,
                serverListProcessor);
    }


    @Bean
    @ConditionalOnMissingBean(name = "serviceGrayServerSorter")
    public ServiceGrayServerSorter serviceGrayServerSorter(
            ServerIdExtractor serverServerIdExtractor,
            GrayManager grayManager,
            RequestLocalStorage requestLocalStorage,
            PolicyDecisionManager policyDecisionManager,
            ServerExplainer serverExplainer) {
        return new ServiceGrayServerSorter(grayManager, requestLocalStorage, policyDecisionManager, serverServerIdExtractor, serverExplainer);
    }

    @Bean
    @ConditionalOnMissingBean(name = "instanceGrayServerSorter")
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
    public RoutingConnectionPoint ribbonConnectionPoint(
            GrayManager grayManager,
            RequestLocalStorage requestLocalStorage,
            LocalStorageLifeCycle localStorageLifeCycle,
            @Autowired(required = false) MockManager mockManager) {
        if (Objects.isNull(mockManager)) {
            mockManager = new NoOpMockManager();
        }
        return new DefaultRoutingConnectionPoint(grayManager, requestLocalStorage, localStorageLifeCycle, mockManager);
    }


    @Bean
    @ConditionalOnMissingBean
    public ServerIdExtractor serverIdExtractor(
            RequestLocalStorage requestLocalStorage,
            ServerExplainer serverServerExplainer) {
        return new DefaultServerIdextractor(requestLocalStorage, serverServerExplainer);
    }


    @Bean
    @ConditionalOnMissingBean
    public VersionExtractor versionExtractor(
            @Value("${gray.client.instance.metadata.version-field:version}") String versionField) {
        return new MetadataVersionExtractor(versionField);
    }


    @Bean
    @ConditionalOnMissingBean
    public LoadBalancerFactory loadBalancerFactory() {
        return new RoundRobinLoadBalancerFactory();
    }
}
