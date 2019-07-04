package cn.springcloud.gray.client.netflix.connectionpoint;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.RequestInterceptor;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.LocalStorageLifeCycle;
import cn.springcloud.gray.request.RequestLocalStorage;

import java.util.List;

public class DefaultRibbonConnectionPoint implements RibbonConnectionPoint {

    private GrayManager grayManager;
    private RequestLocalStorage requestLocalStorage;
    private LocalStorageLifeCycle localStorageLifeCycle;

    public DefaultRibbonConnectionPoint(
            GrayManager grayManager,
            RequestLocalStorage requestLocalStorage,
            LocalStorageLifeCycle localStorageLifeCycle) {
        this.grayManager = grayManager;
        this.requestLocalStorage = requestLocalStorage;
        this.localStorageLifeCycle = localStorageLifeCycle;
    }

    @Override
    public void executeConnectPoint(ConnectPointContext connectPointContext) {

        localStorageLifeCycle.initContext();

        ConnectPointContext.setContextLocal(connectPointContext);
        GrayRequest grayRequest = connectPointContext.getGrayRequest();
        //todo 待优化，为每次请求复制一个GrayTrackInfo
        GrayTrackInfo grayTrackInfo = requestLocalStorage.getGrayTrackInfo();
        grayRequest.setGrayTrackInfo(grayTrackInfo);
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
//        if (requestLocalStorage.getGrayRequest() == null) {
//            return;
//        }
        try {
            List<RequestInterceptor> interceptors = grayManager.getRequeestInterceptors(connectPointContext.getInterceptroType());
            interceptors.forEach(interceptor -> {
                if (interceptor.shouldIntercept()) {
                    if (!interceptor.after(connectPointContext.getGrayRequest())) {
                        return;
                    }
                }
            });
            ConnectPointContext.removeContextLocal();
            requestLocalStorage.removeGrayRequest();
        } finally {
            localStorageLifeCycle.closeContext();
        }
    }


}
