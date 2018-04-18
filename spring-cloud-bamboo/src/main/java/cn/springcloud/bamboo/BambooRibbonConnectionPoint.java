package cn.springcloud.bamboo;


/**
 * 个接口是负责将bamboo跟ribbon连接起来的，将请求的信息， 以及根据业务需要添加的一些路由信息，和获取请求接口的目标版本，
 * 还有触发执行LoadBanceRequestTrigger等，都是由该接口的实现类DefaultRibbonConnectionPoint负责实现。
 */
public interface BambooRibbonConnectionPoint {


    void executeConnectPoint(ConnectPointContext connectPointContext);


    void shutdownconnectPoint();


//    void executeBeforeReuqestTrigger();


//    void executeAfterReuqestTrigger();

}
