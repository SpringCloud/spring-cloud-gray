package cn.springcloud.gray.communication;

import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayTrackDefinition;

import java.util.List;


/**
 * InformationClientDecorator是一个适配器类
 */
public abstract class InformationClientDecorator implements InformationClient {


    public enum RequestType {
        ADD_GRAY_INSTANCE,
        SERVICE_DOWNLINE,
        ALL_GRAY_INSTANCES,
        GET_GRAY_INSTANCE,
        GET_TRACK_DEFINITIONS,
    }


    public interface RequestExecutor<R> {
        R execute(InformationClient delegate);

        RequestType getRequestType();
    }


    protected abstract <R> R execute(RequestExecutor<R> requestExecutor);

    @Override
    public GrayInstance getGrayInstance(String serviceId, String instanceId) {
        return execute(new RequestExecutor<GrayInstance>() {
            @Override
            public GrayInstance execute(InformationClient delegate) {
                return delegate.getGrayInstance(serviceId, instanceId);
            }

            @Override
            public RequestType getRequestType() {
                return RequestType.GET_GRAY_INSTANCE;
            }
        });
    }

    @Override
    public List<GrayInstance> allGrayInstances() {
        return execute(new RequestExecutor<List<GrayInstance>>() {
            @Override
            public List<GrayInstance> execute(InformationClient delegate) {
                return delegate.allGrayInstances();
            }

            @Override
            public RequestType getRequestType() {
                return RequestType.ALL_GRAY_INSTANCES;
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
                return RequestType.ADD_GRAY_INSTANCE;
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
                return RequestType.SERVICE_DOWNLINE;
            }
        });
    }

    @Override
    public List<GrayTrackDefinition> getTrackDefinitions(String serviceId, String instanceId) {
        return execute(new RequestExecutor<List<GrayTrackDefinition>>() {
            @Override
            public List<GrayTrackDefinition> execute(InformationClient delegate) {
                return delegate.getTrackDefinitions(serviceId, instanceId);
            }

            @Override
            public RequestType getRequestType() {
                return RequestType.GET_TRACK_DEFINITIONS;
            }
        });
    }
}
