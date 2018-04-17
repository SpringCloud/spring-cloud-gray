package cn.springcloud.gray.core;

import java.util.List;


/**
 * InformationClientDecorator是一个适配器类
 */
public abstract class InformationClientDecorator implements InformationClient {


    public enum RequestType {
        AddGrayInstance,
        ServiceDownline,
        GetGrayInstance,
        GetGrayService,
        ListGrayServices
    }


    public interface RequestExecutor<R> {
        R execute(InformationClient delegate);

        RequestType getRequestType();
    }


    protected abstract <R> R execute(RequestExecutor<R> requestExecutor);


    @Override
    public List<GrayService> listGrayService() {
        return execute(new RequestExecutor<List<GrayService>>() {
            @Override
            public List<GrayService> execute(InformationClient delegate) {
                return delegate.listGrayService();
            }

            @Override
            public RequestType getRequestType() {
                return RequestType.ListGrayServices;
            }
        });
    }

    @Override
    public GrayService grayService(String serviceId) {

        return execute(new RequestExecutor<GrayService>() {
            @Override
            public GrayService execute(InformationClient delegate) {
                return delegate.grayService(serviceId);
            }

            @Override
            public RequestType getRequestType() {
                return RequestType.GetGrayService;
            }
        });

    }

    @Override
    public GrayInstance grayInstance(String serviceId, String instanceId) {
        return execute(new RequestExecutor<GrayInstance>() {
            @Override
            public GrayInstance execute(InformationClient delegate) {
                return delegate.grayInstance(serviceId, instanceId);
            }

            @Override
            public RequestType getRequestType() {
                return RequestType.GetGrayInstance;
            }
        });
    }

    @Override
    public void addGrayInstance(String serviceId, String instanceId) {

        execute(new RequestExecutor<Object>() {
            @Override
            public Object execute(InformationClient delegate) {
                delegate.addGrayInstance(serviceId, instanceId);
                return null;
            }

            @Override
            public RequestType getRequestType() {
                return RequestType.AddGrayInstance;
            }
        });
    }

    @Override
    public void serviceDownline() {
        execute(new RequestExecutor<Object>() {
            @Override
            public Object execute(InformationClient delegate) {
                delegate.serviceDownline();
                return null;
            }

            @Override
            public RequestType getRequestType() {
                return RequestType.ServiceDownline;
            }
        });
    }

    @Override
    public void serviceDownline(String serviceId, String instanceId) {
        execute(new RequestExecutor<Object>() {
            @Override
            public Object execute(InformationClient delegate) {
                delegate.serviceDownline(serviceId, instanceId);
                return null;
            }

            @Override
            public RequestType getRequestType() {
                return RequestType.ServiceDownline;
            }
        });
    }
}
