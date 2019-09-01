package cn.springcloud.gray;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.GrayDecisionFactoryKeeper;
import cn.springcloud.gray.model.DecisionDefinition;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayService;
import cn.springcloud.gray.model.PolicyDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CachedGrayManager extends SimpleGrayManager implements CacheableGrayManager {

    protected Cache<String, List<GrayDecision>> grayDecisionCache;


    public CachedGrayManager(
            GrayDecisionFactoryKeeper grayDecisionFactoryKeeper,
            Cache<String, List<GrayDecision>> grayDecisionCache) {
        super(grayDecisionFactoryKeeper);
        this.grayDecisionCache = grayDecisionCache;
    }


    @Override
    public List<GrayDecision> getGrayDecision(GrayInstance instance) {
        return getCacheGrayDecision(instance.getInstanceId(), () -> super.getGrayDecision(instance));
    }

    @Override
    public List<GrayDecision> getGrayDecision(String serviceId, String instanceId) {
        return getCacheGrayDecision(instanceId, () -> super.getGrayDecision(getGrayInstance(serviceId, instanceId)));
    }


    @Override
    public Cache<String, List<GrayDecision>> getGrayDecisionCache() {
        return grayDecisionCache;
    }


    public void removeGrayService(String serviceId) {
        GrayService grayService = getGrayService(serviceId);
        super.removeGrayService(serviceId);
        if(grayService!=null){
            grayService.getGrayInstances().forEach(
                    grayInstance -> invalidateCache(grayInstance.getServiceId(), grayInstance.getInstanceId()));
        }
    }

    @Override
    public void removePolicyDefinition(String serviceId, String instanceId, String policyId) {
        super.removePolicyDefinition(serviceId, instanceId, policyId);
        invalidateCache(serviceId, instanceId);
    }

    @Override
    public void updatePolicyDefinition(String serviceId, String instanceId, PolicyDefinition policyDefinition) {
        super.updatePolicyDefinition(serviceId, instanceId, policyDefinition);
        invalidateCache(serviceId, instanceId);
    }


    @Override
    public void removeDecisionDefinition(String serviceId, String instanceId, String policyId, String decesionId) {
        super.removeDecisionDefinition(serviceId, instanceId, policyId, decesionId);
        invalidateCache(serviceId, instanceId);
    }

    @Override
    public void updateDecisionDefinition(
            String serviceId, String instanceId, String policyId, DecisionDefinition decisionDefinition) {
        super.updateDecisionDefinition(serviceId, instanceId, policyId, decisionDefinition);
        invalidateCache(serviceId, instanceId);
    }


    @Override
    public void updateGrayInstance(GrayInstance instance) {
        super.updateGrayInstance(instance);
        invalidateCache(instance.getServiceId(), instance.getInstanceId());
    }

    @Override
    public void closeGray(GrayInstance instance) {
        super.closeGray(instance);
        invalidateCache(instance.getServiceId(), instance.getInstanceId());
    }

    @Override
    public void closeGray(String serviceId, String instanceId) {
        super.closeGray(serviceId, instanceId);
        invalidateCache(serviceId, instanceId);
    }


    @Override
    public void setGrayServices(Object grayServices) {
        super.setGrayServices(grayServices);
        grayDecisionCache.invalidateAll();
    }


    private List<GrayDecision> getCacheGrayDecision(String key, Supplier<List<GrayDecision>> supplier) {
        return grayDecisionCache.get(key, k -> supplier.get());
    }

    private void invalidateCache(String serviceId, String instanceId) {
        grayDecisionCache.invalidate(instanceId);
    }
}
