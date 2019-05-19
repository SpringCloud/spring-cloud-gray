package cn.springcloud.gray.client.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"cn.springcloud.gray.web.resources"})
public class GrayClientWebConfiguration {
}
