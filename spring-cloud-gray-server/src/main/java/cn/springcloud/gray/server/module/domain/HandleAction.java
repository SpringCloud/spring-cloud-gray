package cn.springcloud.gray.server.module.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author saleson
 * @date 2020-05-30 22:27
 */
@Data
@ApiModel
public class HandleAction {


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
    @ApiModelProperty("是否删除")
    private Boolean delFlag;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作时间")
    private Date operateTime;
}
