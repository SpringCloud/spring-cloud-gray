package cn.springlcoud.gray.event;

import cn.springcloud.gray.model.HandleRuleDefinition;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-05-31 15:30
 */
@Data
public class HandleRuleEvent extends GrayEvent {

    private HandleRuleDefinition handleRuleDefinition;


    @Override
    public String getSourceId() {
        return handleRuleDefinition.getId();
    }


}
