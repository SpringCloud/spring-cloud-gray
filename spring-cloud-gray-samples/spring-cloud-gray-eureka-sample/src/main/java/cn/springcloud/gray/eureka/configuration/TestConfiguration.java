package cn.springcloud.gray.eureka.configuration;

import cn.springcloud.gray.eureka.domain.ApiRes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    public ApiRes apiRes() {
        return ApiRes.builder().code("0").data("data").build();
    }
}
