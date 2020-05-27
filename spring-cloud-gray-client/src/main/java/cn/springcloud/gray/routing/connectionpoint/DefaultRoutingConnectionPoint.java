package cn.springcloud.gray.routing.connectionpoint;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.RequestInterceptor;
import cn.springcloud.gray.mock.GrayReuqestMockInfo;
import cn.springcloud.gray.mock.MockManager;
import cn.springcloud.gray.model.HandleRuleType;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.LocalStorageLifeCycle;
import cn.springcloud.gray.request.RequestLocalStorage;

import java.util.List;

public class DefaultRoutingConnectionPoint implements RoutingConnectionPoint {

    private GrayManager grayManager;
    private RequestLocalStorage requestLocalStorage;
    private LocalStorageLifeCycle localStorageLifeCycle;
    private MockManager mockManager;

    public DefaultRoutingConnectionPoint(
            GrayManager grayManager,
            RequestLocalStorage requestLocalStorage,
            LocalStorageLifeCycle localStorageLifeCycle,
            MockManager mockManager) {
        this.grayManager = grayManager;
        this.requestLocalStorage = requestLocalStorage;
        this.localStorageLifeCycle = localStorageLifeCycle;
        this.mockManager = mockManager;
    }

    @Override
    public Object excuteMockHandle(RoutingConnectPointContext connectPointContext) {
        if (!mockManager.isEnableMock(HandleRuleType.MOCK_SERVER_CLIENT_REPSONSE.code())) {
            return null;
        }
        GrayReuqestMockInfo mockInfo = GrayReuqestMockInfo.builder()
                .grayRequest(connectPointContext.getGrayRequest())
                .build();
        return mockManager.predicateAndExcuteMockHandle(HandleRuleType.MOCK_SERVER_CLIENT_REPSONSE.code(), mockInfo);
    }

    @Override
    public void executeConnectPoint(RoutingConnectPointContext connectPointContext) {

        localStorageLifeCycle.initContext();

        RoutingConnectPointContext.setContextLocal(connectPointContext);
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
    public void shutdownconnectPoint(RoutingConnectPointContext connectPointContext) {
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
            RoutingConnectPointContext.removeContextLocal();
            requestLocalStorage.removeGrayRequest();
        } finally {
            localStorageLifeCycle.closeContext();
        }
    }


}
