package cn.springcloud.gray.client.netflix.connectionpoint;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.RequestInterceptor;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.RequestLocalStorage;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.Collection;
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

    }


}
