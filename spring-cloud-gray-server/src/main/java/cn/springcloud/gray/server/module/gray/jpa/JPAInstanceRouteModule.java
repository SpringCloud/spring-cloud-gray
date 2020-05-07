package cn.springcloud.gray.server.module.gray.jpa;

import cn.springcloud.gray.server.module.gray.GrayServerModule;
import cn.springcloud.gray.server.module.gray.InstanceRouteModule;
import cn.springcloud.gray.server.module.gray.domain.InstanceRoutePolicies;
import cn.springcloud.gray.server.module.gray.domain.InstanceRoutePolicy;
import cn.springcloud.gray.server.module.gray.domain.query.InstanceRoutePolicyQuery;
import cn.springcloud.gray.server.service.InstanceRoutePolicyService;
import cn.springlcoud.gray.event.server.GrayEventTrigger;
import cn.springlcoud.gray.event.server.TriggerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-03-22 09:27
 */
public class JPAInstanceRouteModule implements InstanceRouteModule {

    private InstanceRoutePolicyService instanceRoutePolicyService;
    private GrayServerModule grayServerModule;
    private GrayEventTrigger grayEventTrigger;

    public JPAInstanceRouteModule(
            InstanceRoutePolicyService instanceRoutePolicyService,
            GrayServerModule grayServerModule,
            GrayEventTrigger grayEventTrigger) {
        this.instanceRoutePolicyService = instanceRoutePolicyService;
        this.grayServerModule = grayServerModule;
        this.grayEventTrigger = grayEventTrigger;
    }

    @Override
    public Page<InstanceRoutePolicy> queryInstanceRoutePolicies(InstanceRoutePolicyQuery query, Pageable pageable) {
        return instanceRoutePolicyService.queryInstanceRoutePolicies(query, pageable);
    }

    @Override
    public InstanceRoutePolicy addInstanceRoutePolicy(String instanceId, Long policyId, String operatorId) {
        InstanceRoutePolicy instanceRoutePolicy = createInstanceRoutePolicy(instanceId, policyId, operatorId);
        int state = saveInstanceRoutePolicy(instanceRoutePolicy);
        if (state > 0) {
            triggerGrayEvent(TriggerType.ADD, instanceRoutePolicy);
        }
        return instanceRoutePolicy;
    }


    @Override
    public void updateInstanceRoutePolicyDelFlag(String id, boolean newDelFlag, String userId) {
        InstanceRoutePolicy record = getInstanceRoutePolicy(id);
        if (Objects.isNull(record) || Objects.equals(record.getDelFlag(), newDelFlag)) {
            return;
        }
        record.setDelFlag(newDelFlag);
        record.setOperator(userId);
        record.setOperateTime(new Date());
        instanceRoutePolicyService.saveModel(record);

        TriggerType triggerType = newDelFlag ? TriggerType.DELETE : TriggerType.MODIFY;
        triggerGrayEvent(triggerType, record);
    }

    @Transactional
    @Override
    public int saveInstanceRoutePolicies(InstanceRoutePolicies instanceRoutePolicies) {
        int count = 0;
        List<InstanceRoutePolicy> instanceRoutePolicyList = new ArrayList<>();
        for (String instanceId : instanceRoutePolicies.getInstanceIds()) {
            for (Long policyId : instanceRoutePolicies.getPolicyIds()) {
                InstanceRoutePolicy instanceRoutePolicy =
                        createInstanceRoutePolicy(instanceId, policyId, instanceRoutePolicies.getOperator());
                int state = saveInstanceRoutePolicy(instanceRoutePolicy);
                if (state > 0) {
                    instanceRoutePolicyList.add(instanceRoutePolicy);
                }
                count++;
            }
        }
        instanceRoutePolicyList.forEach(record -> triggerGrayEvent(TriggerType.ADD, record));
        return count;
    }

    @Override
    public InstanceRoutePolicy getInstanceRoutePolicy(String id) {
        return instanceRoutePolicyService.findOneModel(id);
    }

    @Override
    public List<InstanceRoutePolicy> findAllRoutePoliciesByInstanceId(String instanceId, Boolean delFlag) {
        return instanceRoutePolicyService.findAllRoutePoliciesByInstanceId(instanceId, delFlag);
    }


    protected void triggerGrayEvent(TriggerType triggerType, InstanceRoutePolicy instanceRoutePolicy) {
        if (grayServerModule.isActiveGrayInstance(instanceRoutePolicy.getInstanceId())) {
            triggerEvent(TriggerType.MODIFY, instanceRoutePolicy);
        }
    }

    protected void triggerEvent(TriggerType triggerType, Object source) {
        grayEventTrigger.triggering(source, triggerType);
    }


    /**
     * @param instanceRoutePolicy 返回时会补全Id
     * @return 0: 有纪录，且原纪录未删除, 1: 原先纪录, 2: 有纪录，且原纪录已删除，并已恢复
     */
    private int saveInstanceRoutePolicy(InstanceRoutePolicy instanceRoutePolicy) {
        InstanceRoutePolicy record = instanceRoutePolicyService.findByInstanceAndPolicy(
                instanceRoutePolicy.getInstanceId(), instanceRoutePolicy.getPolicyId());

        if (Objects.nonNull(record)) {
            instanceRoutePolicy.setId(record.getId());
        }
        InstanceRoutePolicy newRecord = instanceRoutePolicyService.saveModel(instanceRoutePolicy);
        instanceRoutePolicy.setId(newRecord.getId());

        if (Objects.isNull(record)) {
            return 1;
        }
        if (Objects.equals(record.getDelFlag(), true)) {
            return 2;
        }
        return 0;
    }

    private InstanceRoutePolicy createInstanceRoutePolicy(String instanceId, Long policyId, String operatorId) {
        InstanceRoutePolicy record = new InstanceRoutePolicy();
        record.setOperateTime(new Date());
        record.setOperator(operatorId);
        record.setDelFlag(false);
        record.setInstanceId(instanceId);
        record.setPolicyId(policyId);
        return record;
    }

}
