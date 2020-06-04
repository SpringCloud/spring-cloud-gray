package cn.springcloud.gray.server.module.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author saleson
 * @date 2020-05-30 22:31
 */
@Data
@ApiModel
public class HandleRule {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("命名空间")
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
    private Integer order;
    @ApiModelProperty("是否删除")
    private Boolean delFlag;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作时间")
    private Date operateTime;
}
