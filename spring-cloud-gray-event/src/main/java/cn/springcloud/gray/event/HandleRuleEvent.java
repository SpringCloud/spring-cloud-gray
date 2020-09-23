package cn.springcloud.gray.event;

import cn.springcloud.gray.model.HandleRuleDefinition;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-05-31 15:30
 */
@Data
public class HandleRuleEvent extends GrayEvent {

    private static final long serialVersionUID = -5723492249160514681L;
    private HandleRuleDefinition handleRuleDefinition;

    private String moduleId;
    private String resource;

    @Override
    public String getSourceId() {
        return handleRuleDefinition.getId();
    }


}
