package cn.springcloud.gray;

import cn.springcloud.gray.decision.GrayDecisionFactoryKeeper;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.model.DecisionDefinition;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayService;
import cn.springcloud.gray.model.PolicyDefinition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class SimpleGrayManager extends AbstractGrayManager {


    protected Map<String, GrayService> grayServices = new ConcurrentHashMap<>();
    protected Lock lock = new ReentrantLock();


    public SimpleGrayManager(GrayDecisionFactoryKeeper grayDecisionFactoryKeeper) {
        super(grayDecisionFactoryKeeper);
    }


    @Override
    public boolean hasGray(String serviceId) {
        GrayService grayService = grayServices.get(serviceId);
        return grayService != null && !grayService.getGrayInstances().isEmpty();
    }

    @Override
    public Collection<GrayService> allGrayServices() {
        return Collections.unmodifiableCollection(grayServices.values());
    }

    @Override
    public GrayService getGrayService(String serviceId) {
        return grayServices.get(serviceId);
    }

    @Override
    public void removeGrayService(String serviceId) {
        lock.lock();
        try {
            grayServices.remove(serviceId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void removePolicyDefinition(String serviceId, String instanceId, String policyId) {
        lock.lock();
        try {
            GrayInstance grayInstance = getGrayInstance(serviceId, instanceId);
            if (grayInstance == null) {
                log.error("removePolicyDefinition('{}', '{}', '{}') is not find gray instance",
                        serviceId, instanceId, policyId);
                return;
            }
            PolicyDefinition record = getPolicyDefinition(
                    grayInstance.getPolicyDefinitions(), policyId);
            if (record != null) {
                grayInstance.getPolicyDefinitions().remove(record);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void updatePolicyDefinition(String serviceId, String instanceId, PolicyDefinition policyDefinition) {
        lock.lock();
        try {
            GrayInstance grayInstance = getGrayInstance(serviceId, instanceId);
            if (grayInstance == null) {
                log.error("updatePolicyDefinition('{}', '{}', '{}') is not find gray instance",
                        serviceId, instanceId, policyDefinition);
                return;
            }
            PolicyDefinition record = getPolicyDefinition(
                    grayInstance.getPolicyDefinitions(), policyDefinition.getPolicyId());
            if (record == null) {
                record = new PolicyDefinition();
                record.setPolicyId(policyDefinition.getPolicyId());
                grayInstance.getPolicyDefinitions().add(policyDefinition);
            }
            record.setList(new ArrayList<>(policyDefinition.getList()));
            record.setAlias(policyDefinition.getAlias());

        } finally {
            lock.unlock();
        }
    }

    private PolicyDefinition getPolicyDefinition(String serviceId, String instanceId, String policyId) {
        GrayInstance grayInstance = getGrayInstance(serviceId, instanceId);
        if (grayInstance == null) {
            log.error("getPolicyDefinition('{}', '{}', '{}') is not find gray instance",
                    serviceId, instanceId, policyId);
            return null;
        }
        return getPolicyDefinition(
                grayInstance.getPolicyDefinitions(), policyId);
    }


    @Override
    public void removeDecisionDefinition(String serviceId, String instanceId, String policyId, String decesionId) {
        PolicyDefinition policyDefinition = getPolicyDefinition(serviceId, instanceId, policyId);
        if (policyDefinition == null) {
            return;
        }
        DecisionDefinition decisionDefinition = getDecisionDefinition(policyDefinition.getList(), decesionId);
        if (decisionDefinition != null) {
            policyDefinition.getList().remove(decisionDefinition);
        }
    }

    @Override
    public void updateDecisionDefinition(
            String serviceId, String instanceId, String policyId, DecisionDefinition decisionDefinition) {
        PolicyDefinition policyDefinition = getPolicyDefinition(serviceId, instanceId, policyId);
        if (policyDefinition == null) {
            return;
        }
        DecisionDefinition definition = getDecisionDefinition(policyDefinition.getList(), decisionDefinition.getId());
        if (definition != null) {
            definition.setName(decisionDefinition.getName());
            definition.setInfos(decisionDefinition.getInfos());
        } else {
            policyDefinition.getList().add(decisionDefinition);
        }
    }


    @Override
    public GrayInstance getGrayInstance(String serviceId, String instanceId) {
        GrayService service = getGrayService(serviceId);
        return service != null ? service.getGrayInstance(instanceId) : null;
    }


    @Override
    public void updateGrayInstance(GrayInstance instance) {
        if (instance == null) {
            return;
        }
        lock.lock();
        try {
            //非灰度的实例，需删除掉
            if(!instance.isGray()){
                closeGray(instance);
                return;
            }
            updateGrayInstance(grayServices, instance);
        } finally {
            lock.unlock();
        }
    }

    protected void updateGrayInstance(Map<String, GrayService> grayServices, GrayInstance instance) {
        InstanceLocalInfo instanceLocalInfo = GrayClientHolder.getInstanceLocalInfo();
        if (instanceLocalInfo != null) {
            if (StringUtils.equals(instanceLocalInfo.getServiceId(), instance.getServiceId())) {
                return;
            }
        }

        GrayService service = grayServices.get(instance.getServiceId());
        if (service == null) {
            if (service == null) {
                service = new GrayService();
                service.setServiceId(instance.getServiceId());
                grayServices.put(service.getServiceId(), service);
            }
        }
        log.debug("添加灰度实例, serviceId:{}, instanceId:{}", instance.getServiceId(), instance.getInstanceId());
        service.setGrayInstance(instance);
    }

    @Override
    public void closeGray(GrayInstance instance) {
        closeGray(instance.getServiceId(), instance.getInstanceId());
    }

    @Override
    public void closeGray(String serviceId, String instanceId) {
        GrayService service = getGrayService(serviceId);
        if (service == null) {
            log.debug("没有找到灰度服务:{}, 所以无需删除实例:{} 的灰度状态", serviceId, instanceId);
            return;
        }
        log.debug("关闭实例的在灰度状态, serviceId:{}, instanceId:{}", serviceId, instanceId);
        lock.lock();
        try {
            service.removeGrayInstance(instanceId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setup() {

    }


    @Override
    public void shutdown() {

    }


    @Override
    public void setGrayServices(Object grayServices) {
        if (grayServices instanceof Map) {
            this.grayServices = (Map<String, GrayService>) grayServices;
        } else {
            throw new UnsupportedOperationException("setGrayServices(grayServices) 无法支持的参数类型");
        }
    }


    private PolicyDefinition getPolicyDefinition(List<PolicyDefinition> policyDefinitions, String policyId) {
        for (PolicyDefinition policyDefinition : policyDefinitions) {
            if (StringUtils.equals(policyDefinition.getPolicyId(), policyId)) {
                return policyDefinition;
            }
        }
        return null;
    }

    private DecisionDefinition getDecisionDefinition(List<DecisionDefinition> decisionDefinitions, String decisionId) {
        for (DecisionDefinition decisionDefinition : decisionDefinitions) {
            if (StringUtils.equals(decisionDefinition.getId(), decisionId)) {
                return decisionDefinition;
            }
        }
        return null;
    }
}
