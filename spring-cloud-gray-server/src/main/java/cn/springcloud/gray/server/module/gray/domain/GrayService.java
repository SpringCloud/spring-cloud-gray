package cn.springcloud.gray.server.module.gray.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;


@Setter
@Getter
@Builder
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class GrayService {

    @ApiModelProperty("服务id")
    private String serviceId;
    @ApiModelProperty("服务名称")
    private String serviceName;
    @ApiModelProperty("服务的servlet.context-path")
    private String contextPath;
    @ApiModelProperty("服务实例个数")
    private Integer instanceNumber;
    @ApiModelProperty("灰度实例个数")
    private Integer grayInstanceNumber;
    @ApiModelProperty("服务描述")
    private String describe;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作时间")
    private Date operateTime;

}
