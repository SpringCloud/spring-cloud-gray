package cn.springcloud.gray.client.plugin.ribbon.nacos.configuration;

import cn.springcloud.gray.client.config.properties.GrayHoldoutServerProperties;
import cn.springcloud.gray.client.plugin.ribbon.nacos.NacosServerListProcessor;
import cn.springcloud.gray.servernode.ServerListProcessor;
import com.alibaba.cloud.nacos.ribbon.NacosServerList;
import com.alibaba.nacos.api.naming.NamingService;
import com.netflix.loadbalancer.Server;
import com.netflix.ribbon.Ribbon;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "gray.enabled")
@ConditionalOnClass({Ribbon.class, NacosServerList.class})
public class GrayClientRibbonNacosAutoConfiguration {


    @Bean
    public ServerListProcessor<Server> serverListProcessor(
            GrayHoldoutServerProperties grayHoldoutServerProperties, NamingService namingService){
        return new NacosServerListProcessor(grayHoldoutServerProperties, namingService);
    }

}
