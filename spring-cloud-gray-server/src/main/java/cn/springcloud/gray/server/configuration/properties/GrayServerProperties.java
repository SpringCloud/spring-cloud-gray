package cn.springcloud.gray.server.configuration.properties;

import cn.springcloud.gray.model.InstanceStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@ConfigurationProperties(prefix = "gray.server")
public class GrayServerProperties {

    private int evictionIntervalTimerInMs = 60000;

    private Set<InstanceStatus> normalInstanceStatus =
            new HashSet<>(Arrays.asList(InstanceStatus.STARTING, InstanceStatus.UP));


}
