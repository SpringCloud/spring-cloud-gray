package cn.springcloud.bamboo;

public interface BambooRibbonConnectionPoint {


    void executeConnectPoint(ConnectPointContext connectPointContext);


    void shutdownconnectPoint();


//    void executeBeforeReuqestTrigger();



//    void executeAfterReuqestTrigger();

}
