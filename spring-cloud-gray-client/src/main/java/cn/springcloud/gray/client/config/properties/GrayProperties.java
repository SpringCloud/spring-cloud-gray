package cn.springcloud.gray.client.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("gray")
public class GrayProperties {

    private boolean enabled;

    private boolean grayRouting = true;


}
