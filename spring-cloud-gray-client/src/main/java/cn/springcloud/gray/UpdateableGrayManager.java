package cn.springcloud.gray;

import java.util.Collection;

public interface UpdateableGrayManager extends GrayManager {

    void setGrayServices(Object grayServices);

    void setRequestInterceptors(Collection<RequestInterceptor> requestInterceptors);


}
