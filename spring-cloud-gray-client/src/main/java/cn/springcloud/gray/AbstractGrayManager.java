package cn.springcloud.gray;

import cn.springcloud.gray.client.GrayClientAppContext;
import cn.springcloud.gray.core.*;
import cn.springcloud.gray.decision.MultiGrayDecision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * GrayManager的抽象实现类实现了基础的获取灰度列表， 创建灰度决策对象的能力
 */
public abstract class AbstractGrayManager implements GrayManager {
    private static final Logger log = LoggerFactory.getLogger(AbstractGrayManager.class);

    protected GrayDecisionFactory decisionFactory;
    protected InformationClient client;


    public AbstractGrayManager(InformationClient client, GrayDecisionFactory decisionFactory) {
        this.decisionFactory = decisionFactory;
        this.client = client;
    }


    @Override
    public boolean isOpen(String serviceId) {
        GrayService grayService = grayService(serviceId);
        return grayService != null
                && grayService.isOpenGray();
    }

    @Override
    public List<GrayService> listGrayService() {
        return client.listGrayService();
    }

    @Override
    public GrayService grayService(String serviceId) {
        return client.grayService(serviceId);
    }

    @Override
    public GrayInstance grayInstance(String serviceId, String instanceId) {
        return client.grayInstance(serviceId, instanceId);
    }

    @Override
    public List<GrayDecision> grayDecision(GrayInstance instance) {
        return grayDecision(instance.getServiceId(), instance.getInstanceId());
    }

    @Override
    public List<GrayDecision> grayDecision(String serviceId, String instanceId) {
        GrayInstance grayInstance = grayInstance(serviceId, instanceId);
        if (grayInstance == null || !grayInstance.isOpenGray()
                || grayInstance.getGrayPolicyGroups() == null
                || grayInstance.getGrayPolicyGroups().isEmpty()) {
            return Collections.emptyList();
        }
        List<GrayPolicyGroup> policyGroups = grayInstance.getGrayPolicyGroups();
        List<GrayDecision> decisions = new ArrayList<>(policyGroups.size());
        for (GrayPolicyGroup policyGroup : policyGroups) {
            if (!policyGroup.isEnable()) {
                continue;
            }
            GrayDecision grayDecision = toGrayDecision(policyGroup);
            if (grayDecision != GrayDecision.refuse()) {
                decisions.add(grayDecision);
            }
        }
        return decisions;
    }

    @Override
    public void serviceDownline() {
        InstanceLocalInfo localInfo = GrayClientAppContext.getInstanceLocalInfo();
        if (localInfo.isGray()) {
            log.debug("灰度服务下线...");
            client.serviceDownline();
            log.debug("灰度服务下线完成");
        }
        serviceShutdown();
    }


    protected abstract void serviceShutdown();


    private GrayDecision toGrayDecision(GrayPolicyGroup policyGroup) {
        List<GrayPolicy> policies = policyGroup.getList();
        if (policies == null || policies.isEmpty()) {
            return GrayDecision.refuse();
        }
        MultiGrayDecision decision = new MultiGrayDecision(GrayDecision.allow());
        policies.forEach(policy -> decision.and(decisionFactory.getDecision(policy)));
        return decision;
    }


}
