package cn.springcloud.gray.client.config;


import cn.springcloud.gray.GrayManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(GrayManager.class)
public class GrayClientWebConfiguration {


    @Configuration
    @ComponentScan(basePackages = {"cn.springcloud.gray.web.resources"})
    public static class GrayClientResourcesConfiguration {

    }
}
