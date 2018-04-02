package cn.springcloud.bamboo.feign.config;

import cn.springcloud.bamboo.feign.BambooFeignClient;
import feign.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BambooFeignClientsConfiguration {


    @Autowired
    private Client feignClient;

    @Bean
    public Client bambooFeignClient() {
        return new BambooFeignClient(feignClient);
    }

}
