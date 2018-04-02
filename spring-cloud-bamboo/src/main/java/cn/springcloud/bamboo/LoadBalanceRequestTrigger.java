package cn.springcloud.bamboo;

public interface LoadBalanceRequestTrigger {


    boolean shouldExecute();

    void before(ConnectPointContext connectPointContext);

    void after(ConnectPointContext connectPointContext);
}
