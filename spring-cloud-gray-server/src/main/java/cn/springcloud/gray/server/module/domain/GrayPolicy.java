package cn.springcloud.gray.server.module.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrayPolicy {

    private Long id;
    @ApiModelProperty("服务实例id")
    private String instanceId;
    @ApiModelProperty("策略别名")
    private String alias;
}
