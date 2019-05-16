package cn.springcloud.gray.client.config.properties;

import cn.springcloud.gray.GrayClientConfig;
import cn.springcloud.gray.communication.RetryableInformationClient;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("gray.client")
public class GrayClientProperties implements GrayClientConfig {

    private String runenv = "web";

    private int serviceUpdateIntervalTimerInMs = 60000;

    private String informationClient = "http";

    private String serverUrl = "";

    private boolean retryable = true;
    private int retryNumberOfRetries = RetryableInformationClient.DEFAULT_NUMBER_OF_RETRIES;

    private InstanceConfig instance = new InstanceConfig();


    public String getInformationClient() {
        return informationClient;
    }

    public void setInformationClient(String informationClient) {
        this.informationClient = informationClient;
    }

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

    public String getServerUrl() {
        return serverUrl;
    }

    @Override
    public int getServiceUpdateIntervalTimerInMs() {
        return serviceUpdateIntervalTimerInMs;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }


    public void setServiceUpdateIntervalTimerInMs(int serviceUpdateIntervalTimerInMs) {
        this.serviceUpdateIntervalTimerInMs = serviceUpdateIntervalTimerInMs;
    }

    public InstanceConfig getInstance() {
        return instance;
    }

    public void setInstance(InstanceConfig instance) {
        this.instance = instance;
    }


    public boolean isRetryable() {
        return retryable;
    }

    public void setRetryable(boolean retryable) {
        this.retryable = retryable;
    }

    public int getRetryNumberOfRetries() {
        return retryNumberOfRetries;
    }

    public void setRetryNumberOfRetries(int retryNumberOfRetries) {
        this.retryNumberOfRetries = retryNumberOfRetries;
    }

    /**
     * 实例
     */
    public class InstanceConfig {

        private boolean grayEnroll = false;
        private int grayEnrollDealyTimeInMs = 40000;
        private boolean useMultiVersion = false;
        private String instanceId;

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

        public String getInstanceId() {
            return instanceId;
        }

        public void setInstanceId(String instanceId) {
            this.instanceId = instanceId;
        }
    }
}
