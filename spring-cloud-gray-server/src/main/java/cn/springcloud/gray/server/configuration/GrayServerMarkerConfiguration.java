package cn.springcloud.gray.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrayServerMarkerConfiguration {


    @Bean
    public GrayServerMarker grayServerMarkerBean() {
        return new GrayServerMarker();
    }

    class GrayServerMarker {
    }
}
