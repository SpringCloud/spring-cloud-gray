package cn.springcloud.gray.client.config.properties;

import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("gray.load")
@Setter
@Getter
public class GrayLoadProperties {
    private boolean enabled = false;
    private List<GrayInstance> grayInstances = new ArrayList<>();

}
