package cn.springcloud.gray.server.resources.domain.fo;

import cn.springcloud.gray.server.module.domain.HandleAction;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-05-31 01:18
 */
@Data
@ApiModel
public class HandleActionFO {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("handleId")
    private Long handleId;
    @ApiModelProperty("处理动作名称, 用于查找/创建处理动作")
    private String actionName;
    @ApiModelProperty("配置信息，json类型")
    private String infos;
    @ApiModelProperty("排序字段，值越小，优先级越高")
    private int order;


    public void fillToHandleAction(HandleAction handleAction) {
        handleAction.setActionName(actionName);
        handleAction.setHandleId(handleId);
        handleAction.setInfos(infos);
        handleAction.setOrder(order);
    }


}
