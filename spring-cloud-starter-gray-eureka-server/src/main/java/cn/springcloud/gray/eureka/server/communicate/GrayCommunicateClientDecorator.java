package cn.springcloud.gray.eureka.server.communicate;

import cn.springcloud.gray.model.InstanceInfo;


/**
 * GrayCommunicateClientDecorator是一个适配器类
 */
public abstract class GrayCommunicateClientDecorator implements GrayCommunicateClient {


    public enum RequestType {
        NOTICE_INSTANCE_INFO
    }


    public interface RequestExecutor<R> {
        R execute(GrayCommunicateClient delegate);

        RequestType getRequestType();
    }


    protected abstract <R> R execute(RequestExecutor<R> requestExecutor);


    public void noticeInstanceInfo(InstanceInfo instanceInfo) {
        execute(new RequestExecutor<Object>() {
            @Override
            public Object execute(GrayCommunicateClient delegate) {
                delegate.noticeInstanceInfo(instanceInfo);
                return null;
            }

            @Override
            public RequestType getRequestType() {
                return RequestType.NOTICE_INSTANCE_INFO;
            }
        });
    }
}
