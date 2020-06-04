package cn.springcloud.gray.server.module.domain.query;

import cn.springcloud.gray.server.module.domain.DelFlag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author saleson
 * @date 2020-05-31 00:31
 */
@Data
@Accessors(chain = true)
public class HandleRuleQuery {

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
    @ApiModelProperty("是否删除")
    private DelFlag delFlag;
}
