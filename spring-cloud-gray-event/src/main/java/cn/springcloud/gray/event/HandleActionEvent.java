package cn.springcloud.gray.event;

import cn.springcloud.gray.model.HandleActionDefinition;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-05-31 15:30
 */
@Data
public class HandleActionEvent extends GrayEvent {

    private HandleActionDefinition handleActionDefinition;
    private String handleId;

    @Override
    public String getSourceId() {
        return handleActionDefinition.getId();
    }


}
