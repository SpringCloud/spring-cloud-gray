package cn.springcloud.gray.client.config.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "gray.request")
public class GrayRequestProperties {
    private boolean loadBody = false;

}
