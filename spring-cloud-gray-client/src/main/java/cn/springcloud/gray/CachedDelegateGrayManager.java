package cn.springcloud.gray;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.model.GrayInstance;

import java.util.List;
import java.util.function.Supplier;

public class CachedDelegateGrayManager extends GrayManagerDelegater implements CacheableGrayManager, CommunicableGrayManager {

    private Cache<String, List<GrayDecision>> grayDecisionCache;


    public CachedDelegateGrayManager(GrayManager delegate, Cache<String, List<GrayDecision>> grayDecisionCache) {
        super(delegate);
        this.grayDecisionCache = grayDecisionCache;
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
    public void setGrayServices(Object grayServices) {
        if (delegate instanceof UpdateableGrayManager) {
            ((UpdateableGrayManager) delegate).setGrayServices(grayServices);
            grayDecisionCache.invalidateAll();
        }
    }


    private List<GrayDecision> getCacheGrayDecision(String key, Supplier<List<GrayDecision>> supplier) {
        return grayDecisionCache.get(key, k -> supplier.get());
    }

    private void invalidateCache(String serviceId, String instanceId) {
        grayDecisionCache.invalidate(instanceId);
    }

}
