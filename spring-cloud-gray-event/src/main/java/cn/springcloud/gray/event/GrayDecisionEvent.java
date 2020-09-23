package cn.springcloud.gray.event;

import cn.springcloud.gray.model.DecisionDefinition;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-02-03 12:15
 */
@Data
public class GrayDecisionEvent extends GrayEvent {

    private String policyId;
    private DecisionDefinition source;

    @Override
    public String getSourceId() {
        return getSource().getId();
    }


}
