package cn.springcloud.gray.client.config.properties;

import cn.springcloud.gray.communication.RetryableInformationClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties("gray.server")
public class GrayServerProperties {

    private String url;
    private boolean loadbalanced;
    //在和灰度服务器通信时，如果交互失败，是否重试。
    private boolean retryable = true;
    //重试次数
    private int retryNumberOfRetries = RetryableInformationClient.DEFAULT_NUMBER_OF_RETRIES;
}
