package cn.springcloud.gray.client.plugin.ribbon.nacos.configuration;

import cn.springcloud.gray.client.plugin.ribbon.nacos.NacosInstanceLocalInfoObtainer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author saleson
 * @date 2020-09-18 11:41
 */
@Configuration
@AutoConfigureBefore(name = {"cn.springcloud.gray.client.config.GrayClientDiscoveryAutoConfiguration"})
public class GrayClientNacosDiscoveryAutoConfiguration {

    @Bean
    public NacosInstanceLocalInfoObtainer nacosInstanceLocalInfoObtainer() {
        return new NacosInstanceLocalInfoObtainer();
    }
}
