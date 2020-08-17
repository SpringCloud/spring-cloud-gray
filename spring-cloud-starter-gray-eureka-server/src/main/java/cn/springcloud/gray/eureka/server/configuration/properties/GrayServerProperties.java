package cn.springcloud.gray.eureka.server.configuration.properties;


import cn.springcloud.gray.eureka.server.communicate.RetryableGrayCommunicateClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("gray.server")
public class GrayServerProperties {

    private String url;
    private boolean retryable = true;
    private int retryNumberOfRetries = RetryableGrayCommunicateClient.DEFAULT_NUMBER_OF_RETRIES;

}
