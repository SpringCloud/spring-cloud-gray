package cn.springcloud.gray;

import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayService;

import java.util.Collection;
import java.util.List;

public abstract class GrayManagerDelegater implements UpdateableGrayManager, CommunicableGrayManager {

    protected GrayManager delegate;

    public GrayManagerDelegater(GrayManager delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean hasGray(String serviceId) {
        return delegate.hasGray(serviceId);
    }

    @Override
    public Collection<GrayService> allGrayServices() {
        return delegate.allGrayServices();
    }

    @Override
    public GrayService getGrayService(String serviceId) {
        return delegate.getGrayService(serviceId);
    }

    @Override
    public GrayInstance getGrayInstance(String serviceId, String instanceId) {
        return delegate.getGrayInstance(serviceId, instanceId);
    }


    @Override
    public void updateGrayInstance(GrayInstance instance) {
        delegate.updateGrayInstance(instance);
    }

    @Override
    public void closeGray(GrayInstance instance) {
        delegate.closeGray(instance);
    }

    @Override
    public void closeGray(String serviceId, String instanceId) {
        delegate.closeGray(serviceId, instanceId);
    }

    @Override
    public List<RequestInterceptor> getRequeestInterceptors(String interceptroType) {
        return delegate.getRequeestInterceptors(interceptroType);
    }

    @Override
    public void setup() {
        delegate.setup();
    }

    @Override
    public void shutdown() {
        delegate.shutdown();
    }

    @Override
    public void setGrayServices(Object grayServices) {
        if (delegate instanceof UpdateableGrayManager) {
            ((UpdateableGrayManager) delegate).setGrayServices(grayServices);
        }
    }

    @Override
    public void setRequestInterceptors(Collection<RequestInterceptor> requestInterceptors) {
        if (delegate instanceof UpdateableGrayManager) {
            ((UpdateableGrayManager) delegate).setRequestInterceptors(requestInterceptors);
        }
    }

    @Override
    public InformationClient getGrayInformationClient() {
        if (delegate instanceof CommunicableGrayManager) {
            return ((CommunicableGrayManager) delegate).getGrayInformationClient();
        }
        throw new UnsupportedOperationException("delegate不是CommunicableGrayManager的实现类");
    }


    public void removeGrayService(String serviceId) {
        if (delegate instanceof UpdateableGrayManager) {
            ((UpdateableGrayManager) delegate).removeGrayService(serviceId);
        }
    }

}
