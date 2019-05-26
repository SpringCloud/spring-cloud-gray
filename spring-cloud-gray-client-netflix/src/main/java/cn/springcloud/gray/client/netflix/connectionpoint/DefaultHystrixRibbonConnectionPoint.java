package cn.springcloud.gray.client.netflix.connectionpoint;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.request.RequestLocalStorage;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

public class DefaultHystrixRibbonConnectionPoint extends DefaultRibbonConnectionPoint {

    private ThreadLocal<Boolean> hystrixRequestContextInitialized = new ThreadLocal<>();

    public DefaultHystrixRibbonConnectionPoint(GrayManager grayManager, RequestLocalStorage requestLocalStorage) {
        super(grayManager, requestLocalStorage);
    }


    @Override
    public void executeConnectPoint(ConnectPointContext connectPointContext) {
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
            hystrixRequestContextInitialized.set(true);
        }
        super.executeConnectPoint(connectPointContext);
    }

    @Override
    public void shutdownconnectPoint(ConnectPointContext connectPointContext) {
        try {
            super.shutdownconnectPoint(connectPointContext);
        } finally {
            Boolean hystrixReqCxtInited = hystrixRequestContextInitialized.get();
            if (hystrixReqCxtInited != null && hystrixReqCxtInited && HystrixRequestContext.isCurrentThreadInitialized()) {
                HystrixRequestContext.getContextForCurrentThread().shutdown();
            }
        }
    }


}
