package cn.springcloud.gray.server.module.route.policy.jpa;

import cn.springcloud.gray.function.Predicate3;
import cn.springcloud.gray.model.RoutePolicy;
import cn.springcloud.gray.server.exception.NonAuthorityException;
import cn.springcloud.gray.server.module.NamespaceFinder;
import cn.springcloud.gray.server.module.gray.domain.GrayModelType;
import cn.springcloud.gray.server.module.route.policy.RoutePolicyModule;
import cn.springcloud.gray.server.module.route.policy.domain.RoutePolicyRecord;
import cn.springcloud.gray.server.module.route.policy.domain.RouteResourcePolicies;
import cn.springcloud.gray.server.module.route.policy.domain.query.RoutePolicyQuery;
import cn.springcloud.gray.server.module.user.AuthorityModule;
import cn.springcloud.gray.server.service.RoutePolicyRecordService;
import cn.springcloud.gray.event.server.GrayEventTrigger;
import cn.springcloud.gray.event.server.TriggerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author saleson
 * @date 2020-03-22 09:27
 */
@Slf4j
public class JPARoutePolicyModule implements RoutePolicyModule {

    private RoutePolicyRecordService routePolicyRecordService;
    private GrayEventTrigger grayEventTrigger;
    private NamespaceFinder namespaceFinder;
    private AuthorityModule authorityModule;


    private Map<String, Predicate3<String, String, RoutePolicy>> authorityPredicate = new ConcurrentHashMap<>();


    public JPARoutePolicyModule(
            RoutePolicyRecordService routePolicyRecordService,
            GrayEventTrigger grayEventTrigger,
            NamespaceFinder namespaceFinder,
            AuthorityModule authorityModule) {
        this.routePolicyRecordService = routePolicyRecordService;
        this.grayEventTrigger = grayEventTrigger;
        this.namespaceFinder = namespaceFinder;
        this.authorityModule = authorityModule;
    }

    @Override
    public Page<RoutePolicyRecord> queryRoutePolicies(RoutePolicyQuery query, Pageable pageable) {
        return routePolicyRecordService.queryRoutePolicies(query, pageable);
    }

    @Override
    public List<RoutePolicyRecord> findAllRoutePolicies(RoutePolicyQuery query) {
        return routePolicyRecordService.queryRoutePolicies(query);
    }

    @Override
    public RoutePolicyRecord addRoutePolicy(String ns, RoutePolicy routePolicy, String operatorId) {
        RoutePolicyQuery query = RoutePolicyQuery.of(routePolicy);
        query.setNs(ns);
        RoutePolicyRecord routePolicyRecord = routePolicyRecordService.findFirstAscByDelFlag(query);
        if (Objects.isNull(routePolicyRecord)) {
            routePolicyRecord = createRoutePolicyRecord(ns, routePolicy, operatorId);
            int state = saveRoutePolicyRecord(routePolicyRecord);
            if (state > 0) {
                triggerGrayEvent(TriggerType.ADD, routePolicyRecord);
            }
            return routePolicyRecord;
        }

        if (Objects.isNull(routePolicyRecord.getDelFlag()) || routePolicyRecord.getDelFlag()) {
            updateRoutePolicyDelFlag(routePolicyRecord.getId(), false, operatorId);
            return routePolicyRecordService.findOneModel(routePolicyRecord.getId());
        }
        return routePolicyRecord;

    }

    @Override
    public void physicsDeleteRoutePolicy(String type, String moduleId, String resource) {
        RoutePolicyQuery irpQuery = RoutePolicyQuery.builder()
                .type(type)
                .moduleId(moduleId)
                .resource(resource)
                .build();
        List<RoutePolicyRecord> routePolicyRecords =
                Optional.ofNullable(routePolicyRecordService.queryRoutePolicies(irpQuery)).orElse(Collections.EMPTY_LIST);
        routePolicyRecordService.deleteModel(routePolicyRecords);
    }


    @Override
    public void updateRoutePolicyDelFlag(Long id, boolean newDelFlag, String operatorId) {
        RoutePolicyRecord record = getRoutePolicy(id);
        if (Objects.isNull(record) || Objects.equals(record.getDelFlag(), newDelFlag)) {
            return;
        }
        record.setDelFlag(newDelFlag);
        record.setOperator(operatorId);
        record.setOperateTime(new Date());
        routePolicyRecordService.saveModel(record);

        TriggerType triggerType = newDelFlag ? TriggerType.DELETE : TriggerType.MODIFY;
        triggerGrayEvent(triggerType, record);
    }


    @Transactional
    @Override
    public int saveRoutePolicies(RouteResourcePolicies routeResourcePolicies, String operatorId, boolean needAuthority) {
        int count = 0;
        List<RoutePolicyRecord> routePolicyRecordList = new ArrayList<>();
        for (String resource : routeResourcePolicies.getResources()) {
            for (Long policyId : routeResourcePolicies.getPolicyIds()) {
                RoutePolicyRecord routePolicyRecord = createRoutePolicyRecord(
                        routeResourcePolicies.getNamespace(), routeResourcePolicies.getType(), routeResourcePolicies.getModuleId(), resource, policyId, operatorId);
                if (needAuthority && !hasResourceAuthority(operatorId, routePolicyRecord)) {
                    throw new NonAuthorityException();
                }

                int state = saveRoutePolicyRecord(routePolicyRecord);
                if (state > 0) {
                    routePolicyRecordList.add(routePolicyRecord);
                }
                count++;
            }
        }
        routePolicyRecordList.forEach(record -> triggerGrayEvent(TriggerType.ADD, record));
        return count;
    }

    @Override
    public RoutePolicyRecord getRoutePolicy(Long id) {
        return routePolicyRecordService.findOneModel(id);
    }


    @Override
    public boolean hasResourceAuthority(String userId, RoutePolicyRecord routePolicyRecord) {
        return hasResourceAuthority(routePolicyRecord.getNamespace(), userId, routePolicyRecord.toRoutePolicy());
    }

    @Override
    public boolean hasResourceAuthority(String ns, String userId, RoutePolicy routePolicy) {
        if (!authorityModule.hasResourceAuthority(ns, userId)) {
            return false;
        }
        if (!Objects.equals(ns, namespaceFinder.getNamespaceCode(GrayModelType.POLICY, routePolicy.getPolicyId()))) {
            log.error("policy id '{}' 不属于 namespace -> '{}'", routePolicy.getPolicyId(), ns);
            return false;
        }

        Predicate3<String, String, RoutePolicy> authorityPredicate = getResourceAuthorityPredicate(routePolicy.getType());
        if (Objects.isNull(authorityPredicate)) {
            log.error("没有找到Type为'{}'的权限断言器", routePolicy.getType());
            return false;
        }
        return authorityPredicate.test(ns, userId, routePolicy);
    }


    @Override
    public void registerResourceAuthorityPredicate(String type, Predicate3<String, String, RoutePolicy> predicate) {
        authorityPredicate.put(type.toLowerCase(), predicate);
    }

    @Override
    public Predicate3<String, String, RoutePolicy> getResourceAuthorityPredicate(String type) {
        return authorityPredicate.get(type.toLowerCase());
    }


    protected void triggerGrayEvent(TriggerType triggerType, RoutePolicyRecord routePolicyRecord) {
        triggerEvent(triggerType, routePolicyRecord);
    }

    protected void triggerEvent(TriggerType triggerType, Object source) {
        grayEventTrigger.triggering(source, triggerType);
    }


    /**
     * @param routePolicyRecord 返回时会补全Id
     * @return 0: 有纪录，且原纪录未删除, 1: 原先纪录, 2: 有纪录，且原纪录已删除，并已恢复
     */
    private int saveRoutePolicyRecord(RoutePolicyRecord routePolicyRecord) {
        RoutePolicyRecord record = routePolicyRecordService.find(
                routePolicyRecord.getType(), routePolicyRecord.getModuleId(), routePolicyRecord.getResource(), routePolicyRecord.getPolicyId());

        if (Objects.nonNull(record)) {
            routePolicyRecord.setId(record.getId());
        }
        RoutePolicyRecord newRecord = routePolicyRecordService.saveModel(routePolicyRecord);
        routePolicyRecord.setId(newRecord.getId());

        if (Objects.isNull(record)) {
            return 1;
        }
        if (Objects.equals(record.getDelFlag(), true)) {
            return 2;
        }
        return 0;
    }


    private RoutePolicyRecord createRoutePolicyRecord(String ns, RoutePolicy routePolicy, String operatorId) {
        RoutePolicyRecord record = createRoutePolicyRecord(
                ns, routePolicy.getType(), routePolicy.getModuleId(), routePolicy.getResource(), routePolicy.getPolicyId(), operatorId);
        record.setId(routePolicy.getId());
        return record;
    }

    private RoutePolicyRecord createRoutePolicyRecord(
            String ns, String type, String moduleId, String resource, Long policyId, String operatorId) {
        return RoutePolicyRecord.builder()
                .type(type)
                .namespace(ns)
                .moduleId(moduleId)
                .resource(resource)
                .policyId(policyId)
                .operator(operatorId)
                .operateTime(new Date())
                .delFlag(false)
                .build();
    }


}
