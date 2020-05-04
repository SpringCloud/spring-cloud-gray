package cn.springcloud.gray.decision;

import cn.springcloud.gray.model.DecisionDefinition;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author saleson
 * @date 2020-04-07 22:48
 */
@Data
@Accessors(chain = true)
public class PolicyInfo {
    private String policyId;
    private String alias;
    private Map<String, DecisionDefinition> decisionDefinitions = new ConcurrentHashMap<>();


    public void setDecisionDefinition(DecisionDefinition decisionDefinition) {
        decisionDefinitions.put(decisionDefinition.getId(), decisionDefinition);
    }

    public DecisionDefinition removeDecisionDefinition(String decisionId) {
        return decisionDefinitions.remove(decisionId);
    }
}
