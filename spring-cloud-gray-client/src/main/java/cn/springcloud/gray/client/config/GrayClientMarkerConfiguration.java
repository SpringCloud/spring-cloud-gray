package cn.springcloud.gray.client.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrayClientMarkerConfiguration {

    @Bean
    public GrayClientMarker grayClientMarker() {
        return new GrayClientMarker();
    }

    public class GrayClientMarker {
    }
}
