package cn.springcloud.gray.server.resources.domain.fo;

import cn.springcloud.gray.server.module.domain.HandleRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-05-31 01:18
 */
@Data
@ApiModel
public class HandleRuleFO {

    @ApiModelProperty("id")
    private Long id;
    private String namespace;
    @ApiModelProperty("规则类型，区分处理规则")
    private String type;
    @ApiModelProperty("模块id，用于搜索索引，如果serviceId")
    private String moduleId;
    @ApiModelProperty("路由的资源，如：serviceId, instanceId, service version")
    private String resource;
    @ApiModelProperty("匹配策略id列表")
    private Long[] matchingPolicyIds;
    @ApiModelProperty("处理选项")
    private String handleOption;
    @ApiModelProperty("排序字段，值越小，优先级越高")
    private int order;


    public HandleRule toHandleRule() {
        HandleRule handleRule = new HandleRule();
        handleRule.setId(id);
        fillToHandleRule(handleRule);
        return handleRule;
    }


    public void fillToHandleRule(HandleRule handleRule) {
        handleRule.setHandleOption(handleOption);
        handleRule.setMatchingPolicyIds(matchingPolicyIds);
        handleRule.setModuleId(moduleId);
        handleRule.setResource(resource);
        handleRule.setOrder(order);
        handleRule.setType(type);
    }


}
