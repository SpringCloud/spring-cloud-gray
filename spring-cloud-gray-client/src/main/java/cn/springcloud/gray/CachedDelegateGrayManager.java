package cn.springcloud.gray;

import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayService;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class CachedDelegateGrayManager implements CacheableGrayManager, CommunicableGrayManager {

    private GrayManager delegate;
    private Cache<String, List<GrayDecision>> grayDecisionCache;


    public CachedDelegateGrayManager(GrayManager delegate, Cache<String, List<GrayDecision>> grayDecisionCache) {
        this.delegate = delegate;
        this.grayDecisionCache = grayDecisionCache;
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
    public List<GrayDecision> getGrayDecision(GrayInstance instance) {
        return getCacheGrayDecision(instance.getInstanceId(), () -> delegate.getGrayDecision(instance));
    }

    @Override
    public List<GrayDecision> getGrayDecision(String serviceId, String instanceId) {
        return getCacheGrayDecision(instanceId, () -> delegate.getGrayDecision(serviceId, instanceId));
    }


    @Override
    public Cache<String, List<GrayDecision>> getGrayDecisionCache() {
        return grayDecisionCache;
    }


    @Override
    public void updateGrayInstance(GrayInstance instance) {
        delegate.updateGrayInstance(instance);
        invalidateCache(instance.getServiceId(), instance.getInstanceId());
    }

    @Override
    public void closeGray(GrayInstance instance) {
        delegate.closeGray(instance);
        invalidateCache(instance.getServiceId(), instance.getInstanceId());
    }

    @Override
    public void closeGray(String serviceId, String instanceId) {
        delegate.closeGray(serviceId, instanceId);
        invalidateCache(serviceId, instanceId);
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
            grayDecisionCache.invalidateAll();
        }
    }

    @Override
    public void setRequestInterceptors(Collection<RequestInterceptor> requestInterceptors) {
        if (delegate instanceof UpdateableGrayManager) {
            ((UpdateableGrayManager) delegate).setRequestInterceptors(requestInterceptors);
        }
    }

    private List<GrayDecision> getCacheGrayDecision(String key, Supplier<List<GrayDecision>> supplier) {
        return grayDecisionCache.get(key, k -> supplier.get());
    }

    private void invalidateCache(String serviceId, String instanceId) {
        grayDecisionCache.invalidate(instanceId);
    }

    @Override
    public InformationClient getGrayInformationClient() {
        if (delegate instanceof CommunicableGrayManager) {
            return ((CommunicableGrayManager) delegate).getGrayInformationClient();
        }
        throw new UnsupportedOperationException("delegate不是CommunicableGrayManager的实现类");
    }
}
