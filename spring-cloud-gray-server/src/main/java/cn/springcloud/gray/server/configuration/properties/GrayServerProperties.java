package cn.springcloud.gray.server.configuration.properties;

import cn.springcloud.gray.model.InstanceStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Setter
@Getter
@ConfigurationProperties(prefix = "gray.server")
public class GrayServerProperties {


    private DiscoveryProperties discovery = new DiscoveryProperties();

    private InstanceProperties instance = new InstanceProperties();


    @Setter
    @Getter
    public static class InstanceProperties {

        private Set<InstanceStatus> normalInstanceStatus =
                new HashSet<>(Arrays.asList(InstanceStatus.STARTING, InstanceStatus.UP));


        private InstanceRecordEvictProperties eviction = new InstanceRecordEvictProperties();


    }


    @Setter
    @Getter
    public static class DiscoveryProperties {
        private boolean evictionEnabled = true;
        private long evictionIntervalTimerInMs = TimeUnit.SECONDS.toMillis(60);
    }


    /**
     * 灰度实例的清理配置参数
     */
    @Setter
    @Getter
    public static class InstanceRecordEvictProperties {

        private boolean enabled;

        /**
         * 定时执行的时间周期
         */
        private long evictionIntervalTimerInMs = TimeUnit.DAYS.toMillis(1l);


        /**
         * 将被清理的状态
         */
        private Set<InstanceStatus> evictionInstanceStatus =
                new HashSet<>(Arrays.asList(InstanceStatus.DOWN, InstanceStatus.UNKNOWN));


        /**
         * 最后更新时间过期天数
         */
        private int lastUpdateDateExpireDays = 1;
    }
}
