package cn.springcloud.gray.communication;

import cn.springcloud.gray.model.GrayInstance;

import java.util.List;


/**
 * InformationClientDecorator是一个适配器类
 */
public abstract class InformationClientDecorator implements InformationClient {


    public enum RequestType {
        AddGrayInstance,
        ServiceDownline,
        AllGrayInstances
    }


    public interface RequestExecutor<R> {
        R execute(InformationClient delegate);

        RequestType getRequestType();
    }


    protected abstract <R> R execute(RequestExecutor<R> requestExecutor);


    @Override
    public List<GrayInstance> allGrayInstances() {
        return execute(new RequestExecutor<List<GrayInstance>>() {
            @Override
            public List<GrayInstance> execute(InformationClient delegate) {
                return delegate.allGrayInstances();
            }

            @Override
            public RequestType getRequestType() {
                return RequestType.AllGrayInstances;
            }
        });
    }


    @Override
    public void addGrayInstance(GrayInstance grayInstance) {

        execute(new RequestExecutor<Object>() {
            @Override
            public Object execute(InformationClient delegate) {
                delegate.addGrayInstance(grayInstance);
                return null;
            }

            @Override
            public RequestType getRequestType() {
                return RequestType.AddGrayInstance;
            }
        });
    }


    @Override
    public void serviceDownline(String instanceId) {
        execute(new RequestExecutor<Object>() {
            @Override
            public Object execute(InformationClient delegate) {
                delegate.serviceDownline(instanceId);
                return null;
            }

            @Override
            public RequestType getRequestType() {
                return RequestType.ServiceDownline;
            }
        });
    }
}
