package cn.springcloud.gray.server.module.gray.jpa;

import cn.springcloud.gray.server.module.gray.InstanceRouteModule;
import cn.springcloud.gray.server.module.gray.domain.InstanceRoutePolicies;
import cn.springcloud.gray.server.module.gray.domain.InstanceRoutePolicy;
import cn.springcloud.gray.server.module.gray.domain.query.InstanceRoutePolicyQuery;
import cn.springcloud.gray.server.service.InstanceRoutePolicyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-03-22 09:27
 */
public class JPAInstanceRouteModule implements InstanceRouteModule {

    private InstanceRoutePolicyService instanceRoutePolicyService;

    public JPAInstanceRouteModule(InstanceRoutePolicyService instanceRoutePolicyService) {
        this.instanceRoutePolicyService = instanceRoutePolicyService;
    }

    @Override
    public Page<InstanceRoutePolicy> queryInstanceRoutePolicies(InstanceRoutePolicyQuery query, Pageable pageable) {
        return instanceRoutePolicyService.queryInstanceRoutePolicies(query, pageable);
    }

    @Override
    public InstanceRoutePolicy saveInstanceRoutePolicy(InstanceRoutePolicy instanceRoutePolicy) {
        return instanceRoutePolicyService.saveModel(instanceRoutePolicy);
    }

    @Override
    public void updateInstanceRoutePolicyDelFlag(String id, boolean newDelFlag, String userId) {
        InstanceRoutePolicy record = getInstanceRoutePolicy(id);
        if (Objects.isNull(record)) {
            return;
        }
        if (Objects.equals(record.getDelFlag(), newDelFlag)) {
            return;
        }
        record.setDelFlag(newDelFlag);
        record.setOperator(userId);
        record.setOperateTime(new Date());
        instanceRoutePolicyService.saveModel(record);
        //todo 推送事件
    }

    @Override
    public int saveInstanceRoutePolicies(InstanceRoutePolicies instanceRoutePolicies) {

        return 0;
    }

    @Override
    public InstanceRoutePolicy getInstanceRoutePolicy(String id) {
        return instanceRoutePolicyService.findOneModel(id);
    }

    @Override
    public List<InstanceRoutePolicy> findAllRoutePoliciesByInstanceId(String instanceId, Boolean delFlag) {
        return instanceRoutePolicyService.findAllRoutePoliciesByInstanceId(instanceId, delFlag);
    }
}
