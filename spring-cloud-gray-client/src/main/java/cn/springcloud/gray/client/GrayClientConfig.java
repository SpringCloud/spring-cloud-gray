package cn.springcloud.gray.client;

public interface GrayClientConfig {


    /**
     * 启动时是否灰度注册
     *
     * @return
     */
    boolean isGrayEnroll();

    /**
     * 向灰度服务器注册的延迟时间(ms)
     *
     * @return
     */
    int grayEnrollDealyTimeInMs();


    /**
     * 灰度服务器的url
     *
     * @return
     */
    String getServerUrl();


    /**
     * 更新灰度列表的时间间隔(ms)
     *
     * @return
     */
    int getServiceUpdateIntervalTimerInMs();


    /**
     * 在和灰度服务器通信时，如果交互失败，是否重试。
     *
     * @return
     */
    boolean isRetryable();

    /**
     * 重试次数
     *
     * @return
     */
    int getRetryNumberOfRetries();

}
