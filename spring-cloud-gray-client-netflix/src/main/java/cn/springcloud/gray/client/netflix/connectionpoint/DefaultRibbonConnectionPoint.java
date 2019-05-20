package cn.springcloud.gray.client.netflix.connectionpoint;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.RequestInterceptor;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.RequestLocalStorage;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import java.util.List;

public class DefaultRibbonConnectionPoint implements RibbonConnectionPoint {

    private GrayManager grayManager;
    private RequestLocalStorage requestLocalStorage;
    private ThreadLocal<Boolean> hystrixRequestContextInitialized = new ThreadLocal<>();

    public DefaultRibbonConnectionPoint(GrayManager grayManager, RequestLocalStorage requestLocalStorage) {
        this.grayManager = grayManager;
        this.requestLocalStorage = requestLocalStorage;
    }

    @Override
    public void executeConnectPoint(ConnectPointContext connectPointContext) {

        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
            hystrixRequestContextInitialized.set(true);
        }

        ConnectPointContext.setContextLocal(connectPointContext);
        GrayRequest grayRequest = connectPointContext.getGrayRequest();
        grayRequest.setGrayTrackInfo(requestLocalStorage.getGrayTrackInfo());
        requestLocalStorage.setGrayRequest(grayRequest);

        List<RequestInterceptor> interceptors = grayManager.getRequeestInterceptors(connectPointContext.getInterceptroType());
        interceptors.forEach(interceptor -> {
            if (interceptor.shouldIntercept()) {
                if (!interceptor.pre(grayRequest)) {
                    return;
                }
            }
        });

    }

    @Override
    public void shutdownconnectPoint(ConnectPointContext connectPointContext) {
        try {
            if (requestLocalStorage.getGrayRequest() == null) {
                return;
            }

            List<RequestInterceptor> interceptors = grayManager.getRequeestInterceptors(connectPointContext.getInterceptroType());
            interceptors.forEach(interceptor -> {
                if (interceptor.shouldIntercept()) {
                    if (!interceptor.after(connectPointContext.getGrayRequest())) {
                        return;
                    }
                }
            });
            ConnectPointContext.removeContextLocal();
            requestLocalStorage.removeGrayTrackInfo();
            requestLocalStorage.removeGrayRequest();
        } finally {
            Boolean hystrixReqCxtInited = hystrixRequestContextInitialized.get();
            if (hystrixReqCxtInited != null && hystrixReqCxtInited && HystrixRequestContext.isCurrentThreadInitialized()) {
                HystrixRequestContext.getContextForCurrentThread().shutdown();
            }
        }
    }


}
