package cn.springlcoud.gray.event;

import cn.springcloud.gray.model.DecisionDefinition;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-02-03 12:15
 */
@Data
public class GrayDecisionEvent extends GrayEvent<DecisionDefinition> {

    private String serviceId;
    private String instanceId;
    private String policyId;

    @Override
    public String getSourceId() {
        return getSource().getId();
    }


}
