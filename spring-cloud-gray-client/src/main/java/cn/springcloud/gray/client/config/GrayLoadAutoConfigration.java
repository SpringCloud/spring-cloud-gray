package cn.springcloud.gray.client.config;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.client.config.properties.GrayLoadProperties;
import cn.springcloud.gray.model.GrayStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "gray.load.enabled", havingValue = "true")
@EnableConfigurationProperties({GrayLoadProperties.class})
public class GrayLoadAutoConfigration {

    @Autowired
    private GrayLoadProperties grayLoadProperties;
    @Autowired
    private GrayManager grayManager;


    @Bean
    public InitializingBean loadGrayInfoInitializing() {
        return () -> {
            grayLoadProperties.getGrayInstances().forEach(instance -> {
                if (instance.getGrayStatus() == null) {
                    instance.setGrayStatus(GrayStatus.OPEN);
                }
                grayManager.updateGrayInstance(instance);
            });
        };
    }

}
