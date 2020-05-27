package cn.springcloud.gray.handle;

import cn.springcloud.gray.model.HandleRuleDefinition;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-05-26 00:46
 */
public class DefaultHandleRuleManager implements HandleRuleManager {

    private Map<String, List<HandleRuleInfo>> handleRuleTypeInfos = new ConcurrentHashMap<>();

    private Map<String, HandleRuleInfo> handleRuleInfos = new ConcurrentHashMap<>();


    @Override
    public List<HandleRuleInfo> getHandleRuleTypeInfos(String handleRuleType) {
        List<HandleRuleInfo> handleRuleInfos = handleRuleTypeInfos.get(handleRuleType);
        if (Objects.nonNull(handleRuleInfos)) {
            return handleRuleInfos;
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public synchronized HandleRuleInfo removeHandleRuleInfo(String handleRuleId) {
        HandleRuleInfo handleRuleInfo = handleRuleInfos.remove(handleRuleId);
        if (Objects.nonNull(handleRuleInfo)) {
            resetHandleRuleInfos(handleRuleInfo.getType());
        }
        return handleRuleInfo;
    }

    @Override
    public HandleRuleInfo getHandleInfo(String handleRuleId) {
        return handleRuleInfos.get(handleRuleId);
    }

    @Override
    public synchronized void clearHandleRuleInfos() {
        handleRuleTypeInfos.clear();
        handleRuleInfos.clear();
    }

    @Override
    public synchronized void addHandleRuleInfo(HandleRuleInfo handleRuleInfo) {
        handleRuleInfos.put(handleRuleInfo.getId(), handleRuleInfo);
        resetHandleRuleInfos(handleRuleInfo.getType());
    }

    @Override
    public synchronized void addHandleRuleInfos(HandleRuleInfo... handleRuleInfos) {
        Set<String> types = new HashSet<>();
        for (HandleRuleInfo handleRuleInfo : handleRuleInfos) {
            types.add(handleRuleInfo.getType());
            this.handleRuleInfos.put(handleRuleInfo.getId(), handleRuleInfo);
        }
        types.forEach(this::resetHandleRuleInfos);
    }

    @Override
    public void addHandleRuleDefinition(HandleRuleDefinition handleRuleDefinition) {
        addHandleRuleInfo(toHandleRuleInfo(handleRuleDefinition));
    }

    @Override
    public void addHandleRuleDefinitions(HandleRuleDefinition... handleRuleDefinitions) {
        HandleRuleInfo[] handleRuleInfos = new HandleRuleInfo[handleRuleDefinitions.length];
        for (int i = 0; i < handleRuleDefinitions.length; i++) {
            handleRuleInfos[i] = toHandleRuleInfo(handleRuleDefinitions[i]);
        }
        addHandleRuleInfos(handleRuleInfos);
    }

    @Override
    public boolean addHandleRuleRoutePolicy(String handleRuleId, String policyId) {
        HandleRuleInfo handleRuleInfo = handleRuleInfos.remove(handleRuleId);
        if (Objects.isNull(handleRuleInfo)) {
            return false;
        }
        return handleRuleInfo.getRoutePolicies().addData(policyId);
    }

    @Override
    public boolean removeHandleRuleRoutePolicy(String handleRuleId, String policyId) {
        HandleRuleInfo handleRuleInfo = handleRuleInfos.remove(handleRuleId);
        if (Objects.isNull(handleRuleInfo)) {
            return false;
        }
        return handleRuleInfo.getRoutePolicies().removeData(policyId);
    }


    private void resetHandleRuleInfos(String handleRuleType) {
        List<HandleRuleInfo> handleRuleInfos = this.handleRuleInfos.values()
                .stream()
                .filter(info -> Objects.equals(handleRuleType, info.getType()))
                .sorted()
                .collect(Collectors.toList());
        if (!handleRuleInfos.isEmpty()) {
            handleRuleTypeInfos.put(handleRuleType, handleRuleInfos);
        }
    }


    private HandleRuleInfo toHandleRuleInfo(HandleRuleDefinition handleRuleDefinition) {
        HandleRuleInfo handleRuleInfo = new HandleRuleInfo(handleRuleDefinition.getId());
        handleRuleInfo.setType(handleRuleDefinition.getType());
        handleRuleInfo.setHandleInfo(handleRuleDefinition.getHandleInfo());
        handleRuleInfo.getRoutePolicies().addDatas(handleRuleDefinition.getPolicyIds());
        handleRuleInfo.setOrder(handleRuleDefinition.getOrder());
        return handleRuleInfo;
    }


}
