package cn.springcloud.gray.client.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("gray")
public class GrayProperties {

    private boolean enabled;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
