package cn.springcloud.gray.handle;

import cn.springcloud.gray.model.HandleRuleDefinition;

import java.util.List;

/**
 * @author saleson
 * @date 2020-05-24 22:19
 */
public interface HandleRuleManager {

    List<HandleRuleInfo> getHandleRuleTypeInfos(String handleRuleType);


    HandleRuleInfo removeHandleRuleInfo(String handleRuleId);


    HandleRuleInfo getHandleInfo(String handleRuleId);


    void clearHandleRuleInfos();


    void putHandleRuleInfo(HandleRuleInfo handleRuleInfo);

    void putHandleRuleInfos(HandleRuleInfo... handleRuleInfos);


    void putHandleRuleDefinition(HandleRuleDefinition handleRuleDefinition);

    void putHandleRuleDefinitions(HandleRuleDefinition... handleRuleDefinitions);


    boolean addHandleRuleRoutePolicy(String handleRuleId, String policyId);


    boolean removeHandleRuleRoutePolicy(String handleRuleId, String policyId);

}
