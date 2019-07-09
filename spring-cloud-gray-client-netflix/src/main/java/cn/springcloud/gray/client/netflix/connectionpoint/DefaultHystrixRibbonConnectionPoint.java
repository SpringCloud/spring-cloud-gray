package cn.springcloud.gray.client.netflix.connectionpoint;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.request.LocalStorageLifeCycle;
import cn.springcloud.gray.request.RequestLocalStorage;

@Deprecated
public class DefaultHystrixRibbonConnectionPoint extends DefaultRibbonConnectionPoint {

    private static final ThreadLocal<Boolean> hystrixRequestContextInitialized = new ThreadLocal<>();

    public DefaultHystrixRibbonConnectionPoint(
            GrayManager grayManager,
            RequestLocalStorage requestLocalStorage,
            LocalStorageLifeCycle localStorageLifeCycle) {
        super(grayManager, requestLocalStorage, localStorageLifeCycle);
    }


    @Override
    public void executeConnectPoint(ConnectPointContext connectPointContext) {
//        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
//            HystrixRequestContext.initializeContext();
//            hystrixRequestContextInitialized.set(true);
//        }
        super.executeConnectPoint(connectPointContext);
    }

    @Override
    public void shutdownconnectPoint(ConnectPointContext connectPointContext) {
        try {
            super.shutdownconnectPoint(connectPointContext);
        } finally {
//            Boolean hystrixReqCxtInited = hystrixRequestContextInitialized.get();
//            if (hystrixReqCxtInited != null) {
//                hystrixRequestContextInitialized.remove();
//                if (hystrixReqCxtInited && HystrixRequestContext.isCurrentThreadInitialized()) {
//                    hystrixRequestContextInitialized.remove();
//                    HystrixRequestContext.getContextForCurrentThread().shutdown();
//                }
//            }
        }
    }


}
