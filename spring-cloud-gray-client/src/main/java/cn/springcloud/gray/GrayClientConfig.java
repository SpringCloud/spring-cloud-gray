package cn.springcloud.gray;

public interface GrayClientConfig {

    /**
     * 运行类型：web
     *
     * @return run env
     */
    String runenv();


    /**
     * 启动时是否灰度注册
     *
     * @return boolean true to register gray after started
     */
    boolean isGrayEnroll();

    /**
     * 向灰度服务器注册的延迟时间(ms)
     *
     * @return 返回灰度服务器注册的延迟时间(ms)
     */
    int grayEnrollDealyTimeInMs();


    /**
     * 灰度服务器的url
     *
     * @return 返回gray-server的url
     */
    String getServerUrl();


    /**
     * 更新灰度列表的时间间隔(ms)，小于等于0将不会开启定时轮询
     *
     * @return 返回更新灰度列表的时间间隔(ms)
     */
    int getServiceUpdateIntervalTimerInMs();


    /**
     * 在和灰度服务器通信时，如果交互失败，是否重试。
     *
     * @return 返回是否重试
     */
    boolean isRetryable();

    /**
     * 重试次数
     *
     * @return 返回重试次数
     */
    int getRetryNumberOfRetries();

}
