package cn.springcloud.gray.client.dubbo.configuration;

import cn.springcloud.gray.AliasRegistry;
import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.client.dubbo.listener.ServiceInstanceChangeListener;
import cn.springcloud.gray.client.dubbo.listener.ServiceInstanceLoadListener;
import cn.springcloud.gray.client.dubbo.servernode.*;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.VersionExtractor;
import org.apache.dubbo.rpc.Invoker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author saleson
 * @date 2020-09-10 18:10
 */
@Configuration
@ConditionalOnBean(GrayManager.class)
@Import(DubboPluginNacosRegistoryConfiguration.class)
public class DubboPluginServernodeAutoConfiguration {


    @Bean
    public ServiceInstanceExtractor serviceInstanceExtractor(
            AliasRegistry aliasRegistry, ServiceInstanceIdExtractor serviceInstanceIdExtractor) {
        return new ServiceInstanceExtractor(aliasRegistry, serviceInstanceIdExtractor);
    }


    @Bean
    @ConditionalOnMissingBean
    public ServerMetadataExtractor serverMetadataExtractor(ServiceInstanceExtractor serviceInstanceExtractor) {
        return new DubboServerMetadataExtractor(serviceInstanceExtractor);
    }


    @Bean
    @ConditionalOnMissingBean
    public ServerExplainer serverExplainer(
            VersionExtractor<Invoker> versionExtractor,
            ServerMetadataExtractor<Invoker> serverMetadataExtractor,
            ServiceInstanceExtractor serviceInstanceExtractor) {
        return new DubboServerExplainer(versionExtractor, serverMetadataExtractor, serviceInstanceExtractor);
    }

    @Bean
    public ServiceInstanceLoadListener serviceInstanceLoadListener(
            ServiceInstanceExtractor serviceInstanceExtractor,
            DiscoveryClient discoveryClient) {
        return new ServiceInstanceLoadListener(serviceInstanceExtractor, discoveryClient);
    }

    @Bean
    public ServiceInstanceChangeListener serviceInstanceChangeListener(ServiceInstanceExtractor serviceInstanceExtractor) {
        return new ServiceInstanceChangeListener(serviceInstanceExtractor);
    }
}
