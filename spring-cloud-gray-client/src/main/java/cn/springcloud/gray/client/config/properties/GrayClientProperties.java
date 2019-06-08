package cn.springcloud.gray.client.config.properties;

import cn.springcloud.gray.GrayClientConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("gray.client")
public class GrayClientProperties implements GrayClientConfig {

    private String runenv = "web";

    private int serviceUpdateIntervalTimerInMs = 60000;

    /**
     * 实始化灰度信息的延迟时间
     */
    private int serviceInitializeDelayTimeInMs = 40000;

    private InstanceConfig instance = new InstanceConfig();

    private Map<String, CacheProperties> caches = new HashMap<>();


    @Override
    public String runenv() {
        return runenv;
    }

    @Override
    public boolean isGrayEnroll() {
        return instance.isGrayEnroll();
    }

    @Override
    public int grayEnrollDealyTimeInMs() {
        return instance.getGrayEnrollDealyTimeInMs();
    }


    @Override
    public int getServiceUpdateIntervalTimerInMs() {
        return serviceUpdateIntervalTimerInMs;
    }

    public Map<String, CacheProperties> getCaches() {
        return caches;
    }

    public void setCaches(Map<String, CacheProperties> caches) {
        this.caches = caches;
    }

    public CacheProperties getCacheProperties(String key) {
        CacheProperties cacheProperties = getCaches().get(key);
        if (cacheProperties == null) {
            cacheProperties = new CacheProperties();
            caches.put(key, cacheProperties);
        }
        return cacheProperties;
    }

    public void setServiceUpdateIntervalTimerInMs(int serviceUpdateIntervalTimerInMs) {
        this.serviceUpdateIntervalTimerInMs = serviceUpdateIntervalTimerInMs;
    }

    @Override
    public int getServiceInitializeDelayTimeInMs() {
        return serviceInitializeDelayTimeInMs;
    }

    public void setServiceInitializeDelayTimeInMs(int serviceInitializeDelayTimeInMs) {
        this.serviceInitializeDelayTimeInMs = serviceInitializeDelayTimeInMs;
    }

    public InstanceConfig getInstance() {
        return instance;
    }

    public void setInstance(InstanceConfig instance) {
        this.instance = instance;
    }


    /**
     * 实例
     */
    public class InstanceConfig {

        private boolean grayEnroll = false;
        private int grayEnrollDealyTimeInMs = 40000;
        private boolean useMultiVersion = false;

        public boolean isGrayEnroll() {
            return grayEnroll;
        }

        public void setGrayEnroll(boolean grayEnroll) {
            this.grayEnroll = grayEnroll;
        }

        public int getGrayEnrollDealyTimeInMs() {
            return grayEnrollDealyTimeInMs;
        }

        public void setGrayEnrollDealyTimeInMs(int grayEnrollDealyTimeInMs) {
            this.grayEnrollDealyTimeInMs = grayEnrollDealyTimeInMs;
        }

        /**
         * 是否使用多版本,默认不使用
         *
         * @return ture to use service multi version control
         */
        public boolean isUseMultiVersion() {
            return useMultiVersion;
        }

        public void setUseMultiVersion(boolean useMultiVersion) {
            this.useMultiVersion = useMultiVersion;
        }


    }
}
