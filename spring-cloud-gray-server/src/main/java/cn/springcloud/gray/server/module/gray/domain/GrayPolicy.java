package cn.springcloud.gray.server.module.gray.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrayPolicy {

    private Long id;
    @ApiModelProperty("命名空间")
    private String namespace;
    @ApiModelProperty("策略别名")
    private String alias;
    @ApiModelProperty("是否删除")
    private Boolean delFlag;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作时间")
    private Date operateTime;
}
