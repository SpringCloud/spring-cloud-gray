package cn.springcloud.gray.client.netflix.connectionpoint;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.RequestInterceptor;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.RequestLocalStorage;

import java.util.List;

public class DefaultRibbonConnectionPoint implements RibbonConnectionPoint {

    private GrayManager grayManager;
    private RequestLocalStorage requestLocalStorage;

    public DefaultRibbonConnectionPoint(GrayManager grayManager, RequestLocalStorage requestLocalStorage) {
        this.grayManager = grayManager;
        this.requestLocalStorage = requestLocalStorage;
    }

    @Override
    public void executeConnectPoint(ConnectPointContext connectPointContext) {

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
//        if (requestLocalStorage.getGrayRequest() == null) {
//            return;
//        }

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
    }


}
