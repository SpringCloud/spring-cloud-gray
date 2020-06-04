package cn.springcloud.gray.server.resources.domain.fo;

import cn.springcloud.gray.server.module.domain.DelFlag;
import cn.springcloud.gray.server.module.domain.query.HandleRuleQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-05-31 00:31
 */
@Data
public class HandleRuleQueryFO {

    @ApiModelProperty("命名空间")
    private String namespace;
    @ApiModelProperty("规则类型，区分处理规则")
    private String type;
    @ApiModelProperty("模块id，用于搜索索引，如果serviceId")
    private String moduleId;
    @ApiModelProperty("路由的资源，如：serviceId, instanceId, service version")
    private String resource;
    @ApiModelProperty("处理选项")
    private String handleOption;
    @ApiModelProperty("删除标记")
    private DelFlag delFlag = DelFlag.UNDELETE;


    public HandleRuleQuery toHandleRuleQuery() {
        return new HandleRuleQuery()
                .setDelFlag(delFlag)
                .setNamespace(namespace)
                .setHandleOption(handleOption)
                .setModuleId(moduleId)
                .setResource(resource)
                .setType(type);
    }
}
