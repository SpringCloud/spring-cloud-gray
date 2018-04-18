package cn.springcloud.bamboo;

/**
 * Ribbon请求的触发器，在ribbon请求发起时， 会被执行
 */
public interface LoadBalanceRequestTrigger {


    /**
     * 判断是否需要执行的方法
     *
     * @return
     */
    boolean shouldExecute();


    /**
     * 请求之前执行
     *
     * @param connectPointContext
     */
    void before(ConnectPointContext connectPointContext);


    /**
     * 请求完成之后执行
     * 如果出现异常，该方法依然会被执行
     *
     * @param connectPointContext
     */
    void after(ConnectPointContext connectPointContext);
}
